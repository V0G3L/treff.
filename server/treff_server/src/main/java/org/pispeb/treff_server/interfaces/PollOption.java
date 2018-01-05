package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.sql.Position;

public interface PollOption {

    String getTitle();
    void setTitle(String title);

    Position getPosition();
    void setPosition(Position position);

}
