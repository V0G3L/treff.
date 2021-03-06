package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.serializers.UsergroupCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;

import java.util.Date;

public class UsergroupChangeUpdate extends UpdateToSerialize{
    @JsonSerialize(using = UsergroupCompleteSerializer.class)
    public final Usergroup usergroup;

    public UsergroupChangeUpdate(Date date, int creator, Usergroup group) {
        super(UpdateType.USERGROUP_CHANGE.toString(), date, creator);
        this.usergroup = group;
    }
}
