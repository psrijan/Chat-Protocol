/**
 * -----ServerRequestModerator----
 * This class is where the request from client are first seen.
 * This takes in the request message validates the request states
 * and takes the message for further processing based on the command issued by the user
 * This class takes in the input stream of the server and processes messages incoming in the input stream.
 * 02/27/2020
 */
package com.srijan.pandey.server.core.moderator;

import com.srijan.pandey.common.exceptions.InvalidMessageException;
import com.srijan.pandey.common.exceptions.InvalidStateException;
import com.srijan.pandey.server.core.server.ConcurrentChatThread;
import com.srijan.pandey.server.dto.MessageValidationDTO;
import com.srijan.pandey.server.model.ModeRequest;
import com.srijan.pandey.server.model.ServerRequest;
import com.srijan.pandey.server.model.ServerRequestParseMessage;
import com.srijan.pandey.server.model.UserRegistrationRequest;
import com.srijan.pandey.server.state.ServerState;
import com.srijan.pandey.server.stubs.ServerDatabase;
import com.srijan.pandey.server.util.ServerMessageValidator;
import com.srijan.pandey.server.util.ServerRequestParser;
import com.srijan.pandey.server.util.ServerStateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.srijan.pandey.common.constants.MessageTypes.Codes;
import static com.srijan.pandey.common.constants.MessageTypes.Commands.*;

public class ServerRequestModerator implements Runnable {

    private ConcurrentChatThread parent; //Parent Thread that holds the response moderator
    private InputStream inputStream; // socket stream
    private DataInputStream dataInputStream; // wrapper that wraps the input stream
    private Logger log = LoggerFactory.getLogger(ServerResponseModerator.class); // Logger class
    private ServerDatabase serverDatabase; // Stub Database class that contains users
    private ServerState serverState; // State Class that holds the state of the Thread

    public ServerRequestModerator(InputStream inputStream, ServerState serverState, ConcurrentChatThread parent) {
        this.inputStream = inputStream;
        this.dataInputStream = new DataInputStream(inputStream);
        this.serverState = serverState;
        this.parent = parent;
    }

    public void setDatabase(ServerDatabase serverDatabase) {
        this.serverDatabase = serverDatabase;
    }

    @Override
    public void run() {
        log.debug("Client Request Moderator");
        receiveMessages();
    }

    /**
     * Message Validation DTO that is used for validation
     * of incoming message in ServerMessageValidator
     *
     * @return
     */
    private MessageValidationDTO messageValidationDTO() {
        MessageValidationDTO messageValidationDTO = new MessageValidationDTO();
        messageValidationDTO.setServerDatabase(serverDatabase);
        messageValidationDTO.setServerState(serverState);
        messageValidationDTO.setUsername(parent.getUsername());
        return messageValidationDTO;
    }


    /**
     * Receives Messages that are sent over from the client
     */
    private void receiveMessages() {
        log.debug("Receive Messages Start");
        String streamMessage = "";

        while (true) {
            try {
                streamMessage += dataInputStream.readUTF();
                log.debug("Message {}", streamMessage);
                ServerRequestParseMessage praseRequest = ServerRequestParser.parseMessage(streamMessage);
                streamMessage = praseRequest.getStreamMessage();
                ServerRequest request = praseRequest.getServerRequest();
                ServerMessageValidator.validateMessage(request, messageValidationDTO());
                ServerStateValidator.validateState(request, serverState); //STATEFUL
                processMessageDispatcher(request);
                Thread.sleep(1000);
            } catch (InvalidMessageException ex) {
                parent.sendResponse(Codes.GENRIC_ERROR, ex.getMessage());
            } catch (InvalidStateException ex) {
                parent.sendResponse(Codes.GENRIC_ERROR, ex.getMessage());
            } catch (Exception ex) {
                log.error("Error", ex);
                return;
            }
        }
    }

    /**
     * Message dispatches that requests further processing based on the message type
     *
     * @param request
     */
    public void processMessageDispatcher(ServerRequest request) {
        String command = request.getCommand();
        log.debug("Current Command {}", command);

        switch (command) {
            case USER:
                processUser(request);
                break;
            case PASS:
                processPass(request);
                break;
            case SUSER:
                processSuperUser(request);
                break;
            case SPASS:
                processSuperPass(request);
                break;
            case MODE:
                processMode(request);
                break;
            case CHAT:
                parent.processChat(request);
                break;
            case REGU:
                processUserRegistration(request);
                break;
            case LIST:
                processOptionList(request);
                break;
            case BYE:
                processBye(request);
                break;
            case VERSION:
                processVersion(request);
            default:
                log.debug("Not Supported Operation {}, {} ", request.getCommand(), request.getBody());
                break;
        }
    }

