package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Class representing a JSON cancel-contact-request object
 */

public class CancelContactRequestCommand extends AbstractCommand{

    private final UserRepository userRepository;
    private Request output;

    public CancelContactRequestCommand(int id, String token,
                                       UserRepository userRepository) {
        super(Response.class);
        output = new Request(id, token);
        this.userRepository = userRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        userRepository.setIsPending(output.id, false);
        userRepository.setIsFriend(output.id, false);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.CANCEL_CONTACT_REQUEST.toString());
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
