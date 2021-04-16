package com.srijan.pandey.server.stubs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * -----ServerDatabase-----
 * Stub database class that holds
 * username and password of the user
 * 03/20/2020
 */
public class ServerDatabase {

    Logger log = LoggerFactory.getLogger(ServerDatabase.class);

    /**
     * Global level variables
     */
    private Map<String, String> superUsers;
    private Map<String, String> users;
    private String version = "1.0";
    private List<String> availableGroups;

    public ServerDatabase() {
        initPassword();
    }

    public String getVersion() {
        return version;
    }

    public List<String> getUserList() {
        return new ArrayList<>(users.keySet());
    }


    public List<String> getSuperUserList() {
        return new ArrayList<>(superUsers.keySet());
    }


    private void initPassword() {
        users = new HashMap<>();
        users.put("srijan", "srijan");
        users.put("richard", "richard");
        users.put("edwin" , "edwin");
        users.put("mike" , "mike");

        superUsers = new HashMap<>();
        superUsers.put("admin", "admin");

        availableGroups = new ArrayList<>();
        availableGroups.add("Games");
        availableGroups.add("Studies");
        availableGroups.add("Fun");
    }

    public List<String> getAvailableGroups() {
        return availableGroups;
    }

    /**
     * Admin Client can add new users to the database
     *
     * @param username
     * @param password
     */
    public void addNewUsers(String username, String password) {
        users.put(username, password);
    }

    public List<String> availableUsers() {
        return users.keySet().stream().collect(Collectors.toList());
    }

    public boolean userAvailable(String username, boolean isSuperUser) {
        if (isSuperUser) {
            String password = superUsers.get(username);
            return password != null;
        } else {
            String password = users.get(username);
            return password != null;
        }
    }

    /**
     * Check that the password matches for a user.
     * @param username
     * @param password
     * @param isSuperUser
     * @return
     */
    public boolean passwordMatch(String username, String password, boolean isSuperUser) {
        log.debug("Username {} , Password {} , isSuper {} ", username, password, isSuperUser);
        if (isSuperUser) {
            String savedpassword = superUsers.get(username);
            return password.equals(savedpassword);
        } else {
            String savedUsername = users.get(username);
            return password.equals(savedUsername);
        }
    }


}
