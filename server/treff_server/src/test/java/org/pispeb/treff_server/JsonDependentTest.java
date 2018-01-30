package org.pispeb.treff_server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;

/**
 * @author tim
 */
public abstract class JsonDependentTest {

    protected static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
