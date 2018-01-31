package org.pispeb.treff_server.commands.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Account;

import java.io.IOException;

public class AccountCompleteSerializer extends JsonSerializer<Account>{

    //TODO

    @Override
    public void serialize(Account value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

    }
}
