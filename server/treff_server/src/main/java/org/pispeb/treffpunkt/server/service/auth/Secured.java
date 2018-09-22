package org.pispeb.treffpunkt.server.service.auth;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a resource method or resource class is available only to authenticated users.
 * Requests made to such a resource are immediately rejected, without calling the resource method,
 * if no or invalid credentials are supplied.
 */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured { }
