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
        RequestHandler requestHandler = new RequestHandler(null);
        String inputString = Json.createObjectBuilder()
                .add("cmd", "execute-order-66")
                .build().toString();
        String response
                = requestHandler.handleRequest(inputString).responseString;
        Assert.assertEquals(toJsonObject(response).getInt("error"),
                ErrorCode.UNKNOWN_COMMAND.getCode());
    }

    /**
     * Checks that the {@link RequestHandler} won't try to instantiate
     * an abstract command class when a user
     * specified its name and omitted the "Command" suffix.
     * At no point during development was there a really hacky reflection
     * based instantiation mechanic that allowed this to happen.
     */
    @Test
    public void abstractCommand() {
        RequestHandler requestHandler = new RequestHandler(null);
        String inputString = Json.createObjectBuilder()
                .add("cmd", "abstract")
                .build().toString();
        String response
                = requestHandler.handleRequest(inputString).responseString;
        Assert.assertEquals(toJsonObject(response).getInt("error"),
                ErrorCode.UNKNOWN_COMMAND.getCode());
    }
}
