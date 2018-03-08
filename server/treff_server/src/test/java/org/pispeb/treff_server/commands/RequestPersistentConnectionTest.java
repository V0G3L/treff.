package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.pispeb.treff_server.Server;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.networking.RequestHandler;
import org.pispeb.treff_server.networking.WebSocketEndpoint;
import org.pispeb.treff_server.update_notifier.PersistentConnection;

import javax.json.JsonObjectBuilder;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestPersistentConnectionTest extends MultipleUsersTest {

    public RequestPersistentConnectionTest() {
        super("request-persistent-connection");
    }

    @Test
    public void execute() {
        Thread t = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    User u = registerAccount("" + i,
                            "inthelandofmordorwheretheshadowslie");
                    SendContactRequestCommand sendContactRequestCommand
                            = new SendContactRequestCommand(accountManager,
                            mapper);
                    JsonObjectBuilder input
                            = getCommandStubForUser("send-contact-request",
                            u);
                    input.add("id", ownUser.id);
                    runCommand(sendContactRequestCommand, input);
                }
            }
        };
        t.start();
        Server server = mock(Server.class);
        when(server.getRequestHandler())
                .thenReturn(new RequestHandler(accountManager));
        WebSocketEndpoint wSE = new WebSocketEndpoint(server);
        Queue<String> q = new ConcurrentLinkedQueue<>();
        Session s = mock(Session.class);
        RemoteEndpoint.Basic b = mock(RemoteEndpoint.Basic.class);
        when(s.getBasicRemote()).thenReturn(b);
        try {
            doAnswer(invocation -> {
                        q.add((String) invocation.getArguments()[0]);
                        return null;
                    }).when(b).sendText(anyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        wSE.onMessage("\"{\"cmd\":\"request-persistent-connection\"}\"",
                s);
        int i = 1;
        while (!q.isEmpty()) {
            Assert.assertEquals(i++,
                    toJsonObject(q.poll()).getInt("count"));
        }
    }
}
