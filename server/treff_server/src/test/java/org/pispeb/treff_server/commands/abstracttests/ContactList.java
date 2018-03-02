package org.pispeb.treff_server.commands.abstracttests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author tim
 */
public class ContactList {

    public final Set<Integer> contacts;
    public final Set<Integer> incomingRequests;
    public final Set<Integer> outgoingRequests;
    public final Set<Integer> blocks;

    public ContactList(@JsonProperty("contacts")
                               int[] contacts,
                       @JsonProperty("incoming-requests")
                               int[] incomingRequests,
                       @JsonProperty("outgoing-requests")
                               int[] outgoingRequests,
                       @JsonProperty("blocks")
                               int[] blocks) {
        this.contacts = toIDSet(contacts);
        this.incomingRequests = toIDSet(incomingRequests);
        this.outgoingRequests = toIDSet(outgoingRequests);
        this.blocks = toIDSet(blocks);
    }

    private Set<Integer> toIDSet(int[] ids) {
        Set<Integer> set = new TreeSet<>();

        // assert that each ID is only included once
        for (int i : ids) {
            boolean isNewID = set.add(i);
            Assert.assertTrue(isNewID);
        }

        return Collections.unmodifiableSet(set);
    }

    /**
     * Checks the specified ContactList for equality with this one.
     * The two ContactLists are considered equal if for all sets in this
     * ContactList {@link Set#equals(Object)} with the corresponding Set in
     * the other ContactList returns true.
     *
     * @param obj The other ContactList
     * @return {@code true} if and only if the specified ContactList is
     * equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContactList))
            return false;

        ContactList other = (ContactList) obj;
        return other.contacts.equals(this.contacts)
                && other.incomingRequests.equals(this.incomingRequests)
                && other.outgoingRequests.equals(this.outgoingRequests);
    }
}
