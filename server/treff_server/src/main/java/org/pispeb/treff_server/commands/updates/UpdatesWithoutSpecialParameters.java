package org.pispeb.treff_server.commands.updates;

import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class UpdatesWithoutSpecialParameters extends UpdateToSerialize{

    public UpdatesWithoutSpecialParameters(Date date, int creator,
                                           Update.UpdateType type) {
        super(type.toString(), date, creator);
    }
}
