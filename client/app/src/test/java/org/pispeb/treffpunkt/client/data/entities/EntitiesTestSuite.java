package org.pispeb.treffpunkt.client.data.entities;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treffpunkt.client.data.entities.converter.ConverterTestSuite;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConverterTestSuite.class,
        ChatMessageTest.class,
        EventTest.class,
        GroupMembershipTest.class,
        UserGroupTest.class,
        UserTest.class
})
public class EntitiesTestSuite {
}
