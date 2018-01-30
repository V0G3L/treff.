package org.pispeb.treff_server.commands.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.pispeb.treff_server.JsonDependentTest;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;

import javax.json.Json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.mockito.Mockito.*;

/**
 * @author tim
 */
public class PollCompleteSerializerTest {

    @Test
    public void serialize() throws IOException {
        StringWriter outputWriter = new StringWriter();
        JsonGenerator jsonGenerator
                = new JsonFactory().createGenerator(outputWriter);
        jsonGenerator.useDefaultPrettyPrinter();

        Poll poll = mock(Poll.class);
        when(poll.getID()).thenReturn(4);
        when(poll.getQuestion()).thenReturn("the-question");
        when(poll.isMultiChoice()).thenReturn(true);
        when(poll.getTimeVoteClose())
                .thenReturn(new Date(2018, 1, 1));

        // mock creator
        Account creator = mock(Account.class);
        when(poll.getCreator()).thenReturn(creator);
        when(creator.getID()).thenReturn(1337);

        // mock polloptions
        Map<Integer, PollOption> pollOptions = new HashMap<>();
        when(poll.getPollOptions()).thenReturn(pollOptions);

        PollOption pollOption = mock(PollOption.class);
        when(pollOption.getID()).thenReturn(77);
        when(pollOption.getPosition())
                .thenReturn(new Position(13.37,42.00));
        when(pollOption.getTimeStart())
                .thenReturn(new Date(2018, 2, 1));
        when(pollOption.getTimeEnd())
                .thenReturn(new Date(2018, 3, 1));
        when(pollOption.getTitle()).thenReturn("the-title");
        when(pollOption.getReadWriteLock())
                .thenReturn(new ReentrantReadWriteLock());
        pollOptions.put(77, pollOption);

        // mock voters
        Map<Integer, Account> voters = new HashMap<>();
        when(pollOption.getVoters()).thenReturn(voters);

        Account voter = mock(Account.class);
        when(voter.getID()).thenReturn(42);
        voters.put(42, voter);

        PollCompleteSerializer pollCompleteSerializer
                = new PollCompleteSerializer();

        pollCompleteSerializer.serialize(poll, jsonGenerator,null);
        jsonGenerator.close();
        System.out.println(outputWriter.toString());
    }
}