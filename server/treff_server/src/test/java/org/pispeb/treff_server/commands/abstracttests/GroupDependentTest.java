package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.CreateGroupCommand;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.*;
import java.util.HashSet;
import java.util.Set;

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

        // remove updates produced by group creation to avoid interfering
        // with other commands
        members.remove(ownUser);
        for (User member : members)
            getSingleUpdateForUser(member);

    }
}
