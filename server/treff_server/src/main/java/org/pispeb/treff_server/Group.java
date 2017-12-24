package org.pispeb.treff_server;

import org.pispeb.treff_server.exceptions.AccountNotInGroupException;

import java.util.List;
import java.util.Set;

public interface Group {

    void setName(String name);
    String getName();

    void addMember(Account member);
    Set<Account> getAllMembers();

    void addEvent();
    List<Event> getAllEvents();

    void addChatMessage(ChatMessage message);
    List<ChatMessage> getAllChatMessages(); // TODO: sorting by time

    boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException;
    void setPermissionOfMember(Account member, Permission permission, boolean value)
            throws AccountNotInGroupException;

}
