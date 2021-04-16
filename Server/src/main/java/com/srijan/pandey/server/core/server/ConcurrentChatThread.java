/**
 * ---ConcurrentChatThread-----
 * This is a Server Thread that handles individual chat client
 * Each request, response of a client is handled by ChatThread
 * It holds Server RequestModerator and Server ResponseModerator to moderate and
 * dispatch chat processes. It also has instance of Conversation Module
 * that it utilizes to dispatch chats between uses.
 * 02/28/2020
 */
package com.srijan.pandey.server.core.server;

import com.srijan.pandey.common.constants.CommonConstants;
import com.srijan.pandey.common.constants.MessageTypes;
import com.srijan.pandey.server.core.conversation.ConversationModule;
import com.srijan.pandey.server.core.moderator.ServerRequestModerator;
import com.srijan.pandey.server.core.moderator.ServerResponseModerator;
import com.srijan.pandey.server.dto.AddConversationDTO;
import com.srijan.pandey.server.model.ModeRequest;
import com.srijan.pandey.server.model.ServerRequest;
import com.srijan.pandey.server.state.ServerState;
import com.srijan.pandey.server.stubs.ServerDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConcurrentChatThread implements Runnable {

    private Socket sock; 
    private Logger logger = LoggerFactory.getLogger(ConcurrentChatThread.class);
    private ServerRequestModerator requestModerator;
    private ServerResponseModerator responseModerator;
    private String username = ""; //for which username is this ConcurrentChatServer class for
    private ConversationModule conversationModule;
    private ServerState serverState;
    private ServerDatabase serverDatabase;

    @Override
    public void run() {
        System.out.println("Running Chat Server");
    }

    public ConcurrentChatThread(Socket socket, ConversationModule conversationModule, ServerDatabase serverDatabase) {
        serverState = new ServerState();
        this.sock = socket;
        this.conversationModule = conversationModule;
        this.serverDatabase = serverDatabase;
        init();
    }

    public void setServerDatabase(ServerDatabase serverDatabase) {
        this.serverDatabase = serverDatabase;
    }

    private void init() {
        logger.debug("Initializing Concurrent Chat Server...");
        try {
            requestModerator = new ServerRequestModerator(sock.getInputStream(), serverState, this);
            responseModerator = new ServerResponseModerator(sock.getOutputStream(), serverState);
            requestModerator.setDatabase(serverDatabase);
            responseModerator.setDatabase(serverDatabase);
            Thread clientRequestThread = new Thread(requestModerator);
            clientRequestThread.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ServerRequestModerator getRequestModerator() {
        return requestModerator;
    }

    public ServerResponseModerator getResponseModerator() {
        return responseModerator;
    }

    public void invalidMessageResponse(String message) {
        responseModerator.sendInvalidMessage(message);
    }

    public void userAuthenticatedResponse(String message, String username) {
        this.username = username;
        responseModerator.sendUserAuthenticated(message);
    }

    public void passwordAuthenticatedResponse(String message) {
        responseModerator.sendPasswordAuthenticated(message);
    }

    public void sendResponse(int code, String message) {
        responseModerator.sendMessage(code, message);
    }

    public void conversationCreator(ModeRequest modeRequest) {
        logger.debug("Creating New Conversation for {} ", modeRequest.getName());
        try {
            String mode = modeRequest.getMode();
            String name = modeRequest.getName();
            AddConversationDTO addConversationDTO = new AddConversationDTO(mode, username, name, serverDatabase.getUserList());
            conversationModule.addConversation(addConversationDTO);
            sendResponse(MessageTypes.Codes.MODE_SEL_SUCCESS, "Successfully Connected");
        } catch (Exception ex) {
            responseModerator.sendMessage(MessageTypes.Codes.GENRIC_ERROR, ex.getMessage());
        }
    }

    public void processChat(ServerRequest serverRequest) {
        String chatMessage = serverRequest.getBody();
        boolean chatSendSuccess = conversationModule.relayChat(username, serverState.getMode(), chatMessage, username);

        if (!chatSendSuccess) {
            responseModerator.sendMessage(MessageTypes.Codes.GENRIC_ERROR, "Unable to Connect to other party");
        }
    }

    public String getUsername() {
        return username;
    }


    /**
     * Because each user created only 1 connection to the server, this limited the number of users each person 
     * could chat to, to 1. However, this doesn't mean that the server is not concurrent, the server can handle
     * multiple chat request from the client and mediate it. 
     * @param optionType
     * @param dbUsers
     */
    public void processOptionList(String optionType, List<String> dbUsers) {
        if (optionType.equals(CommonConstants.ModeSelectionConstants.GROUP)) {
            //Available Groups are constant and is hardcoded.
            List<String> availableGroups = serverDatabase.getAvailableGroups();
            responseModerator.sendOptionsList(availableGroups, optionType);
        } else {
            List<String> busyUsers = conversationModule.getBusyUsers();
            List<String> availableUser = new ArrayList<>();
            for (String user : dbUsers) {
                boolean available = true;
                for (String curBusyUser : busyUsers) {
                    if (user.equals(curBusyUser)) {
                        available = false;
                        break;
                    }
                }
                if (available) {
                    availableUser.add(user);
                }
            }
            responseModerator.sendOptionsList(availableUser, optionType);
        }
    }

    //@todo need to write logic
    public void processBye(String uname) {
        conversationModule.kickUser(username);
    }

    public void processVersion() {
        String version = serverDatabase.getVersion();
        responseModerator.sendMessage(MessageTypes.Codes.VERSION, version);
    }

    /**
     * Logic to close the thread
     */
    public void closeChatThread() {
        logger.debug("Closing Concurrent Server for username {} ", username);
        responseModerator.sendChat("Bye Bye!!" , "Server");
        responseModerator.sendMessage(MessageTypes.Codes.BYE , "hula hula"); // Send Bye Message
        requestModerator.closeStreams();
        responseModerator.closeStreams();

        try {
            sock.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

