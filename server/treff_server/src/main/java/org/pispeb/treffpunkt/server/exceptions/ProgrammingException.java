package org.pispeb.treffpunkt.server.exceptions;

// TODO: catch on top-level
// return internal server error message

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author tim
 */
public class ProgrammingException extends WebApplicationException {

    private static final String standardText =
            "A severe error occurred that was caused by a programming " +
                    "error and can't be fixed during runtime.\n" +
                    "Please drop us a \"lol doesn't work\"-one-liner on " +
                    "GitHub accompanied by a motion blurred photograph of " +
                    "your screen.";

    private final String message;

    public ProgrammingException(String message) {
        this.message = message;
    }

    public ProgrammingException(Exception e) {
        this.message = standardText + "\n" +
                "The original exception contained the following message:\n" +
                e.getMessage();
    }

    public ProgrammingException() {
        this.message = standardText;

    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Response getResponse() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
