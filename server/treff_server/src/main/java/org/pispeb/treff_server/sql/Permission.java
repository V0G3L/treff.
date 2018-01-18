package org.pispeb.treff_server.sql;

public enum Permission {
    CHANGE_GROUP_NAME("change_group_name"),
    MANAGE_MEMBERS("manage_members"),
    CHANGE_PERMISSIONS("change_permissions"),
    CREATE_EVENT("create_event"),
    EDIT_ANY_EVENT("edit_any_event"),
    CREATE_POLL("create_poll"),
    EDIT_ANY_POLL("edit_any_poll");

    private final String name;
    Permission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
