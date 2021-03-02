/**
 * ----- ClientResponseParser -----
 * Parses the Response sent out by the server
 * and provides an output. Checks that the
 * message is of pattern accepted by the client
 * 03/1/2020
 */
package com.srijan.pandey.client.util;

import com.srijan.pandey.client.model.response.AbstractClientResponse;
import com.srijan.pandey.client.model.response.ClientResponseParseMessage;
import com.srijan.pandey.client.model.response.GenericClientResponse;
import com.srijan.pandey.client.model.response.OptionMessage;
import com.srijan.pandey.common.constants.MessageTypes;
import com.srijan.pandey.common.exceptions.InvalidMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.srijan.pandey.common.constants.CommonConstants.MessageDelimiters.*;

public class ClientResponseParser {

    private static Logger logger = LoggerFactory.getLogger(ClientResponseParser.class);
    private static final String PATTERN = "<.+>";

    public static ClientResponseParseMessage parseResponse(String streamString) throws InvalidMessageException {
        logger.debug("Parse Response...");

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
            logger.debug("%d %d", startIndex, endIndex);
        }

        int end = m.regionEnd();

        String curMessage = streamString.substring(startIndex, endIndex);
        String remainingData = streamString.substring(endIndex, end);

        logger.debug("\n PAYLOAD: " + curMessage);
        logger.debug("REMAINING: " + remainingData);

        AbstractClientResponse clientResponse = parse(curMessage);
        clientResponse.setRawMessage(curMessage);

        return new ClientResponseParseMessage(remainingData, clientResponse);
    }

    /**
     * Message Parser that takes in the message and splits it into parts that
     * is accepted by the client. Also checks that the parsed message is valid.
     *
     * @param curMessage
     * @return
     */
    private static AbstractClientResponse parse(String curMessage) {

        String[] msgParts = curMessage.replace(MESSAGE_START, "")
                .replace(MESSAGE_END, "")
                .split(MESSAGE_SPLIT_REGEX);

        int code;

        try {
            code = Integer.parseInt(msgParts[0]);
        } catch (NumberFormatException ex) {
            logger.error("Error: ", ex);
            throw new InvalidMessageException("Invalid Code sent");
        }

        AbstractClientResponse msg;

        switch (code) {
            case MessageTypes.Codes.OPTION_LIST:
                msg = new OptionMessage(code, msgParts[1], msgParts[2]);
                break;
            default:
                msg = new GenericClientResponse(Integer.parseInt(msgParts[0]), msgParts[1]);
                break;
        }
        return msg;
    }
}
