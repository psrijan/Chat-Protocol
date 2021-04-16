/**
 * -----GenericServerRequest-------
 * This is a generic message model class
 * that handles most messages that is received by the server
 * 03/20/2020
 */

package com.srijan.pandey.server.model;


public class GenericServerRequest extends ServerRequest {

    private String command;
    private String body; // payload carried by the message

    public GenericServerRequest(String command, String body) {
        this.command = command;
        this.body = body;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getBody(){
        return body;
    }
}
