package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.Update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "updates")
public class UpdateHib extends DataObjectHib implements Update {

    @Column
    @Lob
    private String content;

    @Override
    public Date getTime() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public String getUpdate() {
        return content;
    }

    @Override
    public boolean removeAffectedAccount(Account account) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, ? extends Account> getAffectedAccounts() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(Update o) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    public void setContent(String content) {
        this.content = content;
    }
}
