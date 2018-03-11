package org.pispeb.treffpunkt.client.data.networking.commands;

/**
 * Abstract Request which can be serialized
 */

public abstract class AbstractRequest {
    public final String cmd;

    protected AbstractRequest(String cmd) {
        this.cmd = cmd;
    }
}
