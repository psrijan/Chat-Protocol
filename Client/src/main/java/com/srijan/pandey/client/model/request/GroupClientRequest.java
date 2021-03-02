/**
 * ----GroupClientRequest-----
 * This is a DTO class that transports request message
 * within chat client to decide on option for chat protocol.
 * 03/08/2020
 */
package com.srijan.pandey.client.model.request;

public class GroupClientRequest {
    private char option;

    public GroupClientRequest(char option) {
        this.option = option;
    }

    public char getOption() {
        return option;
    }

    public void setOption(char option) {
        this.option = option;
    }


}
