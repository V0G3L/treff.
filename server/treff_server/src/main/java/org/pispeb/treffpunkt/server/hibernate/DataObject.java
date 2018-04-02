package org.pispeb.treffpunkt.server.hibernate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Abstract class defining common properties and methods of the domain model
 * objects specified in the treffpunkt design document.
 * <p>
 * Any domain model object stored in the underlying persistent database is
 * represented by at most a single {@code DataObject} instance at a time.
 * Because of this, reference equality (==) can be used to check whether
 * two {@code DataObject}s represent the same domain model object.
 * <p>
 * All {@code DataObject}s have a unique numerical identifier.
 * The uniqueness of the identifier is only guaranteed over a set of objects
 * of the same {@code DataObject}-subtype:<br />
 * No two {@link Account}s can possess the same ID but an {@code Account} and a
 * {@link Poll} may possess the same ID.
 * <p>
 * The uniqueness of IDs and the fact that a single {@code DataObject} possesses
 * only a single ID also guarantees that any {@link Map}s of the form
 * [ID -> {@code DataObject-subtype}] returned by methods of
 * {@code DataObject}-subtypes are injective functions.
 * <p>
 * Because {@code DataObject} subtypes of different implementations are not
 * compatible in general, care should be taken to only supply
 * {@code DataObject}s as parameters to methods of {@code DataObject}s of the
 * same implementation.
 * To avoid {@code ClassCastException}s when using container classes returned
 * by methods of {@code DataObject}s, all returned containers are unmodifiable
 * and will throw a {@link UnsupportedOperationException}s upon invocation of
 * methods that write to their underlying data structures.
 * See {@link Collections#unmodifiableCollection(Collection)} for more details.
 * <p>
 * To avoid concurrency errors, all {@code DataObject}s possess a
 * {@link ReadWriteLock} that might need to be acquired before a method can
 * be safely invoked.
 * If and which lock must be acquired is defined in each method's documentation.
 * Note that {@link #getID()}, {@link #getReadWriteLock()}, and
 * {@link #isDeleted()} do not require locking.
 * <p>
 * To avoid deadlocks, there is a strict order in which
 * {@code DataObject}s are to be locked if a single thread intents to lock
 * multiple objects.
 * This order is based on the {@code DataObject} access hierarchy:
 * <p><code>
 * {@link Account}<br />
 * |-- {@link Usergroup}<br />
 * |&nbsp;&nbsp;&nbsp;|-- {@link Event}<br />
 * |&nbsp;&nbsp;&nbsp;|-- {@link Poll}<br />
 * |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|-- {@link PollOption}<br />
 * |-- {@link Update}<br />
 * </code></p>
 * Objects closer to its root are to be locked first.
 * If two objects' types are different but on the same level of the hierarchy,
 * which is true for ({@link Event}, {@link Poll}) and
 * ({@link Usergroup}, {@link Update}), they are to be locked in the following
 * order:
 * <ul>
 *     <li>{@code Event}s are to be locked before {@code Poll}s</li>
 *     <li>{@code Usergroup}s are to be locked before {@code Update}s</li>
 * </ul>
 * If two objects are of the same type, they are to be locked in ascending order
 * of their IDs.
 * To aid with this, all {@code DataObject}s are {@link Comparable} to
 * {@code DataObject}s of the same subtype and their natural ordering matches
 * the ascending order of their IDs.
 * <p>
 * All {@code DataObject}s support deletion via {@link #delete()}.
 * Deletion of an object will free the underlying persistent storage resources
 * and render it impossible to retrieve a reference on it from methods of this
 * package.
 * <p>
 * If an object has been deleted, in which case {@code isDeleted()} returns
 * true, only {@code isDeleted()}, {@code getID()}, and
 * {@code getReadWriteLock()} may be called on that object.
 * Because of this, it is strongly advised to always acquire the necessary
 * lock and then check for deletion before invoking any other methods.
 * The behaviour of a deleted {@code DataObject} is unspecified for all other
 * methods.
 * <p>
 * Further consequences of a deletion, i.e. effects on other {@code DataObject}s
 * are specified in the documentation of the subtype's {@code delete()} method.
 *
 * TODO: remove Locking stuff
 */
@MappedSuperclass
public abstract class DataObject {

    protected static Properties config;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    /**
         * Deletes this {@code DataObject}.
         * For a detailed specification of this method's consequences, see
         * {@link DataObject} or the specific subtype's documentation of this
         * method.
         */
    public void delete() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
         * Returns this {@code DataObjects} unique numerical identifier.
         * Note that IDs are only unique among the same {@code DataObject}-subtype.
         *
         * @return This {@code DataObject}s unique numerical identifier.
         */
    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass())
                && this.getID() == ((DataObject) obj).getID();
    }

    protected <T extends DataObject> Map<Integer, T> toMap(Collection<T> coll) {
        return Collections.unmodifiableMap(coll.stream()
                .collect(Collectors.toMap(DataObject::getID, Function.identity())));
    }

    public void setProperties(Properties config) {
        DataObject.config = config;
    }
}
