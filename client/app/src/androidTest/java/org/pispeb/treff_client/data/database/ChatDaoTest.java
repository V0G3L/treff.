package org.pispeb.treff_client.data.database;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pispeb.treff_client.data.entities.ChatMessage;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing functionality of saving and reading ChatMessages in the database
 */
@RunWith(AndroidJUnit4.class)
public class ChatDaoTest extends DaoTest {

    private ChatDao testChatDao;

    @Override
    @Before
    public void setDao() {
        testChatDao = getTestDb().getChatDao();
    }

    @Test
    public void insertMessageTest() {
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678,"User",  new Date());
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        //TODO checck if it's the right message
    }

    @Test
    public void readMessageTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678,"User",  new Date());
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        assertTrue(msg.equalContent(list.get(0)));
//        assertEquals(msg, list.get(0));
    }

    @Test
    public void groupMessagesTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678,"User",  new Date());
        ChatMessage msg2 = new ChatMessage(2345, "Hello2", 5678,"User",  new Date());
        testChatDao.save(msg);
        testChatDao.save(msg2);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        assertTrue(msg.equalContent(list.get(0)));
//        assertEquals(msg, list.get(0));
    }

    @Test
    public void duplicateTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678,"User",  new Date());
        testChatDao.save(msg);
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 2);

        assertTrue(msg.equalContent(list.get(0)));
        assertTrue(msg.equalContent(list.get(1)));
//        assertEquals(msg, list.get(0));
//        assertEquals(msg, list.get(1));
    }

    @Test
    public void deleteTest() {
        // depends on insert
        ChatMessage msg
                = new ChatMessage(1234, "Hello", 5678, "User", new Date());
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        testChatDao.deleteMessages(list);

        list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 0);
    }
}
