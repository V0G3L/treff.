package org.pispeb.treffpunkt.client.data.networking;

import org.pispeb.treffpunkt.client.data.networking.commands.AbstractCommand;

public class CommandJob {

    public final AbstractCommand command;
    public final CommandFailHandler failHandler;
    public final boolean abortOnCantConnect;

    public CommandJob(AbstractCommand command,
                      CommandFailHandler failHandler,
                      boolean abortOnCantConnect) {
        this.command = command;
        this.failHandler = failHandler;
        this.abortOnCantConnect = abortOnCantConnect;
    }
}
