package org.pispeb.treff_client.data.networking.commands;


import java.util.Date;

/**
 * Request an array of all updates that accumulated on the server since the
 * last time this was requested
 */

public class RequestUpdatesCommand extends AbstractCommand {

    public RequestUpdatesCommand() {
        super(Response.class);
    }

    @Override
    public AbstractRequest getRequest() {
        return null;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
    }

    // sends empty command
    public static class Request extends AbstractRequest {
        public Request() {
            super(CmdDesc.REQUEST_UPDATES.toString());
        }
    }

    public static class Response extends AbstractResponse {
        // TODO updates!
        public Response() {
        }
    }


    private static class Update {
        public final String type;
        public final Date created;
        public final int creator;

        public Update(String type, Date created, int creator) {
            this.type = type;
            this.created = created;
            this.creator = creator;
        }
    }
}
