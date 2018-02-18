package org.pispeb.treff_server.commands.updates;

import java.util.Date;

public class UpdatesWithoutSpecialParameters extends UpdateToSerialize{

    public UpdatesWithoutSpecialParameters(Date date, int creator,
                                           UpdateType type) {
        super(type.toString(), date, creator);
    }
}
