/**
 * ----ChatRequest----
 * This class transports the ChatRequest message
 * to the sendMessage function within the client class
 * 03/1/2020
 */

package com.srijan.pandey.client.model.request;

public class ChatRequest {
    String chatText;

    public ChatRequest(String chatText) {
        this.chatText = chatText;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

}
