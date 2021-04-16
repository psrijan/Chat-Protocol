/**
 * ---MessageValidationDTO----
 * DTO used for validation of the Server Message
 * 03/20/2020
 */
package com.srijan.pandey.server.dto;

import com.srijan.pandey.server.state.ServerState;
import com.srijan.pandey.server.stubs.ServerDatabase;

public class MessageValidationDTO {
    private ServerState serverState;
    private ServerDatabase serverDatabase;
    private String username;

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public ServerDatabase getServerDatabase() {
        return serverDatabase;
    }

    public void setServerDatabase(ServerDatabase serverDatabase) {
        this.serverDatabase = serverDatabase;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
