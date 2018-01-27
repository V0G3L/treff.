package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface Event extends DataObject, Comparable<Event> {

    void setTitle(String title);

    String getTitle();

    void setPosition(Position position);

    Position getPosition();

    void setTimeStart(Date timeStart);

    Date getTimeStart();

    void setTimeEnd(Date timeEnd);

    Date getTimeEnd();

    Account getCreator();

    void addParticipant(Account participant);

    void removeParticipant(Account participant);

    Map<Integer, Account> getAllParticipants();

}
