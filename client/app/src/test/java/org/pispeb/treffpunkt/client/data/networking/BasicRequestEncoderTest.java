package org.pispeb.treffpunkt.client.data.networking;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pispeb.treffpunkt.client.data.entities.ChatMessage;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Test the basic functionality of the requestEncoder such as correct
 * handling of commands and responses
 */

public class BasicRequestEncoderTest extends RequestEncoderTestHelper {

    private String message1 = "message1";
    private String message2 = "message2";
    private int groupId = 1234;

    private ArgumentCaptor<ChatMessage> msgCaptor
            = ArgumentCaptor.forClass(ChatMessage.class);
    private ArgumentCaptor<String> stringCaptor
            = ArgumentCaptor.forClass(String.class);

    @After
    public void close() {
        verifyNoMoreInteractions(mockConnectionHandler);
    }

    @Test
    public void singleTonTest() {
        RequestEncoder enc1 = RequestEncoder.getInstance();
        RequestEncoder enc2 = RequestEncoder.getInstance();

        assertTrue(enc1 == enc2);
    }

    @Test
    public void simpleCommandTest() {
        testEncoder.sendChatMessage(groupId, message1);
        verify(mockConnectionHandler).sendMessage(contains(message1));
    }

    @Test
    public void commandQueueTest1() {
        testEncoder.sendChatMessage(groupId, message1);
        testEncoder.sendChatMessage(groupId, message2);
        verify(mockConnectionHandler).sendMessage(contains(message1));
    }

    @Test
    public void commandQueueTest2() {
        // send to commands
        testEncoder.sendChatMessage(groupId, message1);
        testEncoder.sendChatMessage(groupId, message2);
        //responses for both
        testEncoder.onResponse("{}");
        testEncoder.onResponse("{}");

        verify(mockConnectionHandler, times(2)).sendMessage(stringCaptor.capture());
        List<String> chCalls = stringCaptor.getAllValues();
        assertTrue(chCalls.get(0).contains(message1));
        assertTrue(chCalls.get(1).contains(message2));

        verify(mockChatRepository, times(2)).addMessage(msgCaptor.capture());
        List<ChatMessage> repoCalls = msgCaptor.getAllValues();
        assertEquals(repoCalls.get(0).getContent(), message1);
        assertEquals(repoCalls.get(1).getContent(), message2);
    }
}
