package org.pispeb.treffpunkt.server.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treffpunkt.server.Permission;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Immutable complete description of a membership as specified in the
 * treffpunkt protocol document.
 */
public class MembershipDescription {
    @JsonIgnore
    public final Map<Permission, Boolean> permissionMap;
    @JsonProperty("type")
    public final String type = "membership";
    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("account-id")
    public final int accountID;
    @JsonProperty("sharing-until")
    public Date sharingUntil;

    public MembershipDescription(int groupID, int accountID,
                                 long sharingUntil,
                                 Map<Permission, Boolean> permissionMap) {
        this.groupID = groupID;
        this.accountID = accountID;
        this.sharingUntil = new Date(sharingUntil);
        this.permissionMap = permissionMap;
    }

    @JsonProperty("permissions")
    public Map<String, Boolean> getPermissionStringMap() {
        // translate to mutable map, then only save an immutable view of it
        Map<String, Boolean> mutablePermissionMap
                = permissionMap.entrySet()
                .stream()
                .collect(Collectors
                        .toMap(es -> (es.getKey().toString()),
                                Map.Entry::getValue));
        return Collections.unmodifiableMap(mutablePermissionMap);
    }
}
