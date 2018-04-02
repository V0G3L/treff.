package org.pispeb.treffpunkt.server.hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "groupmemberships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"account", "usergroup"}))
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account")
    public Account account;
    @ManyToOne(optional = false)
    @JoinColumn(name = "usergroup")
    public Usergroup usergroup;

    @Column
    private Date locShareTimeEnd;

    public GroupMembership() {
    }

    public GroupMembership(Account account, Usergroup usergroup) {
        this.account = account;
        this.usergroup = usergroup;
    }

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
}
