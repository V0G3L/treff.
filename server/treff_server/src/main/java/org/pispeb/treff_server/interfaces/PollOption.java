package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.util.Set;

public interface PollOption extends DataObject, Comparable<PollOption> {

    String getTitle();

    void setTitle(String title);

    Position getPosition();

    void setPosition(Position position);

    void addVoter(Account voter);

    void removeVoter(Account voter);

    Set<Account> getVoters();

}
