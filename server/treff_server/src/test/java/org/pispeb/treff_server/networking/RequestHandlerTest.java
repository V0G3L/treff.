package org.pispeb.treff_server.networking;

import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;

/**
 * @author tim
 */
public class RequestHandlerTest {

    private static JsonObject toJsonObject(String string) {
        return Json.createReader(new StringReader(string)).readObject();
    }

    @Test
    public void unknownCommand() {
        RequestHandler requestHandler = new RequestHandler(
                "{ \"cmd\": \"bla-bla-bla\" }", null);
        String response = requestHandler.run().responseString;
        Assert.assertEquals(toJsonObject(response).getInt("error"), 101);
    }
}
