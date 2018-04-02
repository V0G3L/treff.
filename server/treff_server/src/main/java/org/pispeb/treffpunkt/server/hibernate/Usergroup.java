package org.pispeb.treffpunkt.server.hibernate;

import org.hibernate.Session;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.exceptions.AccountNotInGroupException;
import org.pispeb.treffpunkt.server.hibernate.GroupMembership.GMKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An object representing a user group with an name, set of {@link Event}s, set
 * of {@link Poll}s, and set of member {@link Account}s.
 * Each member has an associated set of {@link Permission}s and an associated
 * point in time until which that member shares its location with the group.
 */
@Entity
@Table(name = "usergroups")
public class Usergroup extends DataObject {

    @Column
    private String name;
    @OneToMany(mappedBy = "usergroup", orphanRemoval = true)
    private Map<GMKey, GroupMembership> memberships;
    @OneToMany(orphanRemoval = true)
    private Set<Event> events;
    @OneToMany(orphanRemoval = true)
    private Set<Poll> polls;

    /**
     * Set this groups name
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns this group's name
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return This group's name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a member to this group.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param member The member to be added
     */
    public void addMember(Account member, Session session) {
        GroupMembership gm = new GroupMembership();
        gm.setAccount(member);
        gm.setUsergroup(this);
        session.save(gm);
    }

    /**
     * Removes a member from this group.
     * If this removes the last remaining member, the group is deleted after the
     * last member is removed.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param member The member to be removed
     */
    public void removeMember(Account member, Session session) {
        GMKey key = new GMKey(member, this);
        if (!memberships.containsKey(key))
            throw new AccountNotInGroupException();

        memberships.remove(key); // gets deleted by orphanremoval
        if (memberships.isEmpty())
            session.delete(this);
    }

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s that members of this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Accounts} that are members of this group
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Integer, ? extends Account> getAllMembers() {
        Set<Account> members = memberships.values().stream()
                .map(GroupMembership::getAccount)
                .collect(Collectors.toSet());
        return toMap(members);
    }

    /**
     * Returns an unmodifiable [ID -> {@code Event}] map holding all
     * {@code Event}s that part of this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Event} that are part of this group
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Integer, ? extends Event> getAllEvents() {
        return toMap(events);
    }

    /**
     * Returns an unmodifiable [ID -> {@code Poll}] map holding all
     * {@code Poll}s that part of this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Poll} that are part of this group
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Integer, ? extends Poll> getAllPolls() {
        return toMap(polls);
    }

    /**
     * Creates a new {@code Event} with the supplied details.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param title     The title of the new {@code Event}
     * @param position  The position of the new {@code Event}
     * @param timeStart The start time of the new {@code Event}
     * @param timeEnd   The end time of the {@code Event}
     * @param creator   The {@code Account} that created the new {@code Event}
     * @return The newly created {@code Event}
     */
    public Event createEvent(String title, Position position, Date timeStart,
                             Date timeEnd, Account creator, Session session) {
        Event event = new Event();
        event.setTitle(title);
        event.setPosition(position);
        event.setTimeStart(timeStart);
        event.setTimeEnd(timeEnd);
        event.setCreator(creator);
        session.save(event);
        return event;
    }

    /**
     * Creates a new {@code Poll} with the supplied details.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param question    The question of the new {@code Poll}
     * @param creator     The {@code Account} that created the new {@code Poll}
     * @param multichoice {@code true} if and only if {@code Account}s can vote
     *                    for multiple {@link PollOption}s at once
     * @return The newly created {@code Poll}
     */
    public Poll createPoll(String question, Account creator, Date timeVoteClose,
                           boolean multichoice, Session session) {
        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setCreator(creator);
        poll.setTimeVoteClose(timeVoteClose);
        poll.setMultiChoice(multichoice);
        session.save(poll);
        return poll;
    }

    /**
     * Checks whether the supplied member has the supplied
     * {@code Permission} in this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param member     The member
     * @param permission The {@code Permission}
     * @return {@code true} if and only if the supplied member has the supplied
     * {@code Permission}
     * @throws AccountNotInGroupException if the supplied {@code Account} is not
     *                                    a member of this group
     */
    public boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns an unmodifiable [{@code Permission} -> {@code Boolean}] map
     * mapping each available {@code Permission} to a boolean for the
     * supplied member. A value of {@code true} indicates that the member has
     * that {@code Permission} while {@code false} indicates the opposite.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Boolean}s for all available {@code Permission}s
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Permission, Boolean> getPermissionsOfMember(Account member) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Grants or removes the supplied {@code Permission} to or from the supplied
     * member in this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param member     The member
     * @param permission The {@code Permission}
     * @param valid      {@code true} if an only if the supplied member
     *                   should have the supplied {@code Permission}
     *                   when this method returns
     * @throws AccountNotInGroupException if the supplied {@code Account} is not
     *                                    a member of this group
     */
    public void setPermissionOfMember(Account member, Permission permission,
                                      boolean valid)
            throws AccountNotInGroupException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns the point in time until which the supplied member shares its
     * location with this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param member The member
     * @return The location sharing end time
     * @throws AccountNotInGroupException if the supplied {@code Account} is not
     *                                    a member of this group
     */
    public Date getLocationSharingTimeEndOfMember(Account member)
            throws AccountNotInGroupException {
        GMKey key = new GMKey(member, this);
        if (!memberships.containsKey(key))
            throw new AccountNotInGroupException();

        return memberships.get(key).getLocShareTimeEnd();
    }

    /**
     * Sets the point in time until which the supplied member shares its
     * location with this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param member  The member
     * @param timeEnd The location sharing end time
     * @throws AccountNotInGroupException if the supplied {@code Account} is not
     *                                    a member of this group
     */
    public void setLocationSharingTimeEndOfMember(Account member, Date timeEnd)
            throws AccountNotInGroupException {
        GMKey key = new GMKey(member, this);
        if (!memberships.containsKey(key))
            throw new AccountNotInGroupException();

        memberships.get(key).setLocShareTimeEnd(timeEnd);
    }
}
