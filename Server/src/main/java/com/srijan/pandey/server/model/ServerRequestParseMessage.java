package com.srijan.pandey.server.model;

/**
 * -----ServerRequestParseMessage----
 * Internaal DTO that is used to tranfer parse message
 * inside the server side
 * 03/20/2020
 */
public class ServerRequestParseMessage {

    private ServerRequest serverRequest;
    private String streamMessage;

    public ServerRequestParseMessage(ServerRequest serverRequest, String streamMessage) {
        this.serverRequest = serverRequest;
        this.streamMessage = streamMessage;
    }

    public ServerRequest getServerRequest() {
        return serverRequest;
    }

    public void setServerRequest(ServerRequest serverRequest) {
        this.serverRequest = serverRequest;
    }

    public String getStreamMessage() {
        return streamMessage;
    }

    public void setStreamMessage(String streamMessage) {
        this.streamMessage = streamMessage;
    }
}
