package org.pispeb.treff_client.data.networking.commands.updates;

import android.location.Location;
import android.location.LocationManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.osmdroid.util.LocationUtils;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.RepositorySet;

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
