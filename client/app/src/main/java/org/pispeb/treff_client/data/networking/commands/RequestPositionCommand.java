package org.pispeb.treff_client.data.networking.commands;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class RequestPositionCommand extends AbstractCommand {

    private Request output;

    public RequestPositionCommand(int groupId, Date time, String token) {
        super(Response.class);
        output = new Request(groupId, time, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final Date time;
        public final String token;

        public Request(int id, Date time, String token) {
            super(CmdDesc.REQUEST_POSITION.toString());
            this.id = id;
            this.time = time;
            this.token = token;
        }
    }


    //Server returns empty object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
