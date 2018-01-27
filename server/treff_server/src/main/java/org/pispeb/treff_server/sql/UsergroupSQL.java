package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UsergroupSQL extends SQLObject implements Usergroup {

    private static final TableName TABLE_NAME = TableName.USERGROUPS;

    private Set<Account> members;
    private Map<Account, Set<Permission>> memberPermissions;

    UsergroupSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    @Override
    public void setName(String name) {
        setProperties(new AssignmentList()
                .put("name", name));
    }

    @Override
    public String getName() {
        return (String) getProperties("name").get("name");
    }

    @Override
    public void addMember(Account member) {
        // generate placeholders for all permissions
        String placeholders = String.join(",",
                Collections.nCopies(Permission.values().length, "?"));

        // generate values for placeholders
        List<Object> values = new LinkedList<>();
        values.add(TableName.GROUPMEMBERSHIPS.toString());
        // permission column names
        values.add(Arrays.stream(Permission.values())
                .map(Permission::toString)
                .collect(Collectors.joining(",")));
        values.add(member.getID());
        values.add(this.id);
        try {
            // ignore resultset. If it doesn't simply state OK, then an
            // exception is thrown anyways
            database.getQueryRunner().insert(
                    "INSERT INTO ?(accountid,usergroupid," +
                            placeholders +
                            ") VALUES (?,?," +
                            placeholders +
                            ");",
                    (rs) -> null,
                    values.toArray());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeMember(Account member) {
        try {
            database.getQueryRunner().update(
                    "DELETE FROM ? WHERE accountid=? AND usergroupid=?;",
                    TableName.GROUPMEMBERSHIPS.toString(),
                    member.getID(),
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        // clean up events, remove participation
        SortedSet<Event> events = new TreeSet<>(getAllEvents().values());
        events.forEach(e -> e.getReadWriteLock().writeLock().lock());
        try {
            events.forEach(e -> e.removeParticipant(member));
        } finally {
            events.forEach(e -> e.getReadWriteLock().writeLock().unlock());
        }

        // clean up polloptions, remove votes
        SortedSet<Poll> polls = new TreeSet<>(getAllPolls().values());
        polls.forEach(e -> e.getReadWriteLock().writeLock().lock());
        try {
            for (Poll p : polls) {
                List<PollOption> pollOptions = p.getPollOptions();
                pollOptions.forEach(pO
                        -> pO.getReadWriteLock().writeLock().lock());
                try {
                    pollOptions.forEach(pO -> pO.removeVoter(member));
                } finally {
                    pollOptions.forEach(pO
                            -> pO.getReadWriteLock().writeLock().unlock());
                }
            }
        } finally {
            polls.forEach(e -> e.getReadWriteLock().writeLock().unlock());
        }
    }

    @Override
    public Map<Integer, Account> getAllMembers() {
        // get ID list
        try {
            return database.getQueryRunner()
                    .query(
                            "SELECT accountid FROM ? WHERE usergroupid=?;",
                            new ColumnListHandler<Integer>(),
                            TableName.GROUPMEMBERSHIPS,
                            id)
                    .stream()
                    // create ID -> AccountSQL map
                    .collect(Collectors.toMap(Function.identity(),
                            EntityManagerSQL.getInstance()::getAccount));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Event createEvent(String title, Position position, Date timeStart,
                             Date timeEnd, Account creator) {
        int id;
        try {
            id = database.getQueryRunner().insert(
                    "INSERT INTO ?(title,latitude,longitude,timestart," +
                            "timeend,creator,usergroupid) VALUES " +
                            "(?,?,?,?,?,?,?);",
                    new ScalarHandler<Integer>(),
                    TableName.EVENTS,
                    title,
                    position.latitude,
                    position.longitude,
                    timeStart,
                    timeEnd,
                    creator,
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return EntityManagerSQL.getInstance().getEvent(id);
    }

    @Override
    public Map<Integer, Event> getAllEvents() {
        try {
            return database.getQueryRunner()
                    .query(
                            "SELECT id FROM ? WHERE usergroupid=?;",
                            new ColumnListHandler<Integer>(),
                            TableName.EVENTS,
                            id)
                    .stream()
                    // create ID -> EventSQL map
                    .collect(Collectors.toMap(Function.identity(),
                            EntityManagerSQL.getInstance()::getEvent));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Poll createPoll(String question, Account creator,
                           boolean multichoice) {
        int id;
        try {
            id = database.getQueryRunner().insert(
                    "INSERT INTO ?(question,creator,multichoice," +
                            "usergroupid) VALUES (?,?,?,?);",
                    new ScalarHandler<Integer>(),
                    TableName.POLLS,
                    question,
                    creator,
                    multichoice,
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return EntityManagerSQL.getInstance().getPoll(id);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        try {
            return database.getQueryRunner()
                    .query(
                            "SELECT id FROM ? WHERE usergroupid=?;",
                            new ColumnListHandler<Integer>(),
                            TableName.POLLS,
                            id)
                    .stream()
                    // create ID -> PollSQL map
                    .collect(Collectors.toMap(Function.identity(),
                            EntityManagerSQL.getInstance()::getPoll));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean checkPermissionOfMember(Account member, Permission
            permission) throws AccountNotInGroupException, DatabaseException {
        return memberPermissions.get(member).contains(permission);
    }

    @Override
    public void setPermissionOfMember(Account member, Permission permission,
                                      boolean value)
            throws AccountNotInGroupException, DatabaseException {

    }

    @Override
    public void delete() {
        // TODO
        // maybe throw exception when this still has members?
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int compareTo(Usergroup o) {
        return this.id - o.getID();
    }
}
