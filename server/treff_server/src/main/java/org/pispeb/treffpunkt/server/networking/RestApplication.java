package org.pispeb.treffpunkt.server.networking;

import org.pispeb.treffpunkt.server.service.impl.AuthImpl;
import org.pispeb.treffpunkt.server.service.impl.ServiceImpl;
import org.pispeb.treffpunkt.server.service.impl.UsergroupImpl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("/rest")
public class RestApplication extends Application {

    private Set<Object> singletons = new HashSet<>();

    public RestApplication() {
        ServiceImpl[] serviceImpls = {
                new AuthImpl(),
                new UsergroupImpl()
        };
        for (ServiceImpl impl : serviceImpls) {
            singletons.add(impl);
        }
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return super.getClasses();
    }

    @Override
    public Map<String, Object> getProperties() {
        return super.getProperties();
    }
}

