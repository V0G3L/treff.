package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.Permission;

import java.util.Date;
import java.util.Map;

public interface Usergroup extends DataObject, Comparable<Usergroup> {

    void setName(String name);

    String getName();

    void addMember(Account member);

    void removeMember(Account member);

    Map<Integer, ? extends Account> getAllMembers();

    Event createEvent(String title, Position position, Date timeStart,
                      Date timeEnd, Account creator);

    Map<Integer, ? extends Event> getAllEvents();

    Poll createPoll(String question, Account creator, boolean multichoice);

    Map<Integer, ? extends Poll> getAllPolls();

    boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException;

    Map<Permission, Boolean> getPermissionsOfMember(Account account);

    void setPermissionOfMember(Account member, Permission permission,
                               boolean valid)
            throws AccountNotInGroupException;

    Date getLocationSharingTimeEndOfMember(Account member)
            throws AccountNotInGroupException;

    void setLocationSharingTimeEndOfMember(Account member, Date timeEnd)
            throws AccountNotInGroupException;

}
