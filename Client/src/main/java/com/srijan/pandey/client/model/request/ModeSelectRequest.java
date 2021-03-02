/**
 * ----ModeSelectRequest----
 *  DTO class that transports, type of mode User or Group
 *  and name of user or group that the client wants to connect
 *  to.
 *  03/13/2020
 */
package com.srijan.pandey.client.model.request;

public class ModeSelectRequest {
    private String type;
    private String connectTo;

    public ModeSelectRequest(String type, String connectTo) {
        this.type = type;
        this.connectTo = connectTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConnectTo() {
        return connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

}
