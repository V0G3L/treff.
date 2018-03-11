package org.pispeb.treffpunkt.client.data.entities.converter;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */

public class IntegerSetConverterTest {

    private String emptyString = "";
    private String nullString = null;
    private String oneElementString = "69";
    private String largeString = "1#88#69#0#180#9";
    private String largeStringSorted = "0#1#180#69#88#9#";

    private Set<Integer> nullSet = null;
    private Set<Integer> emptySet = new HashSet<Integer>();
    private Set<Integer> oneElementSet = new HashSet<Integer>(Arrays.asList(69));
    private Set<Integer> largeSet = new HashSet<Integer>(Arrays.asList(1, 88, 69, 0, 180, 9));

    @Test
    public void testToSet() {
        assertEquals(emptySet, IntegerSetConverter.toSet(emptyString));
        assertEquals(null, IntegerSetConverter.toSet(nullString));
        assertEquals(oneElementSet, IntegerSetConverter.toSet(oneElementString));
        assertEquals(largeSet, IntegerSetConverter.toSet(largeString));
    }

    @Test
    public void testToString() {
        assertEquals(nullString, IntegerSetConverter.toString(nullSet));
        assertEquals(emptyString, IntegerSetConverter.toString(emptySet));
        assertEquals(oneElementString + "#", IntegerSetConverter.toString(oneElementSet));
        assertEquals(largeStringSorted, IntegerSetConverter.toString(largeSet));
    }
}
