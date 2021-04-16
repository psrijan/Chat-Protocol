/**
 * ----AddConversationDTO----
 * Internatial DTO that is used to create new conversation
 * 02/28/2020
 */
package com.srijan.pandey.server.dto;

import java.util.List;

public class AddConversationDTO {
    private String mode;
    private String userOne;
    private String messageParam;
    private List<String> allUsers;


    public AddConversationDTO(String mode, String userOne, String messageParam, List<String> allUsers) {
        this.mode = mode;
        this.userOne = userOne;
        this.messageParam = messageParam;
        this.allUsers = allUsers;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String userOne) {
        this.userOne = userOne;
    }

    public String getMessageParam() {
        return messageParam;
    }

    public void setMessageParam(String messageParam) {
        this.messageParam = messageParam;
    }

    public List<String> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<String> allUsers) {
        this.allUsers = allUsers;
    }
}
