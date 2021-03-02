/**
 * ------AbstractClientResponse-----
 * Base Class for Client Response. This holds the raw message
 * that is accepted by the client from the server. Other implementations
 * are in the concrete class.
 * 03/05/2020
 */
package com.srijan.pandey.client.model.response;

public abstract class AbstractClientResponse {

    private String rawMessage;

    public abstract int getCode();

    public abstract void setCode(int code);

    public abstract String getMessage();

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }


}
