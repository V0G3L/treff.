package org.pispeb.treffpunkt.server.networking;

import com.jcabi.matchers.RegexMatchers;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.DatabaseDependentTest;
import org.pispeb.treffpunkt.server.abstracttests.JsonDependentTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class RequestHandlerTest extends DatabaseDependentTest{

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

    @Test
    public void validCommand() {
        RequestHandler requestHandler = new RequestHandler(sessionFactory);
        JsonObjectBuilder inputBuilder = Json.createObjectBuilder();
        inputBuilder.add("cmd","register");

        inputBuilder.add("user", "w4rum");
        inputBuilder.add("pass", "D4nz1g0rW4r");
        JsonObject response = toJsonObject(
                requestHandler.handleRequest(inputBuilder.build().toString())
                        .responseString);

        Assert.assertTrue(response.containsKey("id"));
        // throws exception if not a number
        response.getInt("id");
        Assert.assertTrue(response.containsKey("token"));
        Assert.assertTrue(Base64.isBase64(response.getString("token")));
    }
}
