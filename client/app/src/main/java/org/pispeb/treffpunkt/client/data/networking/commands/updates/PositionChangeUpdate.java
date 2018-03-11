package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class PositionChangeUpdate extends Update {

    private final double latitude;
    private final double longitude;
    private final long timeMeasured;

    public PositionChangeUpdate(@JsonProperty("time-created") Date timeCreated,
                                @JsonProperty("creator") int creator,
                                @JsonProperty("latitude") double latitude,
                                @JsonProperty("longitude") double longitude,
                                @JsonProperty("time-measured")
                                        long timeMeasured) {
        super(timeCreated, creator);
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeMeasured = timeMeasured;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == user whose position changed
        User user = repositorySet.userRepository.getUser(creator);

        user.setLocation(toLocation(latitude, longitude, timeMeasured));

        repositorySet.userRepository.updateUser(user);
    }
}
