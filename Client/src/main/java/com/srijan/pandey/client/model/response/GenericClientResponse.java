/**
 * -----GenericClientResponse-----
 * Concrete Class that handles most server response messages
 * the body of the GenericClass contains code and body.
 * This is not the the format the server sends the data in,
 * but what the data looks like after client parses it.
 * 03/4/2020
 */
package com.srijan.pandey.client.model.response;


public class GenericClientResponse extends AbstractClientResponse {
    private int code;
    private String body;

    public GenericClientResponse(int code, String description) {
        this.code = code;
        this.body = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String geBody() {
        return body;
    }

    public String getMessage() {
        return String.format("%s" ,  body);
    }
}
