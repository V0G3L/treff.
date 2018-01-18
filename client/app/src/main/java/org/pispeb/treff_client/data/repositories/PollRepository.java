package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;
import android.os.Handler;

import org.pispeb.treff_client.data.database.PollDao;
import org.pispeb.treff_client.data.entities.Poll;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;

public class PollRepository {
    private PollDao pollDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public PollRepository(PollDao pollDao, RequestEncoder encoder, Handler backgroundHandler) {
        this.pollDao = pollDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    public LiveData<List<Poll>> getPolls() {
        return pollDao.getAllPolls();
    }

    public LiveData<Poll> getPoll(int pollID) {
        return pollDao.getPollByID(pollID);
    }

    public void addPoll(Poll poll) {
        backgroundHandler.post(() -> {
            pollDao.save(poll);
        });
    }
}
