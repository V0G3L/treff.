package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.commands.AcceptContactRequestCommand;
import org.pispeb.treffpunkt.server.commands.BlockAccountCommand;
import org.pispeb.treffpunkt.server.commands.CancelContactRequestCommand;
import org.pispeb.treffpunkt.server.commands.GetContactListCommand;
import org.pispeb.treffpunkt.server.commands.RejectContactRequestCommand;
import org.pispeb.treffpunkt.server.commands.RemoveContactCommand;
import org.pispeb.treffpunkt.server.commands.SendContactRequestCommand;
import org.pispeb.treffpunkt.server.commands.UnblockAccountCommand;
import org.pispeb.treffpunkt.server.service.api.ContactsAPI;
import org.pispeb.treffpunkt.server.service.domain.ContactList;

import javax.jws.WebService;
import javax.ws.rs.core.SecurityContext;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.ContactsAPI")
public class ContactsImpl extends ServiceImpl implements ContactsAPI {

    @Override
    public ContactList getContactList(SecurityContext context) {
        return new GetContactListCommand(sessionFactory)
                .execute(new GetContactListCommand.Input(token(context))).contactList;
    }

    @Override
    public void sendRequest(int aid, SecurityContext context) {
        new SendContactRequestCommand(sessionFactory)
                .execute(new SendContactRequestCommand.Input(aid, token(context)));
    }

    @Override
    public void removeContact(int aid, SecurityContext context) {
        new RemoveContactCommand(sessionFactory)
                .execute(new RemoveContactCommand.Input(aid, token(context)));
    }

    @Override
    public void acceptRequest(int aid, SecurityContext context) {
        new AcceptContactRequestCommand(sessionFactory)
                .execute(new AcceptContactRequestCommand.Input(aid, token(context)));
    }

    @Override
    public void rejectRequest(int aid, SecurityContext context) {
        new RejectContactRequestCommand(sessionFactory)
                .execute(new RejectContactRequestCommand.Input(aid, token(context)));
    }

    @Override
    public void cancelRequest(int aid, SecurityContext context) {
        new CancelContactRequestCommand(sessionFactory)
                .execute(new CancelContactRequestCommand.Input(aid, token(context)));
    }

    @Override
    public void block(int aid, SecurityContext context) {
        new BlockAccountCommand(sessionFactory)
                .execute(new BlockAccountCommand.Input(aid, token(context)));
    }

    @Override
    public void unblock(int aid, SecurityContext context) {
        new UnblockAccountCommand(sessionFactory)
                .execute(new UnblockAccountCommand.Input(aid, token(context)));
    }
}
