package com.srijan.pandey.server.model;

/**
 * ------UserRegistrationRequest----
 * Contains registration information such as username and password
 * of a chat client sent out by an admin client to register new user.
 * 03/20/2020
 */
public class UserRegistrationRequest extends ServerRequest {

    private String command;
    private String username;
    private String password;

    public UserRegistrationRequest(String command, String username, String password) {
        this.command = command;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getBody() {
        return username + " " + password;
    }
}
