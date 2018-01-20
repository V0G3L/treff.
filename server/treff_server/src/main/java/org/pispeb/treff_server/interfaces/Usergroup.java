package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.sql.Permission;

import java.util.List;
import java.util.Set;

public interface Usergroup {

    void setName(String name) throws DatabaseException;

    String getName() throws DatabaseException;

    void addMember(Account member) throws DatabaseException;

    void removeMember(Account member) throws DatabaseException;

    Set<Account> getAllMembers() throws DatabaseException;

    void addEvent() throws DatabaseException;

    List<Event> getAllEvents() throws DatabaseException;

    void addPoll(Poll poll) throws DatabaseException;

    List<Poll> getAllPolls() throws DatabaseException;

    boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException, DatabaseException;

    void setPermissionOfMember(Account member, Permission permission, boolean
            value)
            throws AccountNotInGroupException, DatabaseException;

    // TODO: unload on inactivity

}
