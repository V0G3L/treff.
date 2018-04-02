package org.pispeb.treffpunkt.server.networking;

import org.pispeb.treffpunkt.server.interfaces.AccountUpdateListener;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Represents a persistent ongoing connection hold to a user's device
 * through which update notifications can be sent.
 */
public class PersistentConnection implements AccountUpdateListener {

    private final Session session;
    private final Account observedAccount;

    /**
     * Takes over the connection of the specified {@link PrintWriter}
     * and writes update notifications onto it.
     *
     * @param session The {@link Session} of an active connection
     */
    public PersistentConnection(Session session, AccountManager accountManager,
                                int userID)  {
        this.session = session;
        this.observedAccount = accountManager.getAccount(userID);
        observedAccount.addUpdateListener(this);
    }

    @Override
    public void onUpdateAdded(Update update)  {
        // if the persistent connection was closed, stop listening
        if (!session.isOpen()) {
            observedAccount.removeUpdateListener(this);
            return;
        }

        int updateCount = observedAccount.getUndeliveredUpdates().size();
        JsonObject message = Json.createObjectBuilder()
                .add("count",updateCount).build();
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
