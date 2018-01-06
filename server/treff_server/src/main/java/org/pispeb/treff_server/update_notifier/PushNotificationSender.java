package org.pispeb.treff_server.update_notifier;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountUpdateListener;
import org.pispeb.treff_server.interfaces.Update;

/**
 * Class used to notify {@link org.pispeb.treff_server.interfaces.Account}s of
 * updates via Android Push Notifications.
 */
public class PushNotificationSender implements AccountUpdateListener {

    private final Account account;

    /**
     * Creates a new {@link PushNotificationSender} for the specified
     * {@link Account}.
     * Will automatically register as an {@link AccountUpdateListener}
     * of the specified {@link Account}.
     * @param account The account to notify of {@link Update}s affecting
     *                it
     */
    public PushNotificationSender(Account account) {
        this.account = account;
        account.addUpdateListener(this);
    }

    @Override
    public void onUpdateAdded(Update update) {
        // send push notification
    }
}
