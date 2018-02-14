package org.pispeb.treff_server.commands.descriptions;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Usergroup;

import java.util.Date;
import java.util.Map;

/**
 * @author tim
 */
public class UsergroupComplete extends Description implements Usergroup {

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void addMember(Account member) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void removeMember(Account member) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, Account> getAllMembers() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Event createEvent(String title, Position position, Date timeStart,
                             Date timeEnd, Account creator) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, Event> getAllEvents() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Poll createPoll(String question, Account creator, boolean multichoice) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public boolean checkPermissionOfMember(Account member, Permission
            permission) throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setPermissionOfMember(Account member, Permission permission,
                                      boolean valid) throws
            AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(Usergroup o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
