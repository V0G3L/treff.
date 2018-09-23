package org.pispeb.treffpunkt.server.networking;

import org.pispeb.treffpunkt.server.service.auth.AuthenticationFilter;
import org.pispeb.treffpunkt.server.service.impl.AccountImpl;
import org.pispeb.treffpunkt.server.service.impl.AuthImpl;
import org.pispeb.treffpunkt.server.service.impl.ContactsImpl;
import org.pispeb.treffpunkt.server.service.impl.ServiceImpl;
import org.pispeb.treffpunkt.server.service.impl.UpdateImpl;
import org.pispeb.treffpunkt.server.service.impl.UsergroupImpl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("/rest")
public class RestApplication extends Application {

    private Set<Object> singletons = new HashSet<>();

    public RestApplication() {
        ServiceImpl[] serviceImpls = {
                new AccountImpl(),
                new AuthImpl(),
                new ContactsImpl(),
                new UpdateImpl(),
                new UsergroupImpl()
        };
        singletons.addAll(Arrays.asList(serviceImpls));

        singletons.add(new AuthenticationFilter());
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

