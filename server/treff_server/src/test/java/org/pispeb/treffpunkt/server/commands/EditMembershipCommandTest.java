package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class EditMembershipCommandTest  extends GroupDependentTest {

    public EditMembershipCommandTest() {
        super("edit-membership");
    }

    @Ignore
    @Test
    public void valid() {
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(sessionFactory, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("membership", Json.createObjectBuilder()
                        .add("account-id", users[1].id)
                        .add("group-id", groupId)
                        .add("permissions", permissionInput)
                        .build());

        runCommand(editMembershipCommand, inputBuilder);

        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(sessionFactory, mapper);
        JsonObjectBuilder input = Json.createObjectBuilder()
                .add("cmd", "get-membership-details")
                .add("token", ownUser.token)
                .add("id", users[1].id)
                .add("group-id", groupId);

        JsonObject output =
                runCommand(getMembershipDetailsCommand, input);
        JsonObject membershipDesc = output.getJsonObject("membership");
        Assert.assertEquals(users[1].id,
                membershipDesc.getInt("account-id"));
        JsonObject permissions = membershipDesc.getJsonObject("permissions");
        Assert.assertFalse(permissions.getBoolean("edit_any_event"));
        Assert.assertFalse(permissions.getBoolean("create_poll"));
        Assert.assertFalse(permissions.getBoolean("change_permissions"));
        Assert.assertFalse(permissions.getBoolean("manage_members"));
        Assert.assertFalse(permissions.getBoolean("create_event"));
        Assert.assertFalse(permissions.getBoolean("edit_group"));
        Assert.assertFalse(permissions.getBoolean("edit_any_poll"));
    }

    @Test
    public void invalidUserId() {
        int id = 5;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 23;
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(sessionFactory, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("membership", Json.createObjectBuilder()
                        .add("account-id", id)
                        .add("group-id", groupId)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }

    @Test
    public void invalidGroupId() {
        int id = 23;
        while (id == groupId)
            id += 5;
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(sessionFactory, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("membership", Json.createObjectBuilder()
                        .add("account-id", users[1].id)
                        .add("group-id", id)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Test
    public void notInGroup() {
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(sessionFactory, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("membership", Json.createObjectBuilder()
                        .add("account-id", users[3].id)
                        .add("group-id", groupId)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1511);
    }

    @Ignore
    @Test
    public void noPermission() {
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(sessionFactory, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        JsonObjectBuilder input =
                getCommandStubForUser("edit-membership", users[2]);
        input.add("membership", Json.createObjectBuilder()
                        .add("account-id", users[3].id)
                        .add("group-id", groupId)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, input);

        Assert.assertEquals(output.getInt("error"), 2000);
    }
}