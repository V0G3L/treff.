package org.pispeb.treff_client.data.networking.commands.updates;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteAccount;
import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.util.Date;

public class AccountChangeUpdate extends Update {

    private final CompleteAccount account;

    public AccountChangeUpdate(@JsonProperty("time-created") Date timeCreated,
                               @JsonProperty("creator") int creator,
                               @JsonProperty("account")
                                       CompleteAccount account) {
        super(timeCreated, creator);
        this.account = account;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // assert creator == account.id
        User user = repositorySet.userRepository.getUser(account.id);

        if (user != null) {
            user.setUsername(account.username);
            repositorySet.userRepository.updateUser(user);
        } else {
            user = new User(account.id, account.username, false, false,
                    false, false, new Location("update"));
            repositorySet.userRepository.addUser(user);
        }
    }
}
