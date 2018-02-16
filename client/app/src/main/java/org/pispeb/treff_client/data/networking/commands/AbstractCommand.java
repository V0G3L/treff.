package org.pispeb.treff_client.data.networking.commands;

/**
 * Created by Lukas on 2/16/2018.
 */

public abstract class AbstractCommand {
    private final Class<? extends AbstractResponse> responseClass;

    protected AbstractCommand(
            Class<? extends AbstractResponse> responseClass) {
        this.responseClass = responseClass;
    }

    public abstract AbstractRequest getRequest();

    public abstract void onResponse(AbstractResponse response);

    public Class<? extends AbstractResponse> getResponceClass() {
        return responseClass;
    }
}
