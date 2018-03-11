package org.pispeb.treffpunkt.client.data.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */
public class ChatMessageTest extends AbstractEntityTest{

    protected ChatMessage message2 =
            new ChatMessage(ids[0], messageStrings[0], ids[1], names[0], date1);

    protected ChatMessage message3 =
            new ChatMessage(ids[0], messageStrings[0], ids[1], names[0], date1);

    @Test
    public void getMessageId() throws Exception {
        assertEquals(0, message1.getMessageId());
    }

    @Test
    public void setMessageId() throws Exception {
        message2.setMessageId(1);
        assertEquals(1, message2.getMessageId());
    }

    @Test
    public void getGroupId() throws Exception {
        assertEquals(69, message1.getGroupId());
    }

    @Test
    public void setGroupId() throws Exception {
        message2.setGroupId(9997);
        assertEquals(9997, message2.getGroupId());
    }

    @Test
    public void getContent() throws Exception {
        assertEquals("Hallo", message1.getContent());
    }

    @Test
    public void setContent() throws Exception {
        message2.setContent("NEIN");
        //Getter is already tested
        assertEquals("NEIN", message2.getContent());
    }

    @Test
    public void getUserId() throws Exception {
        assertEquals(88, message1.getUserId());
    }

    @Test
    public void setUserId() throws Exception {
        message2.setUserId(99143);
        assertEquals(99143, message2.getUserId());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("Hans", message1.getUsername());
    }

    @Test
    public void setUsername() throws Exception {
        message2.setUsername("Wurst");
        assertEquals("Wurst", message2.getUsername());
    }

    @Test
    public void getTimeSent() throws Exception {
        assertEquals(date1, message1.getTimeSent());
    }

    @Test
    public void setTimeSent() throws Exception {
        message2.setTimeSent(date2);
        assertEquals(date2, message2.getTimeSent());
    }

    @Test
    public void equalContent() throws Exception {
        assertTrue("same message", message1.equalContent(message1));
        assertTrue("different messages", message1.equalContent(message3));
    }

    @Test
    public void equals() throws Exception {
        assertTrue("same message", message1.equals(message1));
        assertTrue("different messages", message1.equals(message3));
    }

}