package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Collection;

/**
 * @author tim
 */
final class SerializerUtil {

    private SerializerUtil() { }

    static void writeIDArray(Collection<Integer> ids, String arrayName,
                                     JsonGenerator gen) throws IOException {

        gen.writeArrayFieldStart(arrayName);
        for (int id : ids)
            gen.writeNumber(id);
        gen.writeEndArray();
    }
}
