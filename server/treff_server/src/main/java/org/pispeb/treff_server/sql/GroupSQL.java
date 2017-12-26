package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Account;
import org.pispeb.treff_server.ChatMessage;
import org.pispeb.treff_server.Event;
import org.pispeb.treff_server.Group;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Poll;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupSQL implements Group {

    private Set<Account> members;
    private Map<Account, Set<Permission>> memberPermissions;

    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addMember(Account member) {
        // add bidirectional relation
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.addToGroup(this);
        }
    }

    @Override
    public void removeMember(Account member) {
        // remove bidirectional relation
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.removeFromGroup(this);
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
    public void addChatMessage(ChatMessage message) {

    }

    @Override
    public List<ChatMessage> getAllChatMessages() {
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
    public boolean checkPermissionOfMember(Account member, Permission permission) throws AccountNotInGroupException {
        return memberPermissions.get(member).contains(permission);
    }

    @Override
    public void setPermissionOfMember(Account member, Permission permission, boolean value) throws AccountNotInGroupException {

    }
}
