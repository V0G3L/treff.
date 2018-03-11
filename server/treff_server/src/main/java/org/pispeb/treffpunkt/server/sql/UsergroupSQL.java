package org.pispeb.treffpunkt.server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.exceptions.AccountNotInGroupException;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.interfaces.*;
import org.pispeb.treffpunkt.server.sql.SQLDatabase.TableName;
import org.pispeb.treffpunkt.server.sql.resultsethandler.DataObjectHandler;
import org.pispeb.treffpunkt.server.sql.resultsethandler.DataObjectMapHandler;
import org.pispeb.treffpunkt.server.sql.resultsethandler.IDHandler;

import java.util.*;
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
        // clean up events: remove participation
        // then, clean up polloptions: remove votes
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
        return Collections.unmodifiableMap(database.query(
                "SELECT accountid FROM %s WHERE usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                new DataObjectMapHandler<>(AccountSQL.class, entityManager),
                id));
    }

    @Override
    public Event createEvent(String title, Position position, Date timeStart,
                             Date timeEnd, Account creator) {
        return database.insert(
                "INSERT INTO %s(title,latitude,longitude,timestart," +
                        "timeend,creator,usergroupid) VALUES " +
                        "(?,?,?,?,?,?,?);",
                TableName.EVENTS,
                new DataObjectHandler<>(EventSQL.class, entityManager),
                title,
                position.latitude,
                position.longitude,
                timeStart,
                timeEnd,
                creator.getID(),
                this.id);
    }

    @Override
    public Map<Integer, EventSQL> getAllEvents() {
        return Collections.unmodifiableMap(database.query(
                "SELECT id FROM %s WHERE usergroupid=?;",
                TableName.EVENTS,
                new DataObjectMapHandler<>(EventSQL.class, entityManager),
                id));
    }

    @Override
    public Poll createPoll(String question, Account creator, Date timeVoteClose,
                           boolean multichoice) {
        return database.insert(
                "INSERT INTO %s(question,creator,timevoteclose,multichoice," +
                        "usergroupid) VALUES (?,?,?,?,?);",
                TableName.POLLS,
                new DataObjectHandler<>(PollSQL.class, entityManager),
                question,
                creator.getID(),
                timeVoteClose,
                multichoice,
                this.id);
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
                "SELECT permission_" + permission.toString()
                        + " FROM %s WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
                new ScalarHandler<Boolean>(),
                member.getID(),
                this.id);
    }

    @Override
    public Map<Permission, Boolean> getPermissionsOfMember(Account member) {
        List<Permission> permissions = Arrays.asList(Permission.values());
        String placeholders = permissions
                .stream()
                .map(p -> "permission_" + p.toString())
                .collect(Collectors.joining(","));

        List<Boolean> values = database.query(
                "SELECT " + placeholders + " FROM %s " +
                        "WHERE usergroupid=? and accountid=?;",
                TableName.GROUPMEMBERSHIPS,
                rs -> {
                    List<Boolean> ret = new ArrayList<>();
                    rs.next();
                    for (int i = 1; i <= permissions.size(); i++)
                        ret.add(rs.getBoolean(i));
                    return ret;
                },
                id,
                member.getID());

        Map<Permission, Boolean> permissionMap = new HashMap<>();
        for (int i = 0; i < permissions.size(); i++)
            permissionMap.put(permissions.get(i), values.get(i));

        return Collections.unmodifiableMap(permissionMap);
    }

    @Override
    public void setPermissionOfMember(Account member, Permission permission,
                                      boolean value)
            throws AccountNotInGroupException {
        database.update(
                "UPDATE %s SET permission_" + permission.toString()
                        + "=? WHERE accountid=? AND usergroupid=?;",
                TableName.GROUPMEMBERSHIPS,
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
