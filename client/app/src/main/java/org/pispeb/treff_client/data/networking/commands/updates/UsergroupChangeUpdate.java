package org.pispeb.treff_client.data.networking.commands.updates;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteUsergroup;
import org.pispeb.treff_client.data.repositories.RepositorySet;
import org.pispeb.treff_client.view.util.TreffPunkt;

import java.util.Date;

public class UsergroupChangeUpdate extends Update {

    private final CompleteUsergroup usergroup;

    public UsergroupChangeUpdate(@JsonProperty("time-created") Date timeCreated,
                                 @JsonProperty("creator") int creator,
                                 @JsonProperty("usergroup") CompleteUsergroup group) {
        super(timeCreated, creator);
        this.usergroup = group;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        UserGroup userGroupEntity = repositorySet.userGroupRepository
                .getGroup(usergroup.id);
        boolean newGroup = (userGroupEntity == null);

        // if we were added to a group for the first time,
        // we don't yet know that group
        if (newGroup) {
            userGroupEntity = new UserGroup(usergroup.id, usergroup.name);
        } else {
            userGroupEntity.setName(usergroup.name);
        }

        // no need to update events and polls, as changes to the existence of
        // those are reported as *-change or *-deletion updates

        // update members
        repositorySet.userGroupRepository
                .updateGroupMembers(usergroup.id, usergroup.members);

        // TODO: this seems way too complex for something as simple as this
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int ownID = pref.getInt(ctx.getString(R.string.key_userId), -1);

        boolean kickedOut = true;
        for (int memberID : usergroup.members)
            if (memberID == ownID)
                kickedOut = false;

        if (kickedOut)
            repositorySet.userGroupRepository.remove(userGroupEntity);
        else {
            if (newGroup)
                repositorySet.userGroupRepository.addGroup(userGroupEntity);
            else
                repositorySet.userGroupRepository.updateGroup(userGroupEntity);
        }
    }
}
