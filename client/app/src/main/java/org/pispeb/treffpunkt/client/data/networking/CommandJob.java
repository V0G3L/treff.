package org.pispeb.treffpunkt.client.data.networking;

public class CommandJob {

    public final Runnable task;
    public final CommandFailHandler failHandler;

    public CommandJob(Runnable task) {
        this.task = task;
        this.failHandler = null;
    }

    public CommandJob(Runnable task, CommandFailHandler failHandler) {
        this.task = task;
        this.failHandler = failHandler;
    }
}
