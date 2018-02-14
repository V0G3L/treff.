package org.pispeb.treff_server.commands.descriptions;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.PollOption;

import java.util.Date;
import java.util.Map;

/**
 * @author tim
 */
public class PollOptionComplete extends Description implements PollOption {

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Position getPosition() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setPosition(Position position) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTimeStart(Date timeStart) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Date getTimeStart() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void setTimeEnd(Date timeEnd) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Date getTimeEnd() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void addVoter(Account voter) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void removeVoter(Account voter) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, Account> getVoters() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(PollOption o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
