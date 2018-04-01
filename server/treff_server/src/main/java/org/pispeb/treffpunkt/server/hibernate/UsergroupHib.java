package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.exceptions.AccountNotInGroupException;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.Event;
import org.pispeb.treffpunkt.server.interfaces.Poll;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "usergroups")
public class UsergroupHib extends DataObjectHib implements Usergroup {

    @Column
    private String name;
    @ManyToMany(mappedBy = "groups")
    private Set<AccountHib> members;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addMember(Account member) {
        this.members.add((AccountHib) member);
    }

    @Override
    public void removeMember(Account member) {
        this.members.remove((AccountHib) member);
    }

    @Override
    public Map<Integer, ? extends Account> getAllMembers() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Event createEvent(String title, Position position, Date timeStart, Date timeEnd, Account creator) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, ? extends Event> getAllEvents() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Poll createPoll(String question, Account creator, Date timeVoteClose, boolean multichoice) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, ? extends Poll> getAllPolls() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public boolean checkPermissionOfMember(Account member, Permission permission) throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Permission, Boolean> getPermissionsOfMember(Account member) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setPermissionOfMember(Account member, Permission permission, boolean valid) throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Date getLocationSharingTimeEndOfMember(Account member) throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setLocationSharingTimeEndOfMember(Account member, Date timeEnd) throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(Usergroup o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
