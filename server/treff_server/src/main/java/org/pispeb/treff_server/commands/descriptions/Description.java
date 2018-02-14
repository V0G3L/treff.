package org.pispeb.treff_server.commands.descriptions;

import org.pispeb.treff_server.interfaces.DataObject;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Abstract class representing object descriptions as specified in the
 * treffpunkt protocol document.
 * <p>
 * While {@code Descriptions} implement the same interfaces as actual,
 * database-backed objects, they are actually immutable objects holding
 * data that has been submitted by a user as part of a command parameter and as
 * such do not support invocation of their setter methods.
 * {@code Descriptions} also do not have a {@link ReadWriteLock} and thus do not
 * support {@link #getReadWriteLock()}.
 * Invocation of any unsupported function will result in an
 * {@link UnsupportedOperationException} being thrown immediately.
 * <p>
 * Since some commands do not require an ID to be set, {@link #getID()} might
 * also throw an {@code UnsupportedOperationException}.
 * This is further specified in the JavaDoc of the specific subclasses of this
 * abstract class.
 */
public abstract class Description implements DataObject {

    private int id = -1;
    private boolean isIDSet = false;

    /**
     * Initializes a new {@code Description} that does not contain an ID.
     * This constructor may be used for descriptions used in object creation
     * commands.
     */
    protected Description() { }

    /**
     * Initializes a new {@code Description} that contains an ID.
     */
    protected Description(int id) {
        this.id = id;
        this.isIDSet = true;
    }

    /**
     * Will return the described objects ID for {@code Descriptions} that
     * include IDs.
     * Throws an {@link UnsupportedOperationException} if invoked on a
     * {@code Description} that does not include an ID, e.g.
     * {@code Descriptions} used in object creation commands.
     * @return The described objects ID, if available.
     * @throws UnsupportedOperationException if no ID was specified for the
     * described object
     */
    @Override
    public int getID() {
        if (isIDSet)
            return id;
        else
            throw new UnsupportedOperationException("No ID specified.");
    }

    /**
     * Will throw an {@link UnsupportedOperationException}, as
     * {@code Descriptions} are immutable objects and as such have no
     * {@link ReadWriteLock}.
     * @return Nothing, as this methods always throws an exception.
     * @throws UnsupportedOperationException immediately upon invocation
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Will throw an {@link UnsupportedOperationException}, as
     * {@code Descriptions} are immutable objects and as such can't be deleted.
     * @throws UnsupportedOperationException immediately upon invocation
     */
    @Override
    public void delete() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Will throw an {@link UnsupportedOperationException}, as
     * {@code Descriptions} are immutable objects and as such can't be deleted.
     * @return Nothing, as this methods always throws an exception.
     * @throws UnsupportedOperationException immediately upon invocation
     */
    @Override
    public boolean isDeleted() {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
