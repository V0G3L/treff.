package org.pispeb.treffpunkt.server.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Server;
import org.pispeb.treffpunkt.server.commands.AbstractCommand;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;

public abstract class ServiceImpl {

    private static final ObjectMapper mapper = new ObjectMapper();

    private SessionFactory sessionFactory;

    static {
        // Do not fail on unknown properties. Important because cmd is extracted
        // seperately.
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // Allow serialization of empty CommandOutputs
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // Make sure mapping fails if a property is missing, we don't want
        // Jackson to just fill in defaults
        mapper.enable(
                DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
    }

    public ServiceImpl() {
        this.sessionFactory = Server.getInstance().getSessionFactory();
    }

    String execCommand(Class<? extends AbstractCommand> cmdClass, CommandInput input) {
        SessionFactory sessionFactory = Server.getInstance().getSessionFactory();
        AbstractCommand command = AbstractCommand.instantiate(cmdClass, sessionFactory);
        return command.execute(input);
    }
}
