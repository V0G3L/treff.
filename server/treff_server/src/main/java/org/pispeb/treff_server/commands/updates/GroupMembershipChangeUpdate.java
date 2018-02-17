package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class GroupMembershipChangeUpdate extends UpdateToSerialize {


    public GroupMembershipChangeUpdate(Date date, int creator) {
        //TODO
        super(Update.UpdateType.GROUP_MEMBERSHIP_CHANGE.toString(), date, creator);
    }
}
