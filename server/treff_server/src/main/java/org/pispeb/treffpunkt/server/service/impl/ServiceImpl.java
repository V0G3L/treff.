package org.pispeb.treffpunkt.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Server;

import javax.ws.rs.core.SecurityContext;

public abstract class ServiceImpl {

    private final ObjectMapper mapper = Server.getMapper();

    protected SessionFactory sessionFactory;

    ServiceImpl() {
        this.sessionFactory = Server.getInstance().getSessionFactory();
    }

    String token(SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

}
