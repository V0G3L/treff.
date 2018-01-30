package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.networking.ErrorCode;

/**
 * @author tim
 */
public class ErrorOutput extends CommandOutput {

    public final ErrorCode error;

    public ErrorOutput(ErrorCode error) {
        this.error = error;
    }
}