    public void processVersion(ServerRequest serverRequest) {
        log.debug("Inside Process Version");
        parent.processVersion();
    }


    public void processBye(ServerRequest serverRequest) {
        log.debug("Inside Process Bye ");
        parent.processBye(serverRequest.getBody());

    }

    /**
     * There are two types of option for chatting, group and user.
     * Based on the option type requested, the server sends responses
     * for available user and group
     *
     * @param iServerRequest
     */
    public void processOptionList(ServerRequest iServerRequest) {
        log.debug("Inside Process Option List ");
        List<String> dbUsers = serverDatabase.availableUsers();
        parent.processOptionList(iServerRequest.getBody(), dbUsers);
    }

    /**
     * Admin user can register for new user, this function processes new
     * user regiration by an admin user
     *
     * @param request
     */
    public void processUserRegistration(ServerRequest request) {
        log.debug("Inside User Registration");
        UserRegistrationRequest userRegistrationRequest = (UserRegistrationRequest) request;
        serverDatabase.addNewUsers(userRegistrationRequest.getUsername(), userRegistrationRequest.getPassword());
        parent.sendResponse(Codes.REG_USER_SUCCESS, String.format("Successfully Registered %s", userRegistrationRequest.getUsername()));
    }

    /**
     * Prcess Mode Request will select the mode for user. The modes is either user mode or group mode
     *
     * @param request
     */
    public void processMode(ServerRequest request) {
        log.debug("MODE command request");
        serverState.setMode(((ModeRequest) request).getMode());
        serverState.setModeSelection(true);
        parent.conversationCreator((ModeRequest) request);
    }

    /**
     * Process User Request handles the username authentication for the
     * user. It checks if the user is available in the server and sends authentication response.
     *
     * @param serverRequest
     */
    public void processUser(ServerRequest serverRequest) {
        log.debug("USER command request");
        boolean isUserAvailable = serverDatabase.userAvailable(serverRequest.getBody(), false);
        if (isUserAvailable) {
            serverState.setUsername(serverRequest.getBody());
            serverState.setUserAuth(true);
            String message = String.format("User %s authenticated", serverRequest.getBody());
            parent.userAuthenticatedResponse(message, serverState.getUsername());
        } else {
            String message = String.format("User %s not available", serverRequest.getBody());
            parent.sendResponse(Codes.USER_AUTH_FAIL, message);
        }
    }

    /**
     * Process Pass handles password authentication
     * It checks is the password matches the usernames password
     * stored in server and sends response
     *
     * @param serverRequest
     */
    public void processPass(ServerRequest serverRequest) {
        log.debug("PASS command request");
        boolean passValid = serverDatabase.passwordMatch(serverState.getUsername(), serverRequest.getBody(), false);
        if (passValid) {
            serverState.setPassAuth(true);
            parent.sendResponse(Codes.PASS_AUTH_SUCCESS, "Password Authenticated");
        } else {
            parent.sendResponse(Codes.PASS_AUTH_FAIL, "Password Invalid");
        }
    }

    /**
     * Super User username authentication. Checks if the user is available in the server
     *
     * @param serverRequest
     */
    public void processSuperUser(ServerRequest serverRequest) {
        log.debug("SUSER command request");
        boolean isUserAvailable = serverDatabase.userAvailable(serverRequest.getBody(), true);
        if (isUserAvailable) {
            serverState.setUsername(serverRequest.getBody());
            serverState.setUserAuth(true);
            serverState.setSuperUser(true);
            parent.sendResponse(Codes.USER_AUTH_SUCCESS, "Successfully Authenticated User");
        } else {
            parent.sendResponse(Codes.USER_AUTH_FAIL, "Super User Not Available");
        }

    }

    /**
     * Super user password authentication. Checks if the Super user password is valid
     * and sends authentication response
     *
     * @param iServerRequest
     */
    public void processSuperPass(ServerRequest iServerRequest) {
        log.debug("SPASS command request");
        boolean passwordValid = serverDatabase.passwordMatch(serverState.getUsername(), iServerRequest.getBody(), true);

        if (passwordValid) {
            serverState.setPassAuth(true);
            parent.sendResponse(Codes.PASS_AUTH_SUCCESS, "Password Authentication Success");
        } else {
            parent.sendResponse(Codes.PASS_AUTH_FAIL, "Invalid Password");
        }
    }

    /**
     * Closes the input streams
     * if BYE request is received
     */
    public void closeStreams() {
        log.debug("Closing Input Stream...");
        try {
            inputStream.close();
            dataInputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
