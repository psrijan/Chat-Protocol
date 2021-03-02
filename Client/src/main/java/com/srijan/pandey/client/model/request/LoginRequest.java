/**
 * ----- LoginRequest------
 * DTO class that is used to transport login
 * details to the sendMessage inside Client code
 * 03/1/2020
 */
package com.srijan.pandey.client.model.request;

public class LoginRequest {
    private String username;
    private String password;
    private boolean superUser;

    public LoginRequest(String username, String password, boolean superUser) {
        this.username = username;
        this.password = password;
        this.superUser = superUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

}
