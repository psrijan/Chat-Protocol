/**
 * ---- CLientResponseParseMessage---
 * It is a DTO that is used to transfer the
 * remaining message in the stream as well as
 * the baseResponseMessage available.
 * 03/02/2020
 */
package com.srijan.pandey.client.model.response;

public class ClientResponseParseMessage {
    private String streamString;
    private AbstractClientResponse baseResponse;

    public ClientResponseParseMessage(String streamString, AbstractClientResponse baseResponse) {
        this.streamString = streamString;
        this.baseResponse = baseResponse;
    }

    public String getStreamString() {
        return streamString;
    }

    public void setStreamString(String streamString) {
        this.streamString = streamString;
    }

    public AbstractClientResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(AbstractClientResponse baseResponse) {
        this.baseResponse = baseResponse;
    }
}
