package org.pispeb.treff_client.repo;

import org.pispeb.treff_client.database.EventDao;
import org.pispeb.treff_client.database.UserDao;
import org.pispeb.treff_client.database.UserGroupDao;

/**
 * Created by Lukas on 1/3/2018.
 */

public class EntityRepository {

    private EventDao eventDao;
    private UserDao userDao;
    private UserGroupDao userGroupDao;

    public EntityRepository(EventDao eventDao, UserDao userDao, UserGroupDao userGroupDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.userGroupDao = userGroupDao;
    }

    public void addUser() {}

    public void addUserGroup() {}

    public void addEvent() {}
}
