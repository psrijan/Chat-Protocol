package com.srijan.pandey.server.state;

/**
 * ----ServerState-------
 * State class that contains the current state of the Chat Thread
 * this class is used to validate that the DFA is in the correct state
 * at any given time.
 * 03/20/2020
 * STATEFUL
 */
public class ServerState {

    private boolean userAuth;
    private boolean passAuth;
    private boolean modeSelection;

    public boolean isModeSelection() {
        return modeSelection;
    }

    public void setModeSelection(boolean modeSelection) {
        this.modeSelection = modeSelection;
    }

    private String username;
    private String mode;
    private boolean superUser;


    public boolean isSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPassAuth() {
        return passAuth;
    }

    public void setPassAuth(boolean passAuth) {
        this.passAuth = passAuth;
    }

    public boolean isUserAuth() {
        return userAuth;
    }

    public void setUserAuth(boolean userAuth) {
        this.userAuth = userAuth;
    }
}
