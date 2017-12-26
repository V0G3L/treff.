package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Account;
import org.pispeb.treff_server.Event;
import org.pispeb.treff_server.Position;

import java.util.Date;
import java.util.Set;

public class EventSQL implements Event {

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setPosition(Position position) {

    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public void setTimeStart() {

    }

    @Override
    public Date getTimeStart() {
        return null;
    }

    @Override
    public void setTimeEnd() {

    }

    @Override
    public Date getTimeEnd() {
        return null;
    }

    @Override
    public Date getTimeCreated() {
        return null;
    }

    @Override
    public Account getCreator() {
        return null;
    }

    @Override
    public void addParticipant(Account participant) {

    }

    @Override
    public void removeParticipant(Account participant) {

    }

    @Override
    public Set<Account> getAllParticipants() {
        return null;
    }
}
