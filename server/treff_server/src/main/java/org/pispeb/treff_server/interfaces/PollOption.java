package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;

public interface PollOption {

    String getTitle();
    void setTitle(String title);

    Position getPosition();
    void setPosition(Position position);

}
