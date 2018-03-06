package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.CreateGroupCommand;
import org.pispeb.treff_server.commands.EditMembershipCommand;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract test class creating a group containing {@link MultipleUsersTest}s
 * members 0, 1, and 2 <b>but not 3</b>.
 * Members 0 and 1 have all permissions 2 has none at all.
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
        Set<User> members = new HashSet<>();
        members.add(ownUser);
        members.add(users[1]);
        members.add(users[2]);
        JsonArrayBuilder membersArrayBuilder = Json.createArrayBuilder();

        for (User member : members)
            membersArrayBuilder.add(member.id);

        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", groupName)
                .add("members", membersArrayBuilder.build())
                .build();

        JsonObjectBuilder input
                = getCommandStubForUser("create-group", ownUser)
                .add("group", group);

        groupId = runCommand(createGroupCommand, input).getInt("id");

        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(accountManager, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        JsonObjectBuilder permInput
                = getCommandStubForUser("edit-membership", ownUser)
                .add("id", groupId)
                .add("membership", Json.createObjectBuilder()
                        .add("account-id", users[2].id)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, permInput);

        // remove updates produced by group creation etc. to avoid interfering
        // with other commands
        members.remove(ownUser);
        for (User member : members) {
            List<JsonObject> updates = getUpdatesForUser(member);
            Assert.assertEquals(2, updates.size());
        }
    }
}
