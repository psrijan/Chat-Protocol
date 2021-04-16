package com.srijan.pandey.server.util;

import com.srijan.pandey.common.constants.MessageTypes;
import com.srijan.pandey.common.exceptions.InvalidMessageException;
import com.srijan.pandey.server.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.srijan.pandey.common.constants.CommonConstants.MessageDelimiters.*;

/**
 * Parses the Message Stream into its corresponding message parts.
 */
public class ServerRequestParser {

    private static final String PATTERN = "<.+>";

    public static ServerRequestParseMessage parseMessage(String streamString) {

        Pattern p = Pattern.compile(PATTERN);   // the pattern to search for
        Matcher m = p.matcher(streamString);

        int startIndex = 0;
        int endIndex = 0;

        // if we find a match, get the group
        if (m.find()) {
            // we're only looking for one group, so get it
            startIndex = m.start();
            endIndex = m.end();

            // print the group out for verification
//            System.out.format("%d %d", startIndex, endIndex);
        }

        int end = m.regionEnd();

        String curMessage = streamString.substring(startIndex, endIndex);
        String remainingData = streamString.substring(endIndex, end);

//        System.out.println("\n PAYLOAD: " + curMessage);
//        System.out.println("REMAINING: " + remainingData);

        ServerRequest serverRequest = parse(curMessage);
        serverRequest.setRawMessage(curMessage);

       return new ServerRequestParseMessage(serverRequest , remainingData);
    }

    /**
     * Parse the message into its individual parts
     * @param message
     * @return
     */
    private static ServerRequest parse(String message) {
        String[] refinedMessageList = message
                .replace(MESSAGE_START, "")
                .replace(MESSAGE_END, "")
                .split("\\" + MESSAGE_SPLIT);

        if (refinedMessageList.length < 2)
            throw new InvalidMessageException("The message received is invalid");

        ServerRequest request;

        switch (refinedMessageList[0]) {
            case MessageTypes.Commands.MODE:
                request = parseModeMessage(message, refinedMessageList);
                break;
            case MessageTypes.Commands.REGU:
                request = parseUserRegistrationMessage(message, refinedMessageList);
                break;
            default:
                request = new GenericServerRequest(refinedMessageList[0], refinedMessageList[1]);
        }

        return request;
    }

    private static ServerRequest parseUserRegistrationMessage(String message, String[] refinedMessageList) {
        if (refinedMessageList.length < 3)
            throw new InvalidMessageException("Mdde Request Message Invalid");
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(refinedMessageList[0], refinedMessageList[1], refinedMessageList[2]);
        return registrationRequest;
    }

    private static ServerRequest parseModeMessage(String message, String[] parts) {
        if (parts.length < 3)
            throw new InvalidMessageException("Mode Request Message Invalid");

        ServerRequest modeRequest = new ModeRequest(parts[0], parts[1], parts[2]);
        return modeRequest;
    }


}
