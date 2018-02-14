package org.pispeb.treff_server.commands.descriptions;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;

import java.util.Date;
import java.util.Map;

/**
 * Representation of a complete event description as specified in the
 * treffpunkt protocol document.
 * <p>
 * May or may not include an ID, depending on which constructor was used.
 * Other objects holding a reference to an instance of this class should know
 * which constructor was used to create that instance.
 * <p>
 * Unsupported functions include:
 * <ul><li>All setters</li>
 *     <li>{@link #addParticipant(Account)}</li>
 *     <li>{@link #removeParticipant(Account)}</li>
 *     <li>{@link #getAllParticipants()}</li>
 * </ul>
 */
public class EventComplete extends Description implements Event {

    /** Creates a new object representing a complete description of an
     * {@link Event} <b>NOT</b> containing an ID.
     *
     * @param title The title of the event
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd The date and time at which the event ends
     * @param position The position at which the event takes place
     */
    public EventComplete(String title, int creatorID, Date timeStart,
                         Date timeEnd, Position position) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /** Creates a new object representing a complete description of an
     * {@link Event} containing an ID.
     *
     * @param id The ID of the event
     * @param title The title of the event
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd The date and time at which the event ends
     * @param position The position at which the event takes place
     */
    public EventComplete(int id, String title, int creatorID, Date timeStart,
                         Date timeEnd, Position position) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setPosition(Position position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Position getPosition() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTimeStart(Date timeStart) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getTimeStart() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTimeEnd(Date timeEnd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getTimeEnd() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Account getCreator() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void addParticipant(Account participant) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeParticipant(Account participant) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, Account> getAllParticipants() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(Event o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
