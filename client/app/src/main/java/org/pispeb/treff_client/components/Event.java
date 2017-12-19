package org.pispeb.treff_client.components;

import java.time.LocalDateTime;

/**
 * Created by Lukas on 12/19/2017.
 */

public class Event {
    private int id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime start;
    private Position position;

    public Event(int id, String name, LocalDateTime created, LocalDateTime start) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.start = start;
    }
}
