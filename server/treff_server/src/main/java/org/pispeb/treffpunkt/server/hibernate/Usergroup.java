package org.pispeb.treffpunkt.server.hibernate;

import org.hibernate.Session;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.exceptions.AccountNotInGroupException;
import org.pispeb.treffpunkt.server.service.domain.Position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
    @MapKey(name = "account")
    private Map<Account, GroupMembership> memberships = new HashMap<>();
    @OneToMany(orphanRemoval = true)
    private Set<Event> events = new HashSet<>();
    @OneToMany(orphanRemoval = true)
    private Set<Poll> polls = new HashSet<>();

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
        GroupMembership gm = new GroupMembership(member, this);
        this.memberships.put(member, gm);
        member.addMembership(gm);
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
        GroupMembership gm = memberships.get(member);
        if (gm == null)
            throw new AccountNotInGroupException();

        member.removeMembership(gm);
        memberships.remove(member); // gets deleted by orphanremoval
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
        Event event = new Event(title, position, timeStart, timeEnd, creator);
        session.save(event);
        events.add(event);
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
        Poll poll = new Poll(question, multichoice, timeVoteClose, creator);
        session.save(poll);
        polls.add(poll);
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
        return true; // TODO: implement
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
        // TODO: implement
        return Arrays.stream(Permission.values())
                .collect(Collectors.toMap(Function.identity(), p -> true));
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
        // TODO: implement
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
        if (!memberships.containsKey(member))
            throw new AccountNotInGroupException();

        return memberships.get(member).getLocShareTimeEnd();
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
        if (!memberships.containsKey(member))
            throw new AccountNotInGroupException();

        memberships.get(member).setLocShareTimeEnd(timeEnd);
    }
}
