package org.pispeb.treff_server;

import org.pispeb.treff_server.commands.AbstractCommand;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.RequestHandler;
import org.pispeb.treff_server.sql.SQLDatabase;
import org.pispeb.treff_server.commands.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

public class Server {

    private static final String CONFIG_DEFAULT_FILE_PATH
            = Server.class.getClassLoader().getResource("config_default" +
            ".properties").getFile();
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static Server instance = null;

    private AccountManager accountManager;
    private RequestHandler requestHandler;

    public static Server getInstance() {
        if (instance == null)
            instance = new Server();

        return instance;
    }

    private Server() {
        // load default config
        Properties defaultConfig = new Properties();
        try (FileInputStream defaultConfigIn = new FileInputStream
                (CONFIG_DEFAULT_FILE_PATH)) {
            defaultConfig.load(defaultConfigIn);
        } catch (IOException e) {
            // TODO: error message
            e.printStackTrace();
        }

        // load actual config, using default config as fallback
        Properties config = new Properties(defaultConfig);
        if (new File(CONFIG_FILE_PATH).exists()) {
            try (FileInputStream configIn = new FileInputStream
                    (CONFIG_DEFAULT_FILE_PATH)) {
                config.load(configIn);
            } catch (IOException e) {
                // TODO: error message
                e.printStackTrace();
            }
        }

        SQLDatabase sqlDatabase = null;
        try {
            sqlDatabase = new SQLDatabase(config);
        } catch (SQLException | NoSuchAlgorithmException e) {
            // TODO: error message and exit
            e.printStackTrace();
        }

        accountManager = sqlDatabase.getEntityManagerSQL();
        requestHandler = new RequestHandler(accountManager);
        registerAllCommands();
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    private void registerAllCommands() {
        // all hail strings replacements
        // and screw tomcat for not executing our static blocks
        AbstractCommand.registerCommand("accept-contact-request", AcceptContactRequestCommand.class);
        AbstractCommand.registerCommand("add-group-members", AddGroupMembersCommand.class);
        AbstractCommand.registerCommand("add-poll-option", AddPollOptionCommand.class);
        AbstractCommand.registerCommand("block-account", BlockAccountCommand.class);
        AbstractCommand.registerCommand("cancel-contact-request", CancelContactRequestCommand.class);
        AbstractCommand.registerCommand("create-event", CreateEventCommand.class);
        AbstractCommand.registerCommand("create-group", CreateGroupCommand.class);
        AbstractCommand.registerCommand("create-poll", CreatePollCommand.class);
        AbstractCommand.registerCommand("delete-account", DeleteAccountCommand.class);
        AbstractCommand.registerCommand("edit-email", EditEmailCommand.class);
        AbstractCommand.registerCommand("edit-event", EditEventCommand.class);
        AbstractCommand.registerCommand("edit-group", EditGroupCommand.class);
        AbstractCommand.registerCommand("edit-membership", EditMembershipCommand.class);
        AbstractCommand.registerCommand("edit-password", EditPasswordCommand.class);
        AbstractCommand.registerCommand("edit-poll", EditPollCommand.class);
        AbstractCommand.registerCommand("edit-poll-option", EditPollOptionCommand.class);
        AbstractCommand.registerCommand("edit-username", EditUsernameCommand.class);
        AbstractCommand.registerCommand("get-contact-list", GetContactListCommand.class);
        AbstractCommand.registerCommand("get-event-details", GetEventDetailsCommand.class);
        AbstractCommand.registerCommand("get-group-details", GetGroupDetailsCommand.class);
        AbstractCommand.registerCommand("get-membership-details", GetMembershipDetailsCommand.class);
        AbstractCommand.registerCommand("get-poll-details", GetPollDetailsCommand.class);
        AbstractCommand.registerCommand("get-user-details", GetUserDetailsCommand.class);
        AbstractCommand.registerCommand("get-user-id", GetUserIdCommand.class);
        AbstractCommand.registerCommand("join-event", JoinEventCommand.class);
        AbstractCommand.registerCommand("leave-event", LeaveEventCommand.class);
        AbstractCommand.registerCommand("list-groups", ListGroupsCommand.class);
        AbstractCommand.registerCommand("login", LoginCommand.class);
        AbstractCommand.registerCommand("publish-position", PublishPositionCommand.class);
        AbstractCommand.registerCommand("register", RegisterCommand.class);
        AbstractCommand.registerCommand("reject-contact-request", RejectContactRequestCommand.class);
        AbstractCommand.registerCommand("remove-contact", RemoveContactCommand.class);
        AbstractCommand.registerCommand("remove-event", RemoveEventCommand.class);
        AbstractCommand.registerCommand("remove-group-members", RemoveGroupMembersCommand.class);
        AbstractCommand.registerCommand("remove-poll", RemovePollCommand.class);
        AbstractCommand.registerCommand("remove-poll-option", RemovePollOptionCommand.class);
        AbstractCommand.registerCommand("request-position", RequestPositionCommand.class);
        AbstractCommand.registerCommand("request-updates", RequestUpdatesCommand.class);
        AbstractCommand.registerCommand("reset-password", RegisterCommand.class);
        AbstractCommand.registerCommand("reset-password-confirm", ResetPasswordConfirmCommand.class);
        AbstractCommand.registerCommand("send-chat-message", SendChatMessageCommand.class);
        AbstractCommand.registerCommand("send-contact-request", SendContactRequestCommand.class);
        AbstractCommand.registerCommand("unblock-account", UnblockAccountCommand.class);
        AbstractCommand.registerCommand("update-position", UpdatePositionCommand.class);
        AbstractCommand.registerCommand("vote-for-option", VoteForOptionCommand.class);
        AbstractCommand.registerCommand("withdraw-vote-for-option", WithdrawVoteForOptionCommand.class);
    }

}
