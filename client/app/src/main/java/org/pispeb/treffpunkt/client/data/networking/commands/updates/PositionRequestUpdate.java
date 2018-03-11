package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class PositionRequestUpdate extends Update {

    private final Date endTime;

    public PositionRequestUpdate(@JsonProperty("time-created") Date timeCreated,
                                 @JsonProperty("creator") int creator,
                                 @JsonProperty("end-time") Date endTime) {
        super(timeCreated, creator);
        this.endTime = endTime;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // Maybe some notification? A chat message? 2000% volume porn
        // sounds? Device formatting?
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
