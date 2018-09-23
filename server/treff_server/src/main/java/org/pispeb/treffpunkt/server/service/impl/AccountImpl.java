package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.commands.EditEmailCommand;
import org.pispeb.treffpunkt.server.commands.EditPasswordCommand;
import org.pispeb.treffpunkt.server.commands.EditUsernameCommand;
import org.pispeb.treffpunkt.server.commands.GetUserDetailsCommand;
import org.pispeb.treffpunkt.server.commands.GetUserIdCommand;
import org.pispeb.treffpunkt.server.commands.UpdatePositionCommand;
import org.pispeb.treffpunkt.server.service.api.AccountAPI;
import org.pispeb.treffpunkt.server.service.domain.Account;
import org.pispeb.treffpunkt.server.service.domain.Position;

import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.ws.rs.core.SecurityContext;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.AccountAPI")
@Path("/accounts")
public class AccountImpl extends ServiceImpl implements AccountAPI {

    @Override
    public Account getDetails(int aid, SecurityContext context) {
        return new GetUserDetailsCommand(sessionFactory)
                .execute(new GetUserDetailsCommand.Input(aid, token(context))).account;
    }

    @Override
    public int getID(String name, SecurityContext context) {
        return new GetUserIdCommand(sessionFactory)
                .execute(new GetUserIdCommand.Input(name, token(context))).id;
    }

    @Override
    public void setUsername(String username, String password, SecurityContext context) {
        new EditUsernameCommand(sessionFactory)
                .execute(new EditUsernameCommand.Input(username, password, token(context)));
    }

    @Override
    public void setEmail(String email, String password, SecurityContext context) {
        new EditEmailCommand(sessionFactory)
                .execute(new EditEmailCommand.Input(password, email, token(context)));
    }

    @Override
    public void setPassword(String oldPassword, String newPassword, SecurityContext context) {
        new EditPasswordCommand(sessionFactory)
                .execute(new EditPasswordCommand.Input(oldPassword, newPassword, token(context)));
    }

    @Override
    public void updatePosition(Position position, SecurityContext context) {
        new UpdatePositionCommand(sessionFactory)
                .execute(new UpdatePositionCommand.Input(position, token(context)));
    }
}
