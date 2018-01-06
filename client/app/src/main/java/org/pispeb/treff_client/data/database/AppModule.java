package org.pispeb.treff_client.data.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    //TODO remove context/db references

    @Provides
    @Singleton
    TreffDatabase provideTreffDatabase(Context context) {
        return Room.databaseBuilder(context, TreffDatabase.class, "user-db").fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    UserDao provideUserDao(TreffDatabase treffDatabase) {
        //TODO
        return null;
    }

}
