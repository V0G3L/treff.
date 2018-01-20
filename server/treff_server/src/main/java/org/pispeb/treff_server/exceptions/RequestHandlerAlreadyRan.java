package org.pispeb.treff_server.exceptions;

import org.pispeb.treff_server.networking.RequestHandler;

/**
 * Thrown to indicate a {@link RequestHandler}
 * was instructed to rerun it's request.
 */
public class RequestHandlerAlreadyRan extends IllegalStateException {
}
