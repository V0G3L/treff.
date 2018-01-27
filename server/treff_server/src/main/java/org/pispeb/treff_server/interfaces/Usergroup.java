package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.Permission;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Usergroup extends DataObject, Comparable<Usergroup> {

    void setName(String name);

    String getName();

    void addMember(Account member);

    void removeMember(Account member);

    Map<Integer, Account> getAllMembers();

    Event createEvent(String title, Position position, Date timeStart,
                      Date timeEnd, Account creator);

    Map<Integer, Event> getAllEvents();

    Poll createPoll(String question, Account creator, boolean multichoice);

    Map<Integer, Poll> getAllPolls();

    boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException;

    void setPermissionOfMember(Account member, Permission permission,
                               boolean valid)
            throws AccountNotInGroupException;

    // TODO: unload on inactivity

}
