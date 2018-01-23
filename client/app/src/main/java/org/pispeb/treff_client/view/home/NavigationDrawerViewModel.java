package org.pispeb.treff_client.view.home;

import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * Handles onClick events of the items in the drawer
 */

public class NavigationDrawerViewModel extends ViewModel {

    SingleLiveEvent<State> state;

    public NavigationDrawerViewModel() {
        state = new SingleLiveEvent<>();
    }

    public void onSettingsClick() {
        state.setValue(new State(ViewCall.DISPLAY_SETTINGS, 0));
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
