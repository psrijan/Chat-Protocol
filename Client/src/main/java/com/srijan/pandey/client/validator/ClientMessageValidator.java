/**
 * ---- ClientMessageValidator----
 * Validate chat message to ensure that it is 
 * of the size defined in the specification 
 */
package com.srijan.pandey.client.validator;

import com.srijan.pandey.client.model.response.AbstractClientResponse;
import com.srijan.pandey.common.constants.CommonConstants;
import com.srijan.pandey.common.exceptions.InvalidMessageException;

public class ClientMessageValidator {

    public static void validate(AbstractClientResponse clientResponse) {
        String rawMessage = clientResponse.getRawMessage();

        if(rawMessage.length() > CommonConstants.MessageSize.REQUEST_MESSAGE_SIZE)
            throw new InvalidMessageException("Received Message Greater than expected");

    }

}
