package org.pispeb.treff_server.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Permission;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Immutable complete description of a membership as specified in the
 * treffpunkt protocol document.
 */
public class MembershipEditDescription {
    public final Map<Permission, Boolean> permissionMap;
    public final String type = "membership";
    public final int accountID;


    public MembershipEditDescription(@JsonProperty("account-id") int accountID,
                                 @JsonProperty("permissions")
                                         Map<String, Boolean>
                                         permissionStringMap)
            throws IOException {

        this.accountID = accountID;

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
