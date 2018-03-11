package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class AccountDeletionUpdate extends Update {

    public AccountDeletionUpdate(@JsonProperty("time-created") Date timeCreated,
                                 @JsonProperty("creator") int creator) {
        super(timeCreated, creator);
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == account that was deleted
        // TODO: implement, super broken design right now, even server-side
        throw new UnsupportedOperationException();
    }
}
