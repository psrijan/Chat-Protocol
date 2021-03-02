/**
 * ----Request Mediator
 * Handles All Client Outgoing Messages 
 * This class deals with creating messages
 * and sending them through to the server.
 * 03/1/2020
 */
package com.srijan.pandey.client.core;

import com.srijan.pandey.client.model.request.*;
import com.srijan.pandey.client.state.ClientState;
import com.srijan.pandey.client.util.ClientRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class RequestMediator {

    private Logger logger = LoggerFactory.getLogger(RequestMediator.class);
    private DataOutputStream outputStream = null; // Stream that sends messages to the server
    private ClientState clientState; // Client central state

    public RequestMediator(OutputStream os , ClientState clientState) {
        this.outputStream = new DataOutputStream(os);
        this.clientState = clientState;
    }

    /**
     * Sends authentication request message
     * @param loginModel
     * @param isSuper
     */
    public void authenticateUser(LoginRequest loginModel , boolean isSuper) {
        logger.debug("Authenticating Username");
        String userMessage = ClientRequestBuilder.userMessage(loginModel.getUsername() , isSuper);
        sendMessage(userMessage);
    }

    /**
     * Send password verification request to the server.
     * @param loginRequest
     * @param isSuper - isSuper
     */
    public void authenticatePassword(LoginRequest loginRequest , boolean isSuper) {
        logger.debug("Authenticating Password");
        String passMessage = ClientRequestBuilder.passMessage(loginRequest.getPassword() , isSuper);
        sendMessage(passMessage);
    }

    /**
     * Request the server to initiate
     * the chat with a particular client.
     * @param chatModel
     */
    public void initiateChat(ChatRequest chatModel) {
        logger.debug("Inside Request Mediator - processChat {} " , chatModel.toString());
        String chat = chatModel.getChatText();
        List<String> messages = ClientRequestBuilder.chatMessage(chat);

        for (String msg : messages) {
            sendMessage(msg);
        }
    }

    /**
     * Select Mode USER or GROUP
     * @param modeSelectRequest
     */
    public void modeSelect(ModeSelectRequest modeSelectRequest) {
        logger.debug("Mode Select ");
        String modeMessage = ClientRequestBuilder.modeMessage(modeSelectRequest.getType() , modeSelectRequest.getConnectTo());
        sendMessage(modeMessage);
    }

    /**
     * Choose the number of available option in each list
     * @param groupClientRequest
     */
    public void listOptions(GroupClientRequest groupClientRequest) {
        logger.debug("Inside RequestMediator - processOptions {}" , groupClientRequest.toString());
        String clientMessage = ClientRequestBuilder.optionMessage(groupClientRequest.getOption());
        sendMessage(clientMessage);
    }

    /**
     * See which version the server is
     */
    public void versionInfo() {
        String message = ClientRequestBuilder.versionMessage();
        sendMessage(message);
    }

    /**
     * Create new user request send by the super user
     * comes here
     * @param userRequest
     */
    public void addNewUser(AddUserRequest userRequest) {
        logger.debug("Add User Request");
        String message = ClientRequestBuilder.addUserRequest(userRequest.getUsername() , userRequest.getPassword());
        sendMessage(message);
    }

    /**
     *  Base function which sends the message which sends the message to the server
     * @param message
     */
    public void sendMessage(String message) {
        logger.debug("Message: {}", message.toString());
        try {
            outputStream.writeUTF(message);
        } catch (IOException ex) {
            logger.error("error: {}" ,ex );
        }
    }
}
