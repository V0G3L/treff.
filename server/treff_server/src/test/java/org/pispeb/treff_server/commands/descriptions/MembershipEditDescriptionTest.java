package org.pispeb.treff_server.commands.descriptions;

import org.junit.Test;
import org.pispeb.treff_server.abstracttests.JsonDependentTest;

import java.io.IOException;

/**
 * @author tim
 */
public class MembershipEditDescriptionTest extends JsonDependentTest {

    @Test
    public void deserialize() throws IOException {
        String input =
                "{\"account-id\": 1," +
                        "\"group-id\": 1,"+
                        "\"permissions\":{" +
                        "\"edit_any_event\":false,\"create_poll\":false," +
                        "\"change_permissions\":false," +
                        "\"manage_members\":false,\"create_event\":false," +
                        "\"edit_group\":false,\"edit_any_poll\":false" +
                        "}}";

        MembershipEditDescription desc
                = mapper.readValue(input, MembershipEditDescription.class);
        // TODO: actually test contents
    }


}