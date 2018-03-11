package org.pispeb.treffpunkt.client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

/**
 * Created by matth on 17.02.2018.
 */
public class CompleteMembership {

    public final String type;
    public final int groupID;
    public final int accountID;
    public final Date sharingUntil;

    // TODO: translate to Permission->Boolean map (when perm. are implemented)
    public final Map<String, Boolean> permissions;

    public CompleteMembership(@JsonProperty("type") String type,
                              @JsonProperty("group-id") int groupID,
                              @JsonProperty("account-id") int accountID,
                              @JsonProperty("sharing-until") long sharingUntil,
                              @JsonProperty("permissions")
                                      Map<String, Boolean> permissions){
        this.type = type;
        this.groupID = groupID;
        this.accountID = accountID;
        this.sharingUntil = new Date(sharingUntil);
        this.permissions = permissions;
    }
}
