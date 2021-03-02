/**
 * -----AddUserRequest------
 * DTO class that transports the username
 * and password to the sendMessage function
 * 03/1/2020
 */

package com.srijan.pandey.client.model.request;

public class AddUserRequest {
    private String username;
    private String password;

    public AddUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
