/**
 * ----ModeSelectState----------
 * This class contains the States for Mode Selection.
 *
 *
 */
package com.srijan.pandey.client.state;

import java.util.ArrayList;
import java.util.List;


public class ModeSelectState {

    private List<String> userList;
    private List<String> groupList;

    public ModeSelectState() {
        // Initializing the state with empty lists
        List<String> userList = new ArrayList<>();
        List<String> groupList = new ArrayList<>();
    }

    public void addUser(String user) {
        userList.add(user);
    }

    public void addGroup(String group) {
        groupList.add(group);
    }

    public List<String> getUserList() {
        return userList;
    }

    public List<String> getGroupList() {
        return groupList;
    }

}
