package org.pispeb.treff_client.networking;

/**
 * Created by Lukas on 1/5/2018.
 */

public class RequestEncoder {
    private static RequestEncoder entity;
    private ConnectionHandler connectionHandler;

    private final char SEP1 = (char) 1;
    private final char SEP2 = (char) 2;
    private final char SEP3 = (char) 3;

    public static RequestEncoder getEntity() {
        if(entity == null) {
            entity = new RequestEncoder();
        }
        return entity;
    }

    private RequestEncoder() {
        this.connectionHandler = new ConnectionHandler("", 1);
    }

    public int login(String username, String password) {
        connectionHandler.sendRequest("cmd" + SEP1 + username + SEP2 + password);
        return 0;
    }

    public int getUserId(String username) {
        return 0;
    }

    public boolean addContact(int userId) {
        return false;
    }

    public boolean removeContact(int userID) {
        return false;
    }

    public boolean blockAccount(int userId) {
        return false;
    }

    public boolean update_position(int lat, int lon, int time) { return false; }
}
