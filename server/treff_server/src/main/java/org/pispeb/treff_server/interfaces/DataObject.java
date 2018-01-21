package org.pispeb.treff_server.interfaces;

import java.util.concurrent.locks.ReadWriteLock;
import org.pispeb.treff_server.commands.AbstractCommand;

/**
 * @author tim
 */
public interface DataObject {

    /**
     * Returns the {@link ReadWriteLock} that each
     * {@link AbstractCommand} must obtain
     * before invoking methods on this {@link DataObject}.
     *
     * <p>The primary order in which locks are to be acquired matches their
     * access hierarchy, from root to leaves:</p>
     * <p><code>
     * {@link Account}<br />
     * |-- {@link Usergroup}<br />
     * |&nbsp;&nbsp;&nbsp;|-- {@link Event}<br />
     * |&nbsp;&nbsp;&nbsp;|-- {@link Poll}<br />
     * |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|-- {@link PollOption}<br />
     * |-- {@link Update}<br />
     * </code></p>
     *
     * <p>If two DataObjects are on the same level of the access hierarchy,
     * their locks are to be acquired in the order in which they are listed
     * above, e.g. Usergroup-Locks are to be acquired before Update-Locks.</p>
     *
     * <p>If two DataObjects are of the same type, their locks are to be
     * acquired in order of their ID, from lowest to highest.</p>
     * @return The ReadWriteLock of this DataObject
     */
    ReadWriteLock getReadWriteLock();

    /**
     * Returns whether this DataObject has been isDeleted and must no longer be
     * used.
     * @return <code>true</code> if the DataObject has been isDeleted,
     * <code>false</code> otherwise.
     */
    boolean isDeleted();

}
