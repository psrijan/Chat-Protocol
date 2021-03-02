/**
 * ----RESPONSE MEDIATOR----
 * This class handles all the server response for a client.
 * This class runs as a separate thread and handles incoming 
 * messages from the server. There are separate threads created 
 * for this to ensure that the UI thread doesn't hang when there 
 * are long running conversations. 
 * 03/1/2020
 */
package com.srijan.pandey.client.core;

import com.srijan.pandey.client.model.response.AbstractClientResponse;
import com.srijan.pandey.client.model.response.ClientResponseParseMessage;
import com.srijan.pandey.client.model.response.GenericClientResponse;
import com.srijan.pandey.client.model.response.OptionMessage;
import com.srijan.pandey.client.state.ClientState;
import com.srijan.pandey.client.util.ClientResponseParser;
import com.srijan.pandey.common.constants.CommonConstants;
import com.srijan.pandey.common.constants.MessageTypes.Codes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.InputStream;


public class ResponseMediator implements Runnable {

    private DataInputStream inputStream;
    private Logger logger = LoggerFactory.getLogger(RequestMediator.class);
    private ClientState clientState;

    public ResponseMediator(InputStream is, ClientState clientState) {
        this.inputStream = new DataInputStream(is);
        this.clientState = clientState;
    }

    @Override
    public void run() {
        logger.debug("Starting Response Mediator Thread");
        receiveMessage();
    }

    public void receiveMessage() {
        String streamString = "";
        try {
            while (true) {
                logger.debug("ResponseMediator - receiveMessage");
                String msg = inputStream.readUTF();
                streamString += msg;
                logger.debug("Message; {} ", msg);
                ClientResponseParseMessage response = ClientResponseParser.parseResponse(streamString);
                streamString = response.getStreamString();
                processMessage(response.getBaseResponse());
                Thread.sleep(500);
            }
        } catch (Exception ex) {
            logger.error(" Error: ", ex);
        }
    }

    /**
     * Here we have confirmed that the message is valid now we send the message to
     * various functions to do further action. This part of the code ensures that the
     * client states are updated as it should.
     *
     * @param response
     */
    public void processMessage(AbstractClientResponse response) {
        logger.debug("Processing Received Message ");
        int code = response.getCode();
        logger.debug("Response Code - {} ", code);
        logger.debug("Message: {} ", response.getMessage());
        switch (code) {
            case Codes.USER_AUTH_SUCCESS:
                System.out.println(response.getMessage());
                clientState.setUserAuth(true);
                logger.debug(response.getMessage());
                break;
            case Codes.PASS_AUTH_SUCCESS:
                System.out.println(response.getMessage());
                clientState.setPassAuth(true);
                logger.debug(response.getMessage());
                break;
            case Codes.MODE_SEL_SUCCESS:
                System.out.println(response.getMessage());
                System.out.println("--CHAT--");
                clientState.setModeSelect(true);
                break;
            case Codes.OPTION_LIST:
                processOptionList(response);
                break;
            case Codes.CHAT_RECEIVED:
                System.out.println(response.getMessage());
                break;
            case Codes.REG_USER_SUCCESS:
                System.out.println(response.getMessage());
                clientState.setUserAuth(true);
                break;
            case Codes.PASS_AUTH_FAIL:
                clientState.resetLogin();
                break;
            case Codes.VERSION:
                clientState.setVersion(((GenericClientResponse) response).getBody());
                System.out.println("Version: " + clientState.getVersion());
                break;
            case Codes.GENRIC_ERROR:
                System.out.println(response.getMessage());
                logger.debug(response.getMessage());
                break;
            default:
                logger.debug("Message Option {} Message {} ", code, response.getMessage());
        }
    }

    /**
     * This function decides how to proess the option list.
     *
     * @param baseResponse
     */
    public void processOptionList(AbstractClientResponse baseResponse) {
        OptionMessage optionMessage = (OptionMessage) baseResponse;
        System.out.println(optionMessage.getValue());

        if (optionMessage.getOptionType().equals(CommonConstants.ModeSelectionConstants.GROUP)) {
            clientState.addGroups(optionMessage.getValue());
        } else {
            clientState.addUsers(optionMessage.getValue());
        }
    }

}
