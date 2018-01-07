package org.pispeb.treff_client.data.repositories;

import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import javax.inject.Singleton;

@Singleton
public class ChatRepository {
    private ChatDao chatDao;
    private RequestEncoder encoder;

}
