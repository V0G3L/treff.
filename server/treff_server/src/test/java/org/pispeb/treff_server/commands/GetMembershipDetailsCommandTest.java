package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.GroupDependentTest;

import javax.json.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class GetMembershipDetailsCommandTest extends GroupDependentTest {

    public GetMembershipDetailsCommandTest() {
        super("get-membership-details");
    }

    @Test
    public void valid() {
        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", users[1].id)
                .add("group-id", groupId);

        JsonObject output =
                runCommand(getMembershipDetailsCommand, inputBuilder);
        JsonObject membershipDesc = output.getJsonObject("membership");
        Assert.assertEquals(users[1].id,
                membershipDesc.getInt("account-id"));
        JsonObject permissions = membershipDesc.getJsonObject("permissions");
        Assert.assertTrue(permissions.getBoolean("edit_any_event"));
        Assert.assertTrue(permissions.getBoolean("create_poll"));
        Assert.assertTrue(permissions.getBoolean("change_permissions"));
        Assert.assertTrue(permissions.getBoolean("manage_members"));
        Assert.assertTrue(permissions.getBoolean("create_event"));
        Assert.assertTrue(permissions.getBoolean("edit_group"));
        Assert.assertTrue(permissions.getBoolean("edit_any_poll"));
    }

    @Test
    public void invalidUserId() {
        int id = 5;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 23;
        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", id)
                .add("group-id", groupId);

        JsonObject output =
                runCommand(getMembershipDetailsCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }

    @Test
    public void invalidGroupId() {
        int id = 23;
        while (id == groupId)
            id += 5;
        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", users[1].id)
                .add("group-id", id);

        JsonObject output =
                runCommand(getMembershipDetailsCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Test
    public void notInGroup() {
        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", users[3].id)
                .add("group-id", groupId);

        JsonObject output =
                runCommand(getMembershipDetailsCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1511);
    }
}