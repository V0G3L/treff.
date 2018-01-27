package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
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

    }

    @Override
    public String getName() {
        return null;
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
        List<Event> events = getAllEvents();
        events.forEach(e -> e.getReadWriteLock().writeLock().lock());
        try {
            events.forEach(e -> e.removeParticipant(member));
        } finally {
            events.forEach(e -> e.getReadWriteLock().writeLock().unlock());
        }

        // clean up polloptions, remove votes
        List<Poll> polls = getAllPolls();
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
    public Set<Account> getAllMembers() {
        return null;
    }

    @Override
    public void addEvent() {

    }

    @Override
    public List<Event> getAllEvents() {
        return null;
    }

    @Override
    public void addPoll(Poll poll) {

    }

    @Override
    public List<Poll> getAllPolls() {
        return null;
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
    public ReadWriteLock getReadWriteLock() {
        return null;
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
