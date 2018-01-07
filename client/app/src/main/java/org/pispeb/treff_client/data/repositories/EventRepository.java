package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import javax.inject.Singleton;

@Singleton
public class EventRepository {
    private EventDao eventDao;
    private RequestEncoder encoder;

}
