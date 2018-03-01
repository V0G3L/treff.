package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Before;
import org.pispeb.treff_server.commands.CreateGroupCommand;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Abstract test class creating a group containing {@link MultipleUsersTest}s
 * members 0, 1, and 2 <b>but not 3</b>.
 */
public abstract class GroupDependentTest extends MultipleUsersTest {

    protected final String groupName
            = "<script>confirm('press cancel to wipe all data')</script>";
    protected int groupId;

    public GroupDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void createGroup() {
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownUser.id)
                .add(users[1].id)
                .add(users[2].id)
                .build();
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", groupName)
                .add("members", members)
                .build();

        JsonObjectBuilder input
                = getCommandStubForUser("create-group", ownUser)
                .add("group", group);

        groupId = runCommand(createGroupCommand, input).getInt("id");
    }
}
