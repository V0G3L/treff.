package org.pispeb.treff_server.sql.interfaces;

import org.pispeb.treff_server.sql.Position;

import java.util.Date;
import java.util.Set;

public interface Event {

    void setTitle(String title);
    String getTitle();

    void setPosition(Position position);
    Position getPosition();

    void setTimeStart();
    Date getTimeStart();

    void setTimeEnd();
    Date getTimeEnd();

    Date getTimeCreated();

    Account getCreator();

    void addParticipant(Account participant);
    void removeParticipant(Account participant);
    Set<Account> getAllParticipants();

}
