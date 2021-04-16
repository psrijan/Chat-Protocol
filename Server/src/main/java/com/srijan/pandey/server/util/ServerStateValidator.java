/**
 * ---ServerStateValidator----
 * Validates if the Server is in correct state and
 * 03/10/2020
 */
package com.srijan.pandey.server.util;

import com.srijan.pandey.common.constants.MessageTypes.Commands;
import com.srijan.pandey.common.exceptions.InvalidStateException;
import com.srijan.pandey.server.model.ServerRequest;
import com.srijan.pandey.server.state.ServerState;

public class ServerStateValidator {


    /**
     * Validation function that checks if the server
     * is in correct state or throws an exception
     *
     * @param request
     * @param serverState
     */
    public static void validateState(ServerRequest request, ServerState serverState) {

        switch (request.getCommand()) {
            case Commands.USER:
                break;
            case Commands.SUSER:
                break;
            case Commands.PASS:
                if (!serverState.isUserAuth())
                    throw new InvalidStateException("User not authenticated");
                break;
            case Commands.SPASS:
                if (!serverState.isUserAuth() || !serverState.isSuperUser())
                    throw new InvalidStateException("User not authenticated");
                break;
            case Commands.MODE:
                if (!serverState.isUserAuth() || !serverState.isPassAuth() || serverState.isSuperUser())
                    throw new InvalidStateException("User not authenticated or allowed in state");
                break;
            case Commands.LIST:
                if (!serverState.isUserAuth() || !serverState.isPassAuth() || serverState.isSuperUser())
                    throw new InvalidStateException("User not authenticated or allowed in state");
                break;
            case Commands.VERSION:
                if (!serverState.isUserAuth() || !serverState.isPassAuth())
                    throw new InvalidStateException("User not authenticated or allowed in state");
                break;
            case Commands.REGU:
                if (!serverState.isUserAuth() || !serverState.isSuperUser() || !serverState.isPassAuth())
                    throw new InvalidStateException("User not authenticated");
                break;

        }
    }

}
