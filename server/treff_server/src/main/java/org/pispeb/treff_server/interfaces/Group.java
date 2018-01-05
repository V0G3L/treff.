package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.sql.Permission;

import java.util.List;
import java.util.Set;

public interface Group {

    void setName(String name);
    String getName();

    void addMember(Account member);
    void removeMember(Account member);
    Set<Account> getAllMembers();

    void addEvent();
    List<Event> getAllEvents();

    void addChatMessage(ChatMessage message);
    List<ChatMessage> getAllChatMessages(); // TODO: sorting by time

    void addPoll(Poll poll);
    List<Poll> getAllPolls();

    boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException;
    void setPermissionOfMember(Account member, Permission permission, boolean value)
            throws AccountNotInGroupException;

    // TODO: unload on inactivity

}
