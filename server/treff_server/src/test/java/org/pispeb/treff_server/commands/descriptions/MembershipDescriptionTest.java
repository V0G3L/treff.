package org.pispeb.treff_server.commands.descriptions;

import org.junit.Test;
import org.pispeb.treff_server.JsonDependentTest;
import org.pispeb.treff_server.Permission;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.pispeb.treff_server.Permission.*;

/**
 * @author tim
 */
public class MembershipDescriptionTest extends JsonDependentTest {

    @Test
    public void serialize() throws IOException {
        Map<Permission, Boolean> permMap = new HashMap<>();
        permMap.put(EDIT_ANY_EVENT, false);
        permMap.put(CREATE_POLL, false);
        permMap.put(CHANGE_PERMISSIONS, false);
        permMap.put(MANAGE_MEMBERS, false);
        permMap.put(CREATE_EVENT, false);
        permMap.put(EDIT_GROUP, false);
        permMap.put(EDIT_ANY_POLL, false);
        MembershipDescription desc
                = new MembershipDescription(1337, permMap, 0);

        String serialized = mapper.writeValueAsString(desc);

        // TODO: actually test contents
        System.out.println(serialized);
    }
}