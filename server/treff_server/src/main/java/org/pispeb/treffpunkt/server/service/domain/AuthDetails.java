package org.pispeb.treffpunkt.server.service.domain;

/**
 * Authentication token and corresponding account ID returned by the server upon successful login
 * or registration.
 */
public class AuthDetails {

    private final String token;
    private final int id;

    public AuthDetails(String token, int id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

}
