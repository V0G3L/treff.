package org.pispeb.treffpunkt.client.data.networking;

public class APIException extends RuntimeException {

    private final Error error;

    public APIException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
