package org.pispeb.treffpunkt.server.service.domain;

public class Event {

    private int id;
    private String title;
    private long timeStart;
    private long timeEnd;
    private Position position;

    public Event() { }

    public Event(org.pispeb.treffpunkt.server.hibernate.Event hibEvent) {
        this.id = hibEvent.getID();
        this.title = hibEvent.getTitle();
        this.timeStart = hibEvent.getTimeStart().toInstant().getEpochSecond();
        this.timeEnd = hibEvent.getTimeEnd().toInstant().getEpochSecond();
        this.position = hibEvent.getPosition();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
