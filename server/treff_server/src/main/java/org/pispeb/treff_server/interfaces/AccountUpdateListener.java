package org.pispeb.treff_server.interfaces;

/**
 * A listener belonging to a single {@link Account} that performs
 * some action whenever an {@link Update} is marked as affecting
 * that Account.
 */
public interface AccountUpdateListener {

    /**
     * Called when an {@link Update} that affects this listener's
     * {@link Account} is added.
     * @param update The Update that was added
     */
    void onUpdateAdded(Update update);
}
