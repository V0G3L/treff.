package org.pispeb.treffpunkt.client.view.util;

/**
 * A {@link android.arch.lifecycle.ViewModel}s state, represented by a {@link ViewCall}
 * and an optional parameter
 */
public class State {
    public final ViewCall call;
    public final int value;

    public State(ViewCall call, int value) {
        this.call = call;
        this.value = value;
    }
}
