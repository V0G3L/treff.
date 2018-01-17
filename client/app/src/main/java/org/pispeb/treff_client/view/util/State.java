package org.pispeb.treff_client.view.util;

/**
 * Created by Lukas on 1/17/2018.
 */

public class State {
    public final ViewCall call;
    public final int value;

    public State(ViewCall call, int value) {
        this.call = call;
        this.value = value;
    }
}
