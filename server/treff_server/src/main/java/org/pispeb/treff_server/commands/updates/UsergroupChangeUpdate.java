package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers.UsergroupCompleteSerializer;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.interfaces.Usergroup;

import java.util.Date;

public class UsergroupChangeUpdate extends UpdateToSerialize{
    @JsonProperty("usergroup")
    @JsonSerialize(using = UsergroupCompleteSerializer.class)
    Usergroup usergroup;

    public UsergroupChangeUpdate(Date date, int creator, Usergroup group) {
        super(Update.UpdateType.USERGROUP_CHANGE.toString(), date, creator);
        this.usergroup = usergroup;
    }
}
