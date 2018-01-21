package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

public class UsergroupSQL extends SQLObject implements Usergroup {

    private static final TableName TABLE_NAME = TableName.USERGROUPS;

    private Set<Account> members;
    private Map<Account, Set<Permission>> memberPermissions;

    UsergroupSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    @Override
    public void setName(String name) throws DatabaseException {

    }

    @Override
    public String getName() throws DatabaseException {
        return null;
    }

    @Override
    public void addMember(Account member) throws DatabaseException {
        // add bidirectional relation
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.addToGroup(this);
        }
    }

    @Override
    public void removeMember(Account member) throws DatabaseException {
        // remove bidirectional relation
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.removeFromGroup(this);
        }
    }

    @Override
    public Set<Account> getAllMembers() throws DatabaseException {
        return null;
    }

    @Override
    public void addEvent() {

    }

    @Override
    public List<Event> getAllEvents() throws DatabaseException {
        return null;
    }

    @Override
    public void addPoll(Poll poll) throws DatabaseException {

    }

    @Override
    public List<Poll> getAllPolls() throws DatabaseException {
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
}
