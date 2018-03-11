package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteAccount;


import org.pispeb.treffpunkt.client.data.repositories.UserRepository;

public class GetUserDetailsCommand extends AbstractCommand {

    private Request output;
    private final UserRepository userRepository;

    public GetUserDetailsCommand(int id, String token,
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
        userRepository.setUserName(output.id, response.account.username);
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.GET_USER_DETAILS.toString());
            this.id = id;
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final CompleteAccount account;

        public Response(@JsonProperty("account") CompleteAccount account) {
            this.account = account;
        }
    }
}
