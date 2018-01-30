package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.networking.ErrorCode;

/**
 * @author tim
 */
public class ErrorOutput extends CommandOutput {

    private final ErrorCode error;

    public ErrorOutput(ErrorCode error) {
        this.error = error;
    }

    public int getError() {
        return error.getCode();
    }
}
