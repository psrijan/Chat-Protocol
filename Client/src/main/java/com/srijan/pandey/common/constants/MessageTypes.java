/**
 * --- MessageType---
 * Constant that contains Commands and Codes
 * which is sent two and fro the server and client
 * 02/29/2020
 */
package com.srijan.pandey.common.constants;

public class MessageTypes {

    /**
     * Client Request Message types
     */
    public interface Commands {
        String SUSER = "SUSER";
        String SPASS = "SPASS";
        String REGU = "REGU";
        String USER = "USER";
        String LIST = "LIST";
        String PASS = "PASS";
        String MODE = "MODE";
        String BYE = "BYE";
        String CHAT = "CHAT";
        String VERSION = "VERSION";
    }

    /**
     * Response code sent by the server to the client
     */
    public interface Codes {
        int REG_USER_SUCCESS = 100;
        int USER_AUTH_SUCCESS = 101;
        int PASS_AUTH_SUCCESS = 102;
        int OPTION_LIST = 103;
        int CHAT_RECEIVED = 104;
        int CONNECTION_CLOSED = 106;
        int MODE_SEL_SUCCESS = 105;
        int VERSION = 106;
        int CONN_ERROR = 598;
        int GENRIC_ERROR = 500;
        int PASS_AUTH_FAIL = 501;
        int USER_AUTH_FAIL = 502;
    }

}
