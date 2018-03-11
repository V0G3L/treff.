package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class UpdatePositionCommand extends AbstractCommand {
    private Request output;

    public UpdatePositionCommand(double latitude, double longitude, Date time,
                                 String token) {
        super(Response.class);
        output = new Request(latitude, longitude, time, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // Do nothing
    }

    public static class Request extends AbstractRequest {

        public final double latitude;
        public final double longitude;
        @JsonProperty("time-measured")
        public final Date timeMeasured;
        public final String token;

        public Request(double latitude, double longitude, Date timeMeasured,
                       String token) {
            super(CmdDesc.UPDATE_POSITION.toString());
            this.latitude = latitude;
            this.longitude = longitude;
            this.timeMeasured = timeMeasured;
            this.token = token;
        }
    }


    //Server returns empty object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
