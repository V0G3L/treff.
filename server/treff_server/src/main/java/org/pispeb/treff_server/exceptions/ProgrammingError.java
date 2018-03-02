package org.pispeb.treff_server.exceptions;

// TODO: convert to Exception and catch on top-level
// return internal server error message

/**
 * @author tim
 */
public class ProgrammingError extends Error {

    @Override
    public String getMessage() {
        return "A severe error occurred that was caused by a programming " +
                "error and can't be fixed during runtime. " +
                "Please drop us a \"lol doesn't work\"-one-liner on GitHub " +
                "accompanied by a motion blurred photograph of your screen.";
    }
}
