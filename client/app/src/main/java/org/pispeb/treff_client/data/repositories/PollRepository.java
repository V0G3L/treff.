package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.PollDao;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import javax.inject.Singleton;

@Singleton
public class PollRepository {
    private PollDao pollDao;
    private RequestEncoder encoder;

}
