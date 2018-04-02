package org.pispeb.treffpunkt.server.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @Column
    private Date timeCreated;

    @ManyToMany(mappedBy = "undeliveredUpdates")
    private Set<Account> affectedAccounts = new HashSet<>();

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

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * Returns the time the {@code Update} was created
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The time the {@code Update} was created
     */
    public Date getTime() {
        return timeCreated;
    }

    @Override
    public int compareTo(Update o) {
        return this.getID() - o.getID();
    }

    void addAffectedAccount(Account account) {
        affectedAccounts.add(account);
    }

    void removeAffectedAccount(Account account) {
        affectedAccounts.remove(account);
    }
}
