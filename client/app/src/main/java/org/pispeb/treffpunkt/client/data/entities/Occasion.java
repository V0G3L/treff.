package org.pispeb.treffpunkt.client.data.entities;

/**
 * Superclass of {@link Event} and Poll allowing usage in the same UI components
 */
public abstract class Occasion {
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
