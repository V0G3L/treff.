package org.pispeb.treff_server.networking;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.JsonDependentTest;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;

/**
 * @author tim
 */
public class RequestHandlerTest extends JsonDependentTest {

    @Test
    public void unknownCommand() {
        RequestHandler requestHandler = new RequestHandler(
                "{ \"cmd\": \"bla-bla-bla\" }", null);
        String response = requestHandler.run().responseString;
        Assert.assertEquals(toJsonObject(response).getInt("error"),
                ErrorCode.UNKNOWN_COMMAND.getCode());
    }
}
