package org.pispeb.treffpunkt.server.commands.serializers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treffpunkt.server.commands.descriptions.MembershipDescriptionTest;
import org.pispeb.treffpunkt.server.commands.descriptions
        .MembershipEditDescriptionTest;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MembershipDescriptionTest.class,
        MembershipEditDescriptionTest.class,
        PollCompleteSerializerTest.class,
        PollOptionCompleteSerializerTest.class
})
public class SerializerTestSuite { }
