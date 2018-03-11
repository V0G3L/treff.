package org.pispeb.treffpunkt.server.commands.io;

import org.pispeb.treffpunkt.server.networking.ErrorCode;

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
