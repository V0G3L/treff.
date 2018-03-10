package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.Nullable;

import org.pispeb.treff_client.data.database.UserDao;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Room {@link Entity} that represents a user, contains only as much information as the client needs
 */

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int userId;
    private String username;
    private boolean isFriend;
    private boolean isBlocked;
    private boolean isRequesting;
    private boolean requestPending;
    private Location location;

    public User(int userId,
                String username,
                boolean isFriend,
                boolean isBlocked,
                boolean isRequesting,
                boolean requestPending,
                Location location) {
        this.userId = userId;
        this.username = username;
        this.isFriend = isFriend;
        this.isBlocked = isBlocked;
        this.isRequesting = isRequesting;
        this.requestPending = requestPending;
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean requesting) {
        this.isRequesting = requesting;
    }

    public boolean isRequestPending() {
        return requestPending;
    }

    public void setRequestPending(boolean requestPending) {
        this.requestPending = requestPending;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (isFriend != user.isFriend) return false;
        if (isBlocked != user.isBlocked) return false;
        if (isRequesting != user.isRequesting) return false;
        if (requestPending != user.requestPending) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        return location != null ? location.equals(user.location) : user.location == null;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (isFriend ? 1 : 0);
        result = 31 * result + (isBlocked ? 1 : 0);
        result = 31 * result + (isRequesting ? 1 : 0);
        result = 31 * result + (requestPending ? 1 : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    /**
     * Like {@link #getPlaceholderAndScheduleQuery(int, UserRepository,
     * UserManipulationFunction)} but
     * operates on a {@code UserDao} instead and performs no additional
     * manipulation.
     *
     * @param userID  The ID of the user
     * @param userDao The {@code UserDao} in which to store the
     *                placeholder
     * @return The placeholder value that has been stored in the
     * {@code UserRepository}.
     * @see #getPlaceholderAndScheduleQuery(int, UserRepository,
     * UserManipulationFunction)
     */
    public static User getPlaceholderAndScheduleQuery(int userID,
                                                      UserDao userDao) {
        return getPlaceHolderAndScheduleQuery(userID, userDao::save, null);
    }

    /**
     * Like {@link #getPlaceholderAndScheduleQuery(int, UserRepository,
     * UserManipulationFunction)} but without additional manipulation.
     *
     * @param userID         The ID of the user
     * @param userRepository The user repository in which to store the
     *                       placeholder
     * @return The placeholder value that has been stored in the
     * {@code UserRepository}.
     * @see #getPlaceholderAndScheduleQuery(int, UserRepository,
     * UserManipulationFunction)
     */
    public static User getPlaceholderAndScheduleQuery(int userID,
                                                      UserRepository
                                                              userRepository) {
        return getPlaceHolderAndScheduleQuery(userID,
                userRepository::addUser, null);
    }

    /**
     * Creates a placeholder {@code User} for temporary display and schedules
     * the retrieval of that user's actual details.
     *
     * @param userID                 The ID of the user
     * @param userRepository         The user repository in which to store the
     *                               placeholder
     * @param additionalManipulation Optional additional manipulation to
     *                               apply to the placeholder before storing
     *                               it in the {@code UserRepository}.
     *                               May be null.
     * @return The placeholder value that has been stored in the
     * {@code UserRepository}.
     */
    public static User getPlaceholderAndScheduleQuery(
            int userID, UserRepository userRepository,
            @Nullable UserManipulationFunction additionalManipulation) {

        return getPlaceHolderAndScheduleQuery(userID,
                userRepository::addUser, additionalManipulation);
    }

    private interface UserStoreFunction {
        void store(User user);
    }

    public interface UserManipulationFunction {
        void manipulate(User user);
    }

    private static User getPlaceHolderAndScheduleQuery(
            int userID, UserStoreFunction storeFunction,
            UserManipulationFunction userManipulationFunction) {

        User placeholder = new User(userID, "unknown user",
                false, false, false, false,
                new Location("placeholder"));

        if (userManipulationFunction != null)
            userManipulationFunction.manipulate(placeholder);
        storeFunction.store(placeholder);
        RequestEncoder.getInstance()
                .getUserDetails(userID);
        return placeholder;
    }
}
