package com.srijan.pandey.server.model;

/**
 * ---ModeRequest--
 * Mode Request is used to connect to a particular connection based on the
 * type of mode of the user.
 * 03/20/2020
 */
public class ModeRequest extends ServerRequest {

    private String mode; // U or G based on the type of mode selected
    private String command;
    private String name; //username or group name

    public ModeRequest(String command, String mode, String name) {
        this.mode = mode;
        this.command = command;
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public String getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getBody() {
        return null;
    }
}
