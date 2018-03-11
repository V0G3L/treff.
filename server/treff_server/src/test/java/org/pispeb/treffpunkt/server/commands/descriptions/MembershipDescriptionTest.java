package org.pispeb.treffpunkt.server.commands.descriptions;

import org.junit.Test;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.abstracttests.JsonDependentTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.pispeb.treffpunkt.server.Permission.*;

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
                = new MembershipDescription(7355608, 1337, 0, permMap);

        String serialized = mapper.writeValueAsString(desc);

        // TODO: actually test contents
        System.out.println(serialized);
    }
}