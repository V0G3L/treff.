package org.pispeb.treff_server.exceptions;

/**
 * Thrown to indicate a {@link org.pispeb.treff_server.RequestHandler}
 * was instructed to rerun it's request.
 */
public class RequestHandlerAlreadyRan extends IllegalStateException {
}
