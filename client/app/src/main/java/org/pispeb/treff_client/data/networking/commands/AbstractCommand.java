package org.pispeb.treff_client.data.networking.commands;

/**
 * Created by Lukas on 2/16/2018.
 */

public abstract class AbstractCommand {
    public abstract String getJson();

    public abstract void onAnswer(String answer);
}
