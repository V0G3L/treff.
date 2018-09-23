package org.pispeb.treffpunkt.server.service.domain;

public class Account {

    private int id;
    private String username;
    private String email;

    public Account() { }

    public Account(org.pispeb.treffpunkt.server.hibernate.Account hibAccount) {
        this.id = hibAccount.getID();
        this.username = hibAccount.getUsername();
        this.email = hibAccount.getEmail();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
