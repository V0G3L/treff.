package org.pispeb.treff_client.data.networking.commands.updates;

import android.location.Location;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.json.Json;
import javax.json.stream.JsonParsingException;

public abstract class Update {

    protected final Date timeCreated;
    protected final int creator;

    public Update(Date timeCreated, int creator) {
        this.timeCreated = timeCreated;
        this.creator = creator;
    }

    /**
     * Applies the update, making the necessary changes to the repositories.
     * This method should only be run once per update.
     * @param repositorySet The currently used repositories that the update
     *                      should be applied on.
     */
    public abstract void applyUpdate(RepositorySet repositorySet);

    /**
     * Translates a JSON-encoded update into the corresponding Java class.
     * This method does not execute {@link Update#applyUpdate(RepositorySet)}.
     *
     * @param updateString A JSON-encoded update
     * @param mapper       The mapper to use for deserializing the update
     * @return The deserialized update.
     * @throws IOException          if the mapper can't deserialize the update.
     * @throws JsonParsingException if the given String is not a JSON object or
     *                              the type property is missing.
     */
    public static Update parseUpdate(String updateString, ObjectMapper mapper)
            throws IOException, JsonParsingException {
        String typeString = Json.createReader(new StringReader(updateString))
                .readObject()
                .getString("type");

        Class<? extends Update> updateClass
                = UpdateType.forTypeString(typeString).updateClass;
        return mapper.readValue(updateString, updateClass);
    }

    protected static Location toLocation(double latitude, double longitude) {
        return toLocation(latitude, longitude, 0);
    }

    protected static Location toLocation(double latitude,
                                         double longitude,
                                         long timeMeasured) {
        Location location = new Location("update");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(timeMeasured);
        return location;
    }
}
