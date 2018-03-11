package org.pispeb.treffpunkt.client.data.networking.commands;

/**
 * Abstract Command each command inherits from
 * Every Command has to generate a request and be able to accept a response
 */

public abstract class AbstractCommand {
    private final Class<? extends AbstractResponse> responseClass;

    protected AbstractCommand(
            Class<? extends AbstractResponse> responseClass) {
        this.responseClass = responseClass;
    }

    public abstract AbstractRequest getRequest();

    public abstract void onResponse(AbstractResponse response);

    public Class<? extends AbstractResponse> getResponseClass() {
        return responseClass;
    }
}
