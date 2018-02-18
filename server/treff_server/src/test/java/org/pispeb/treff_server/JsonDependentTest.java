package org.pispeb.treff_server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.StringReader;

/**
 * @author tim
 */
public abstract class JsonDependentTest {

    protected static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    protected static JsonObject toJsonObject(String string) {
        return Json.createReader(new StringReader(string)).readObject();
    }

}
