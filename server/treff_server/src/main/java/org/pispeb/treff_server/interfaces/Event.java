package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.util.Date;
import java.util.Set;

public interface Event {

    void setTitle(String title) throws DatabaseException;

    String getTitle() throws DatabaseException;

    void setPosition(Position position) throws DatabaseException;

    Position getPosition() throws DatabaseException;

    void setTimeStart() throws DatabaseException;

    Date getTimeStart() throws DatabaseException;

    void setTimeEnd() throws DatabaseException;

    Date getTimeEnd() throws DatabaseException;

    Date getTimeCreated() throws DatabaseException;

    Account getCreator() throws DatabaseException;

    void addParticipant(Account participant) throws DatabaseException;

    void removeParticipant(Account participant) throws DatabaseException;

    Set<Account> getAllParticipants() throws DatabaseException;

}
