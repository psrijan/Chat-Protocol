/**
 * ----ClientRequestBuilder------
 * Builds the Raw Request Messages that is sent over from
 * the client to the inputstream of the socket
 * 03/11/2020
 */
package com.srijan.pandey.client.util;

import java.util.ArrayList;
import java.util.List;

import static com.srijan.pandey.common.constants.MessageTypes.Commands.*;
import static com.srijan.pandey.common.constants.CommonConstants.MessageDelimiters.*;

public class ClientRequestBuilder {


    public static String userMessage(String username, boolean isSuperUser) {
        String command = isSuperUser ? SUSER : USER;
        return genericMessage(command, username);
    }

    public static String passMessage(String password, boolean isSuperUser) {
        String command = isSuperUser ? SPASS : PASS;
        return genericMessage(command, password);
    }

    public static String optionMessage(char optionType) {
        return genericMessage(LIST, String.valueOf(optionType));
    }

    public static String addUserRequest(String username, String password) {
        String message = username + "|" + password;
        return genericMessage(REGU, message);
    }

    /**
     * Chat Message splits the message into chunks that is acceptable by the server and sends it
     * over to the server side.
     * @param chat
     * @return
     */
    public static List<String> chatMessage(String chat) {
        List<String> messages = new ArrayList<>();

        while (chat.length() > 0) {
            if(chat.length() > 240) {
                String splitMsg =chat.substring(0 , 240);
                chat = chat.substring(240);
                String raw = genericMessage(CHAT, splitMsg);
                messages.add(raw);
            } else {
                String raw = genericMessage(CHAT , chat);
                messages.add(raw);
                return messages;
            }
        }
        return messages;
    }

    public static String modeMessage(String type, String connectTo) {
        String body = type + "|" + connectTo;
        ;
        return genericMessage(MODE, body);
    }

    public static String versionMessage() {
        return genericMessage(VERSION, " ");
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
