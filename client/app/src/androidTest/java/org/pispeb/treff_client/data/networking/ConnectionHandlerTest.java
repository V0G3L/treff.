package org.pispeb.treff_client.data.networking;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ConnectionHandlerTest {

    @Test
    public void sendMessage() throws Exception {
        final String[] parentReponse = {null};
        String uri = "ws://192.168.0.136:8080/adasdasd/ws";
        ConnectionHandler ch = new ConnectionHandler(uri,
                response -> parentReponse[0] = response);

        ch.sendMessage("BLABLAFUCKOFF");
        while (parentReponse[0] == null) { }
        System.out.println("Client received: " + parentReponse[0]);
        ch.disconnect();
    }
}