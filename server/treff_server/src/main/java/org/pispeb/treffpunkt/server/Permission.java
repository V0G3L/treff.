package org.pispeb.treffpunkt.server;

public enum Permission {
    EDIT_GROUP,
    MANAGE_MEMBERS,
    CHANGE_PERMISSIONS,
    CREATE_EVENT,
    EDIT_ANY_EVENT,
    CREATE_POLL,
    EDIT_ANY_POLL;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
