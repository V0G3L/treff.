package org.pispeb.treff_server.commands;

import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.networking.WebSocketEndpoint;
import org.pispeb.treff_server.update_notifier.PersistentConnection;

public class RequestPersistentConnectionTest extends MultipleUsersTest {

    public RequestPersistentConnectionTest() {
        super("request-persistent-connection");
    }

    @Test
    public void execute() {
        //TODO Kein Plan wie ich das testen soll
    }
}
