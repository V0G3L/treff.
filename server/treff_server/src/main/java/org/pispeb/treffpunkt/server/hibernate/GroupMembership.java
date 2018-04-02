package org.pispeb.treffpunkt.server.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "groupmemberships")
@IdClass(GroupMembership.GMKey.class)
public class GroupMembership {

    @Id
    @ManyToOne(optional = false)
    private Account account;
    @Id
    @ManyToOne(optional = false)
    private Usergroup usergroup;

    @Column
    private Date locShareTimeEnd;

    public Date getLocShareTimeEnd() {
        return locShareTimeEnd;
    }

    public void setLocShareTimeEnd(Date locShareTimeEnd) {
        this.locShareTimeEnd = locShareTimeEnd;
    }

    public Usergroup getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(Usergroup usergroup) {
        this.usergroup = usergroup;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static class GMKey implements Serializable {

        public Account account;
        public Usergroup usergroup;

        public GMKey() { }

        public GMKey(Account account, Usergroup usergroup) {
            this.account = account;
            this.usergroup = usergroup;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof GMKey))
                return false;

            GMKey other = (GMKey) obj;
            return account.getID() == other.account.getID()
                    && usergroup.getID() == other.usergroup.getID();
        }

        @Override
        public int hashCode() {
            int hash = 41;
            hash = 31 * hash + account.getID();
            hash = 31 * hash + usergroup.getID();
            return hash;
        }
    }
}
