package org.pispeb.treffpunkt.server.interfaces;


import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import java.util.Date;
import java.util.Map;

/**
 * Represents a change to {@link DataObject}s caused by user
 * interaction that affects other {@link Account}s.
 * <p>
 * Updates are naturally ordered from oldest to newest.
 */
public interface Update extends Comparable<Update>, DataObject {

    /**
     * Returns the time the {@code Update} was created
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The time the {@code Update} was created
     */
    Date getTime();

    /**
     * Returns the content of the {@code Update}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The content of the update in the format specified in the
     * treffpunkt protocol document
     */
    String getUpdate();

    /**
     * Removes an {@link Account} from the set of affected Accounts.
     * If the set is empty after the removal, this {@code Update} is deleted.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param account The Account to be removed
     * @return {@code true} if and only if the set of affected Accounts is
     * empty after removal.
     */
    boolean removeAffectedAccount(Account account);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s that this {@code Update} affects.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Accounts} that this {@code Update} affects.
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAffectedAccounts();

}
