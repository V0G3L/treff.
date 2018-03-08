package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Calendar timeVoteClose = new GregorianCalendar();
        timeVoteClose.set(2018,Calendar.JANUARY,1,13,33,37);
        when(poll.getTimeVoteClose())
                .thenReturn(timeVoteClose.getTime());

        // mock creator
        Account creator = mock(Account.class);
        when(poll.getCreator()).thenReturn(creator);
        when(creator.getID()).thenReturn(1337);

        // mock polloptions
        Map<Integer, PollOption> pollOptions = new HashMap<>();
        pollOptions.put(0, null);
        pollOptions.put(1, null);
        pollOptions.put(2, null);
        doReturn(pollOptions).when(poll).getPollOptions();

        PollCompleteSerializer pollCompleteSerializer
                = new PollCompleteSerializer();

        pollCompleteSerializer.serialize(poll, jsonGenerator,null);
        jsonGenerator.close();
        // TODO: actually test contents
        System.out.println(outputWriter.toString());
    }
}