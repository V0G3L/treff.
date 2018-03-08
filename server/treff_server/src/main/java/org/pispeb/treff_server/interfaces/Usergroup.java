package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.Permission;

import java.util.Date;
import java.util.Map;

/**
 * An object representing a user group with an name, set of {@link Event}s, set
 * of {@link Poll}s, and set of member {@link Account}s.
 * Each member has an associated set of {@link Permission}s and an associated
 * point in time until which that member shares its location with the group.
 */
public interface Usergroup extends DataObject, Comparable<Usergroup> {

    /**
     * Set this groups name
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param name New name
     */
    void setName(String name);

    /**
     * Returns this group's name
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return This group's name
     */
    String getName();

    /**
     * Adds a member to this group.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param member The member to be added
     */
    void addMember(Account member);

    /**
     * Removes a member from this group.
     * If this removes the last remaining member, the group is deleted after the
     * last member is removed.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param member The member to be removed
     */
    void removeMember(Account member);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s that members of this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Accounts} that are members of this group
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllMembers();

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
    Event createEvent(String title, Position position, Date timeStart,
                      Date timeEnd, Account creator);

    /**
     * Returns an unmodifiable [ID -> {@code Event}] map holding all
     * {@code Event}s that part of this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Event} that are part of this group
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Event> getAllEvents();

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
    Poll createPoll(String question, Account creator, Date timeVoteClose,
                    boolean multichoice);

    /**
     * Returns an unmodifiable [ID -> {@code Poll}] map holding all
     * {@code Poll}s that part of this group.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Poll} that are part of this group
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Poll> getAllPolls();

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
    boolean checkPermissionOfMember(Account member, Permission permission)
            throws AccountNotInGroupException;

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
    Map<Permission, Boolean> getPermissionsOfMember(Account member);

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
    void setPermissionOfMember(Account member, Permission permission,
                               boolean valid)
            throws AccountNotInGroupException;

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
    Date getLocationSharingTimeEndOfMember(Account member)
            throws AccountNotInGroupException;

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
    void setLocationSharingTimeEndOfMember(Account member, Date timeEnd)
            throws AccountNotInGroupException;

}
