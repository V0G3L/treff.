package org.pispeb.treff_server.update_notifier;

import jdk.nashorn.api.scripting.JSObject;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.AccountUpdateListener;
import org.pispeb.treff_server.interfaces.Update;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;

/**
 * Represents a persistent ongoing connection hold to a user's device
 * through which update notifications can be sent.
 */
public class PersistentConnection implements AccountUpdateListener {

    private final PrintWriter out;
    private final Account observedAccount;

    /**
     * Takes over the connection of the specified {@link PrintWriter}
     * and writes update notifications onto it.
     * @param out The {@link PrintWriter} of an active connection
     */
    public PersistentConnection(PrintWriter out, AccountManager accountManager,
                                int userID) throws DatabaseException {
        this.out = out;
        this.observedAccount = accountManager.getAccountByID(userID);
        observedAccount.addUpdateListener(this);
    }

    @Override
    public void onUpdateAdded(Update update) throws DatabaseException {
        int updateCount = observedAccount.getUndeliveredUpdates().size();
        JsonObject message = Json.createObjectBuilder()
                .add("count",updateCount).build();
        out.println(message.toString());
    }
}
