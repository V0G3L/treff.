package org.pispeb.treff_client.networking;

import org.pispeb.treff_client.entities.Poll;

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

    public Integer[] listContacts() { return null;}

    public boolean blockAccount(int userId) {
        return false;
    }

    public boolean unblockAccount(int userId) {
        return false;
    }

    public int createGroup(String groupName, Integer[] memberIds) { return 0; }

    public boolean editGroupName(int groupId, String name) { return false; }

    public boolean addGroupMembers(int groupId, Integer[] newMembers) { return false; }

    public boolean removeGroupMembers(int groupId, Integer[] members) { return false; }

    public int createEvent(int groupId, String title, int timeStart,
                           int timeEnd, int lat, int lon) {
        return 0;
    }

    //public boolean editEvent(int groupId)

    public boolean removeEvent(int groupId, int eventId) { return false; }

    public Poll createPoll(int groupId, String question, boolean multichoice, String[] options) {
        return null;
    }

    //public boolean editPoll(int groupId, int pollId, )

    public boolean removePoll(int groupId, int pollId) { return false; }

    public boolean updatePosition(int lat, int lon, int time) { return false; }
}
