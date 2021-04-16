
/**
 * ServerResponseModerator
 * The Class sends out the response to the client. While there is a conversation module that switches messages between client
 * the data sending part is handled by the ResponseModerator. The ServerResponseModerator takes in the output stream of the socket
 * and inserts messages into the output stream.
 * 02/28/2020
 */
package com.srijan.pandey.server.core.moderator;

import com.srijan.pandey.server.state.ServerState;
import com.srijan.pandey.server.stubs.ServerDatabase;
import com.srijan.pandey.server.util.ServerResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.srijan.pandey.common.constants.MessageTypes.Codes.*;


public class ServerResponseModerator {
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;
    private Logger log = LoggerFactory.getLogger(ServerResponseModerator.class);
    private ServerDatabase database;


    /**
     * ResponseModerator Constructor
     *
     * @param outputStream Socket output stream to send messages
     * @param serverState  Class containing state of the Current Thread
     */
    public ServerResponseModerator(OutputStream outputStream, ServerState serverState) {
        this.outputStream = outputStream;
        dataOutputStream = new DataOutputStream(outputStream);
    }

    /**
     * Setter class that sets the stub database
     *
     * @param database
     */
    public void setDatabase(ServerDatabase database) {
        this.database = database;
    }


    /**
     * Sending Invalid Messages to Client
     *
     * @param errorMessage
     */
    public void sendInvalidMessage(String errorMessage) {
        String message = ServerResponseBuilder.buildResponse(GENRIC_ERROR, errorMessage);
        sendMessage(message);
    }

    /**
     * Sends User Authentication Message to Client
     *
     * @param authMessage
     */
    public void sendUserAuthenticated(String authMessage) {
        String message = ServerResponseBuilder.buildResponse(USER_AUTH_SUCCESS, authMessage);
        sendMessage(message);
    }

    /**
     * Sends Password Authentication Message to Client
     *
     * @param passAuthMessage
     */
    public void sendPasswordAuthenticated(String passAuthMessage) {
        String message = ServerResponseBuilder.buildResponse(PASS_AUTH_SUCCESS, passAuthMessage);
        sendMessage(message);
    }


    /**
     * Dispatch Chat conversation to client
     *
     * @param chatMessage
     */
    public void sendChat(String chatMessage, String fromUsername) {
        List<String> message = ServerResponseBuilder.buildChatResponse(CHAT_RECEIVED, chatMessage, fromUsername);
        message.stream().forEach(s -> sendMessage(s));
    }

    /**
     * Sends the available Groups of Users to Client that it can connect to
     *
     * @param optionList Contains the list of optionList to client
     * @param type       is it sending group information or user information
     */
    public void sendOptionsList(List<String> optionList, String type) {
        log.debug("Sending Options List of type {}", type);
        List<String> messageList = optionList.stream().map(user -> ServerResponseBuilder.buildOptionResponse(OPTION_LIST, user, type)).collect(Collectors.toList());

        for (String curMessage : messageList) {
            sendMessage(curMessage);
        }
    }

    /**
     * Send Message function
     *
     * @param code response code
     * @param body body of the message
     */
    public void sendMessage(int code, String body) {
        String message = ServerResponseBuilder.buildResponse(code, body);
        sendMessage(message);
    }


    /**
     * Send Message function that takes in a final mesasge.
     *
     * @param message
     */
    private void sendMessage(String message) {
        log.debug("Sending Message To Client {}", message);
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException ex) {
            log.error("Error: {}" , ex);
        }
    }

    /**
     * Function that closes the streams available in the ResponseModerator
     * This will be issued if a chat is being closed
     */
    public void closeStreams() {
        log.debug("Output Stream Closing...");
        try {
            dataOutputStream.close();
            outputStream.close();
        } catch (Exception ex) {
            log.debug("Error: {} ", ex);
        }
    }


}
