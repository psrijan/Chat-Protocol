package com.srijan.pandey.server.util;

import com.srijan.pandey.common.constants.CommonConstants;

import java.util.ArrayList;
import java.util.List;

import static com.srijan.pandey.common.constants.CommonConstants.MessageDelimiters.*;


public class ServerResponseBuilder {

    public static String buildOptionResponse(int code, String body , String type ) {
         String messageBody = body+ MESSAGE_SPLIT+ type;
        return genericMessage(String.valueOf(code) , messageBody);
    }

    /**
     * Builds chat response and ensures that the chat message isn't bigger than the designated size of the
     * response message
     * @param code
     * @param message
     * @param fromUsername
     * @return
     */
    public static List<String> buildChatResponse(int code, String message,  String fromUsername ) {
        int totalSize = String.valueOf(code).length() + fromUsername.length() + message.length();
        List<String> msgList = new ArrayList<>();

        if(totalSize > CommonConstants.MessageSize.REQUEST_MESSAGE_SIZE) {
            int extraSize = totalSize - CommonConstants.MessageSize.REQUEST_MESSAGE_SIZE;
            String msgOne = message.substring(0 , message.length() - extraSize);
            String chatMessage1 = buildResponse(code, fromUsername+ ":" + msgOne);
            String msgTwo = message.substring(message.length() - extraSize);
            String chatMessage2 = buildResponse(code, fromUsername+ ":" + msgTwo);
            msgList.add(chatMessage1);
            msgList.add(chatMessage2);
        } else {
           String chatMessage = buildResponse(code, fromUsername +":" + message);
           msgList.add(chatMessage);
        }
        return  msgList;
    }

    public static String buildResponse(int code , String body) {
        return genericMessage(String.valueOf(code) , body);
    }


    private static String genericMessage(String command, String str) {

        StringBuilder stringBuffer = new StringBuilder();

        return stringBuffer
                .append(MESSAGE_START)
                .append(command)
                .append(MESSAGE_SPLIT)
                .append(str)
                .append(MESSAGE_END)
                .toString();
    }
}
