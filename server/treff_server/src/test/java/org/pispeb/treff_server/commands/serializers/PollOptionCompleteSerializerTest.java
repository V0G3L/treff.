package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author tim
 */
public class PollOptionCompleteSerializerTest {

    @Test
    public void serialize() throws IOException {
        StringWriter outputWriter = new StringWriter();
        JsonGenerator jsonGenerator
                = new JsonFactory().createGenerator(outputWriter);
        jsonGenerator.useDefaultPrettyPrinter();

        // mock polloptions
        PollOption pollOption = mock(PollOption.class);
        when(pollOption.getID()).thenReturn(77);
        when(pollOption.getPosition())
                .thenReturn(new Position(13.37, 42.00));
        Calendar timeStart = new GregorianCalendar();
        Calendar timeEnd = new GregorianCalendar();
        timeStart.set(2018,Calendar.FEBRUARY,1,0,0,0);
        timeEnd.set(2018,Calendar.MARCH,4,5,6,7);
        when(pollOption.getTimeStart())
                .thenReturn(timeStart.getTime());
        when(pollOption.getTimeEnd())
                .thenReturn(timeEnd.getTime());
        when(pollOption.getReadWriteLock())
                .thenReturn(new ReentrantReadWriteLock());

        // mock voters
        Map<Integer, Account> voters = new HashMap<>();
        doReturn(voters).when(pollOption).getVoters();

        Account voter = mock(Account.class);
        when(voter.getID()).thenReturn(42);
        voters.put(42, voter);

        PollOptionCompleteSerializer pollOptionCompleteSerializer
                = new PollOptionCompleteSerializer();

        pollOptionCompleteSerializer.serialize(pollOption, jsonGenerator, null);
        jsonGenerator.close();
        // TODO: actually test contents
        System.out.println(outputWriter.toString());
    }
}