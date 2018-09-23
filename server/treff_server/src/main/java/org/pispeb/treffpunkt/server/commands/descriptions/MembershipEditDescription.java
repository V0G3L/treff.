package org.pispeb.treffpunkt.server.commands.descriptions;

import org.pispeb.treffpunkt.server.Permission;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Immutable complete description of a membership as specified in the
 * treffpunkt protocol document, lacking the sharing-until property.
 */
public class MembershipEditDescription {
    public final Map<Permission, Boolean> permissionMap;
    public final String type = "membership";
    public final int groupID;
    public final int accountID;


    public MembershipEditDescription(int groupID, int accountID,
                                             Map<String, Boolean>
                                             permissionStringMap)
            throws IOException {

        this.accountID = accountID;
        this.groupID = groupID;

        // translate String->Boolean map to Permission->Boolean map
        // throw IOException if permission unknown. JsonDeserializer will
        // forward it and prompt AbstractClass to respond with a syntax error
        // message
        try {
            // translate to mutable map, then only save an immutable view of it
            Map<Permission, Boolean> mutablePermissionMap
                    = permissionStringMap.entrySet()
                    .stream()
                    .collect(Collectors
                            .toMap(es -> Permission
                                            .valueOf(es.getKey().toUpperCase()),
                                    Map.Entry::getValue));
            this.permissionMap
                    = Collections.unmodifiableMap(mutablePermissionMap);
        } catch (IllegalArgumentException e) {
            throw new IOException();
        }
    }
}
