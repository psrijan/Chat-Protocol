/**
 * --- ServerMessageValidator----
 * Message validator that validates the size and the command type
 * of the message. The functions are self explainatory
 * 03/12/2020
 */
package com.srijan.pandey.server.util;

import com.srijan.pandey.common.constants.CommonConstants;
import com.srijan.pandey.common.constants.MessageTypes;
import com.srijan.pandey.common.exceptions.InvalidMessageException;
import com.srijan.pandey.server.dto.MessageValidationDTO;
import com.srijan.pandey.server.model.ModeRequest;
import com.srijan.pandey.server.model.ServerRequest;
import com.srijan.pandey.server.model.UserRegistrationRequest;

import java.util.ArrayList;
import java.util.List;

import static com.srijan.pandey.common.constants.MessageTypes.Commands.*;
import static com.srijan.pandey.common.constants.MessageTypes.Commands.VERSION;


public class ServerMessageValidator {

    public static void validateMessage(ServerRequest serverRequest, MessageValidationDTO messageValidationDTO) {
        validateSize(serverRequest.getRawMessage());
        validateCommand(serverRequest.getCommand());

        switch (serverRequest.getCommand()) {
            case MessageTypes.Commands.USER:
                validateUsername(serverRequest.getBody(), messageValidationDTO.getServerDatabase().getUserList());
                validateUsernameLength(serverRequest.getBody());
                break;
            case MessageTypes.Commands.SUSER:
                validateUsername(serverRequest.getBody(), messageValidationDTO.getServerDatabase().getSuperUserList());
                break;
            case MessageTypes.Commands.REGU:
                validateUsernameLength(((UserRegistrationRequest)serverRequest).getUsername());
                break;
            case MessageTypes.Commands.LIST:
                validateOptionType(serverRequest.getBody());
                break;
            case MessageTypes.Commands.MODE:
                validateModeSelect((ModeRequest) serverRequest, messageValidationDTO);
                break;
            case PASS:
            case SPASS:
            case CHAT:
            case BYE:
            case VERSION:
                break;
            default:
                throw new InvalidMessageException("Command Not Valid");
        }
    }

    public static void validateModeSelect(ModeRequest modeRequest, MessageValidationDTO messageValidationDTO) {
        validateOptionType(modeRequest.getMode());

        if (modeRequest.getMode().equals(CommonConstants.ModeSelectionConstants.USER)) {
            String username = modeRequest.getName();
            validateUsername(username, messageValidationDTO.getServerDatabase().getUserList());
        } else {
            String groupName = modeRequest.getName();
            validateGroupName(groupName, messageValidationDTO.getServerDatabase().getAvailableGroups());
        }

    }

    public static void validateGroupName(String username, List<String> availableGroups) {
        boolean isValild = availableGroups.stream().filter(s -> s.equals(username)).count() >= 1;
        if (!isValild)
            throw new InvalidMessageException(String.format("There is no user with group %s", username));
    }

    public static void validateOptionType(String option) {
        boolean optionSizeValid = option.length() == 1;
        boolean optionValid = option.equals(CommonConstants.ModeSelectionConstants.GROUP) || option.equals(CommonConstants.ModeSelectionConstants.USER);
        if (!optionSizeValid)
            throw new InvalidMessageException("Option Size Invalid");
        if (!optionValid)
            throw new InvalidMessageException("Option Type Invalid");
    }

    public static void validateUsernameLength(String username) {
        boolean isValid = username.length() < 10;
        if (!isValid)
            throw new InvalidMessageException("The length of the username is too large");
    }

    public static void validateUsername(String username, List<String> availableUsers) {
        boolean isValild = availableUsers.stream().filter(s -> s.equals(username)).count() >= 1;
        if (!isValild)
            throw new InvalidMessageException(String.format("There is no user with username %s", username));
    }

    private static void validateSize(String rawMessage) {
        if (rawMessage.length() > CommonConstants.MessageSize.REQUEST_MESSAGE_SIZE) {
            throw new InvalidMessageException("Size of Message too large");
        }

    }

    private static void validateCommand(String command) {
        List<String> allCommands = new ArrayList<>();
        allCommands.add(MessageTypes.Commands.MODE);
        allCommands.add(MessageTypes.Commands.BYE);
        allCommands.add(MessageTypes.Commands.REGU);
        allCommands.add(MessageTypes.Commands.CHAT);
        allCommands.add(MessageTypes.Commands.LIST);
        allCommands.add(MessageTypes.Commands.SPASS);
        allCommands.add(MessageTypes.Commands.SUSER);
        allCommands.add(MessageTypes.Commands.VERSION);
        allCommands.add(MessageTypes.Commands.PASS);
        allCommands.add(MessageTypes.Commands.USER);

        if (allCommands.stream().filter(s -> s.equals(command)).count() == 0)
            throw new InvalidMessageException("Command Not avialable");
    }


}
