package org.pispeb.treff_client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.view.util.TreffPunkt;

import java.util.Date;

/**
 * Created by matth on 16.02.2018.
 */

public class SendChatMessageCommand extends AbstractCommand {

    private ChatRepository chatRepository;
    private Request output;

    public SendChatMessageCommand(int groupId, String message, String token,
                                  ChatRepository chatRepository) {
        super(Response.class);
        output = new Request(groupId, message, token);
        this.chatRepository = chatRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

        // TODO test
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int creator = pref.getInt(ctx.getString(R.string.key_userId), -1);
        String username = pref.getString(ctx.getString(R.string.key_userName)
                , "");

        chatRepository.addMessage(
                new ChatMessage(output.groupId, output.message,
                        creator, username, new Date()));
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("group-id")
        public final int groupId;
        public final String message;
        public final String token;

        public Request(int groupId, String message, String token) {
            super(CmdDesc.SEND_CHAT_MESSAGE.toString());
            this.groupId = groupId;
            this.message = message;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
