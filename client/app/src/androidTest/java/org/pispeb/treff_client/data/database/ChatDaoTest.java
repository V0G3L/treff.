package org.pispeb.treff_client.data.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pispeb.treff_client.data.entities.ChatMessage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Testing functionality of saving and reading ChatMessages in the database
 */

@RunWith(AndroidJUnit4.class)
public class ChatDaoTest {

    private ChatDao testChatDao;
    private TreffDatabase testDb;


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        testDb = Room.inMemoryDatabaseBuilder(context, TreffDatabase.class)
                .build();
        testChatDao = testDb.getChatDao();
    }

    @After
    public void closeDb() throws IOException {
        testDb.close();
    }

    @Test
    public void insertMessageTest() {
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678, new Date());
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
    }

    @Test
    public void readMessageTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678, new Date());
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        assertEquals(msg, list.get(0));
    }

    @Test
    public void groupMessagesTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678, new Date());
        ChatMessage msg2 = new ChatMessage(2345, "Hello2", 5678, new Date());
        testChatDao.save(msg);
        testChatDao.save(msg2);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        assertEquals(msg, list.get(0));
    }

    @Test
    public void duplicateTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678, new Date());
        testChatDao.save(msg);
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 2);
        assertEquals(msg, list.get(0));
        assertEquals(msg, list.get(1));
    }

    @Test
    public void deleteTest() {
        // depends on insert
        ChatMessage msg = new ChatMessage(1234, "Hello", 5678, new Date());
        testChatDao.save(msg);

        List<ChatMessage> list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 1);
        testChatDao.deleteMessages(list);

        list = testChatDao.getMessageListByGroupId(1234);
        assertEquals(list.size(), 0);
    }
}
