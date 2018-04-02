package org.pispeb.treffpunkt.server.networking;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.Server;
import org.pispeb.treffpunkt.server.abstracttests.MultipleUsersTest;
import org.pispeb.treffpunkt.server.commands.SendContactRequestCommand;

import javax.json.JsonObjectBuilder;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersistentConnectionTest extends MultipleUsersTest {

    public PersistentConnectionTest() {
        super("request-persistent-connection");
    }

    @Ignore
    @Test
    public void execute() throws IOException {
        // Mock server, session, and remote
        // Create WebSocketEndPoint and call onMessage
        // Store Strings sent to session in Queue for later verification
        // TODO: migrate
        Server server = mock(Server.class);
//        when(server.getAccountManager())
//                .thenReturn(accountManager);
//        when(server.getRequestHandler())
//                .thenReturn(new RequestHandler(accountManager));

        WebSocketEndpoint wSE = new WebSocketEndpoint(server);
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Session session = mock(Session.class);
        RemoteEndpoint.Basic remote = mock(RemoteEndpoint.Basic.class);

        when(session.getBasicRemote()).thenReturn(remote);
        when(session.isOpen()).thenReturn(true);

        doAnswer(invocation -> {
            queue.add((String) invocation.getArguments()[0]);
            return null;
        }).when(remote).sendText(anyString());
        wSE.onMessage(inputBuilder.build().toString(), session);

        // create updates by registering other users
        // and sending contact requests
        int UPDATE_TARGET_COUNT = 100;
        for (int i = 0; i < UPDATE_TARGET_COUNT; i++) {
            System.out.println(String.format("Sending update %d/%d",
                    i + 1, UPDATE_TARGET_COUNT));
            User u = registerAccount("" + i,
                    "inthelandofmordorwheretheshadowslie");
            SendContactRequestCommand sendContactRequestCommand
                    = new SendContactRequestCommand(sessionFactory, mapper);
            JsonObjectBuilder input
                    = getCommandStubForUser("send-contact-request", u);
            input.add("id", ownUser.id);
            runCommand(sendContactRequestCommand, input);
        }

        // verify amount of updates
        for (int count = 1; count <= UPDATE_TARGET_COUNT; count++) {
            Assert.assertEquals(count,
                    toJsonObject(queue.poll()).getInt("count"));
        }
    }
}
