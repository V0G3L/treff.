package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Created by matth on 16.02.2018.
 */

public class BlockAccountCommand extends AbstractCommand {

    private final UserRepository userRepository;
    private Request output;

    public BlockAccountCommand(int id, String token,
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
        userRepository.setIsBlocked(output.id, true);
        userRepository.setIsFriend(output.id, false);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.BLOCK_ACCOUNT.toString());
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
