package org.pispeb.treffpunkt.server.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Represents a change to {@link DataObject}s caused by user
 * interaction that affects other {@link Account}s.
 * <p>
 * Updates are naturally ordered from oldest to newest.
 */
@Entity
@Table(name = "updates")
public class Update extends DataObject implements Comparable<Update> {

    @Column
    @Lob
    private String content;

    /**
         * Returns the content of the {@code Update}.
         * <p>
         * Requires a {@code ReadLock}.
         *
         * @return The content of the update in the format specified in the
         * treffpunkt protocol document
         */
    public String getUpdate() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the time the {@code Update} was created
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The time the {@code Update} was created
     */
    public Date getTime() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

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
    public boolean removeAffectedAccount(Account account) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s that this {@code Update} affects.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Accounts} that this {@code Update} affects.
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Integer, ? extends Account> getAffectedAccounts() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(Update o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
