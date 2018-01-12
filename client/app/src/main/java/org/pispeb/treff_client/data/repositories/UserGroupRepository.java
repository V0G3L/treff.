package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import javax.inject.Singleton;

@Singleton
public class UserGroupRepository {
    private UserGroupDao userGroupDao;
    private RequestEncoder encoder;

    public UserGroupRepository(UserGroupDao userGroupDao, RequestEncoder encoder) {
        this.userGroupDao = userGroupDao;
        this.encoder = encoder;
    }
}
