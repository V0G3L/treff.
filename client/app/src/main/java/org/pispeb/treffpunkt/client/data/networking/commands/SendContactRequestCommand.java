package org.pispeb.treffpunkt.client.data.networking.commands;

import org.pispeb.treffpunkt.client.data.repositories.UserRepository;

/**
 * Class representing a JSON send-contact-request object
 */

public class SendContactRequestCommand extends AbstractCommand{

    private Request output;
    private final UserRepository userRepository;

    public SendContactRequestCommand(int id, String token, UserRepository userRepository) {
        super(Response.class);
        this.userRepository = userRepository;
        output = new Request(id, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userRepository.setIsPending(output.id, true);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.SEND_CONTACT_REQUEST.toString());
            this.id = id;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }

}
