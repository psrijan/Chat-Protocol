/**
 * Client State
 * Client Side State Management DTO
 * This class also contains additional variables
 * that may not be identified by the DFA
 * 03/13/2020
 */
package com.srijan.pandey.client.state;

import com.srijan.pandey.client.model.request.LoginRequest;

import java.util.ArrayList;
import java.util.List;

public class ClientState {
    //------------- USER STATE VARIABLES ----------
    private boolean userAuth;
    private boolean passAuth;
    private boolean superUser;
    private boolean authInitiated;
    private boolean modeSelect;

    //---- Additional Datatypes for user ------
    private LoginRequest loginRequest;
    private ModeSelectState modeSelectState;
    private List<String> groupList;
    private List<String> userList;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addGroups(String groups) {
        if(groupList == null)
            groupList = new ArrayList<>();
        groupList.add(groups);
    }

    public void addUsers(String users )  {
        if(userList == null)
            userList = new ArrayList<>();
        userList.add(users);
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public boolean isAuthInitiated() {
        return authInitiated;
    }

    public void setAuthInitiated(boolean authInitiated) {
        this.authInitiated = authInitiated;
    }

    public boolean loginSuccess() {
        return userAuth && passAuth;
    }

    public boolean isUserAuth() {
        return userAuth;
    }

    public void setUserAuth(boolean userAuth) {
        this.userAuth = userAuth;
    }

    public boolean isPassAuth() {
        return passAuth;
    }

    public void setPassAuth(boolean passAuth) {
        this.passAuth = passAuth;
    }


    public LoginRequest getPassMessage() {
        return loginRequest;
    }

    public void setLoginModel(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    public void resetLogin() {
        loginRequest = null;
        userAuth = false;
        passAuth = false;
        authInitiated = false;
    }

    public ModeSelectState getModeSelectState() {
        return modeSelectState;
    }

    public void setModeSelectState(ModeSelectState modeSelectState) {
        this.modeSelectState = modeSelectState;
    }

    public void setModeSelect(boolean modeSelect) {
        this.modeSelect = modeSelect;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }
}
