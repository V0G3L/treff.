package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

public interface PollOption extends DataObject {

    String getTitle() throws DatabaseException;

    void setTitle(String title) throws DatabaseException;

    Position getPosition() throws DatabaseException;

    void setPosition(Position position) throws DatabaseException;

}
