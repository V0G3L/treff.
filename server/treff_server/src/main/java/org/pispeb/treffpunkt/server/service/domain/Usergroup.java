package org.pispeb.treffpunkt.server.service.domain;

import java.util.List;

public class Usergroup {

    private int id;
    private String name;
    private List<Integer> memberIDs;
    private List<Integer> eventIDs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(List<Integer> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public List<Integer> getEventIDs() {
        return eventIDs;
    }

    public void setEventIDs(List<Integer> eventIDs) {
        this.eventIDs = eventIDs;
    }

    // TODO: Polls
}
