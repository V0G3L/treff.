package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;
import org.pispeb.treff_server.sql.resultsethandler.DataObjectMapHandler;
import org.pispeb.treff_server.sql.resultsethandler.IDHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class UsergroupSQL extends SQLObject implements Usergroup {

    private static final TableName TABLE_NAME = TableName.USERGROUPS;

    UsergroupSQL(int id, SQLDatabase database,
                 EntityManagerSQL entityManager, Properties config) {
        super(id, TABLE_NAME, database, entityManager, config);
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
        // SQL string will contain a lot of placeholders and look like this:
        // INSERT INTO groupmemberships(accountid, usergroupid,
        // permission_columns...) VALUES (?,?,n*?);
        // (n = amount of permission columns = amount of permissions)


        // generate permission columns and n*?
        String permissionColumns =
                Arrays.stream(Permission.values())
                        .map(p -> "permission_" + p.toString())
                        .collect(Collectors.joining(","));
        String permissionValuePlaceholders = String.join(",",
                Collections.nCopies(Permission.values().length, "?"));

        // generate values for placeholders
        //
        // placeholders need to be matched values in the following order
        // - accountid
        // - usergroupid
        // - all permission default values, same order as column names

        List<Object> values = new LinkedList<>();
        // permission column names
        values.add(member.getID());
        values.add(this.id);
        // permission default values
        // TODO: define permission default values, currently all true
        values.addAll(Collections.nCopies(Permission.values().length, true));

        database.insert(
                "INSERT INTO %s(accountid,usergroupid," + permissionColumns +
                        ") VALUES (?,?," + permissionValuePlaceholders + ");",
                TableName.GROUPMEMBERSHIPS,
                // ignore resultset as no new ID is produced anyways
                (rs) -> null,
                values.toArray());
    }

    @Override
    public void removeMember(Account member) {
        // clean up events, remove participation
        // then, clean up polloptions, remove votes
        SortedSet<Event> events = new TreeSet<>(getAllEvents().values());
        SortedSet<Poll> polls = new TreeSet<>(getAllPolls().values());
        events.forEach(e -> e.getReadWriteLock().writeLock().lock());
        polls.forEach(e -> e.getReadWriteLock().writeLock().lock());
        try {
            // remove all event participations
            events.forEach(e -> e.removeParticipant(member));
            // go through all polloptions and remove votes
            for (Poll p : polls) {
                SortedSet<PollOption> pollOptions
                        = new TreeSet<>(p.getPollOptions().values());
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
            events.forEach(e -> e.getReadWriteLock().writeLock().unlock());
        }

        // remove membership from DB
        database.update(
                "DELETE FROM %s WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                member.getID(),
                this.id);
    }

    @Override
    public Map<Integer, AccountSQL> getAllMembers() {
        // get ID list
        return Collections.unmodifiableMap(database.query(
                "SELECT accountid FROM %s WHERE usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                new DataObjectMapHandler<AccountSQL>(AccountSQL.class,
                        entityManager),
                id));
    }

    @Override
    public Event createEvent(String title, Position position, Date timeStart,
                             Date timeEnd, Account creator) {
        int id;
        id = database.insert(
                "INSERT INTO %s(title,latitude,longitude,timestart," +
                        "timeend,creator,usergroupid) VALUES " +
                        "(?,?,?,?,?,?,?);",
                TableName.EVENTS,
                new ScalarHandler<Integer>(),
                title,
                position.latitude,
                position.longitude,
                timeStart,
                timeEnd,
                creator,
                this.id);
        return entityManager.getEvent(id);
    }

    @Override
    public Map<Integer, EventSQL> getAllEvents() {
        return Collections.unmodifiableMap(database.query(
                "SELECT id FROM %s WHERE usergroupid=?;",
                TableName.EVENTS,
                new DataObjectMapHandler<EventSQL>(EventSQL.class,
                        entityManager),
                id));
    }

    @Override
    public Poll createPoll(String question, Account creator,
                           boolean multichoice) {
        int id;
        id = database.insert(
                "INSERT INTO %s(question,creator,multichoice," +
                        "usergroupid) VALUES (?,?,?,?);",
                TableName.POLLS,
                new IDHandler(),
                question,
                creator,
                multichoice,
                this.id);
        return entityManager.getPoll(id);
    }

    @Override
    public Map<Integer, PollSQL> getAllPolls() {
        return Collections.unmodifiableMap(database.query(
                "SELECT id FROM %s WHERE usergroupid=?;",
                TableName.POLLS,
                new DataObjectMapHandler<>(PollSQL.class, entityManager),
                id));
    }

    @Override
    public boolean checkPermissionOfMember(Account member, Permission
            permission) throws AccountNotInGroupException {
        return database.query(
                "SELECT ? FROM %s WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                new ScalarHandler<Boolean>(),
                "permission_" + permission.toString(),
                member.getID(),
                this.id);
    }

    @Override
    public void setPermissionOfMember(Account member, Permission permission,
                                      boolean value)
            throws AccountNotInGroupException, DatabaseException {
        database.update(
                "UPDATE %s SET ?=? WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                "permission_" + permission.toString(),
                value,
                member.getID(),
                this.id);
    }

    @Override
    public Date getLocationSharingTimeEndOfMember(Account member)
            throws AccountNotInGroupException {
        return database.query("SELECT locsharetimeend FROM %s " +
                        "WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                new ScalarHandler<>(),
                member.getID(),
                id);
    }

    @Override
    public void setLocationSharingTimeEndOfMember(Account member, Date timeEnd)
            throws AccountNotInGroupException {
        database.update("UPDATE %s SET locsharetimeend=? " +
                        "WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                timeEnd,
                member.getID(),
                id);
    }

    @Override
    public void delete() {
        // delete is supposed to automatically be called when the last member
        // is removed. If there are still members, remove them all but don't
        // perform the actual deletion in this invocation. Removal of the last
        // member will cause this method to be invoked again.
        Collection<AccountSQL> members = getAllMembers().values();
        if (members.size() > 0) {
            members.forEach(this::removeMember);
        } else {
            // delete all events
            // then, delete all polls
            SortedSet<Event> events = new TreeSet<>(getAllEvents().values());
            SortedSet<Poll> polls = new TreeSet<>(getAllPolls().values());
            events.forEach(e -> e.getReadWriteLock().writeLock().lock());
            polls.forEach(e -> e.getReadWriteLock().writeLock().lock());
            try {
                events.forEach(Event::delete);
                polls.forEach(Poll::delete);
            } finally {
                polls.forEach(e -> e.getReadWriteLock().writeLock().unlock());
                events.forEach(e -> e.getReadWriteLock().writeLock().unlock());
            }

            deleted = true;
        }
    }

    @Override
    public int compareTo(Usergroup o) {
        return this.id - o.getID();
    }
}
