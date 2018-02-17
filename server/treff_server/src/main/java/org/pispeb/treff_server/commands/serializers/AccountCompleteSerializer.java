package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Account;

import java.io.IOException;

public class AccountCompleteSerializer extends JsonSerializer<Account>{

    @Override
    public void serialize(Account account, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("type", "account");
        gen.writeNumberField("id", account.getID());
        gen.writeStringField("username", account.getUsername());

        gen.writeEndObject();
    }
}
