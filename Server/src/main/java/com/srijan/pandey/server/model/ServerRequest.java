package com.srijan.pandey.server.model;

/**
 * ----ServerRequest-----
 * Abstract ServerRequest class that is
 * Base Class for all ServerRequest class
 * 03/20/2020
 */
public abstract class ServerRequest {

    private String rawMessage;

    public abstract String getCommand();
    public abstract void setCommand(String command);
    public abstract String getBody();

    public String getRawMessage() {
        return this.rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage =rawMessage;
    }


}
