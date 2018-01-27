package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface PollOption extends DataObject, Comparable<PollOption> {

    String getTitle();

    void setTitle(String title);

    Position getPosition();

    void setPosition(Position position);

    void setTimeStart(Date timeStart);

    Date getTimeStart();

    void setTimeEnd(Date timeEnd);

    Date getTimeEnd();

    void addVoter(Account voter);

    void removeVoter(Account voter);

    Map<Integer, Account> getVoters();

}
