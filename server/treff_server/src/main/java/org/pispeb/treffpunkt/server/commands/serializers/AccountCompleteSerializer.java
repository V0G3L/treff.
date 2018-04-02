package org.pispeb.treffpunkt.server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treffpunkt.server.hibernate.Account;

import java.io.IOException;

public class AccountCompleteSerializer extends JsonSerializer<Account>{

    @Override
    public void serialize(Account account, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("type", "account");
        gen.writeNumberField("id", account.getID());
        gen.writeStringField("user", account.getUsername());

        gen.writeEndObject();
    }
}
