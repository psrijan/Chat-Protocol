/**
 * ---- ConversationModule----
 * Conversation Module is a module that handles chat conversation between users.
 * This class is responsible for connecting two or more users
 * and relaying chat information from one user to another.
 * 02/26/2020
 */
package com.srijan.pandey.server.core.conversation;

import com.srijan.pandey.common.constants.CommonConstants;
import com.srijan.pandey.common.exceptions.UserUnavailableException;
import com.srijan.pandey.server.core.server.ConcurrentChatThread;
import com.srijan.pandey.server.dto.AddConversationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ConversationModule {

    //All available activeServer snapshot
    private List<ConcurrentChatThread> activeServers;
    //username , conversation
    private Map<String, Conversation> userConversation = new HashMap<>();
    private Map<String, Conversation> groupConversation = new HashMap<>();

    private Logger log = LoggerFactory.getLogger(ConversationModule.class);

    public ConversationModule(List<ConcurrentChatThread> activeServers) {
        this.activeServers = activeServers;
    }

    private ConcurrentChatThread parent;

    /**
     * Sends the chat along to the connected party returns
     *
     * @param username
     * @param mode
     * @param message
     * @return returns a boolean status indicating if it was able to send the message
     */
    public boolean relayChat(String username, String mode, String message, String fromUsername) {
        log.debug("Relaying Chat Message...");

        Conversation conversation = null;

        // Find Conversation based on the type of mode it is
        // User mode find conversation in User Map
        // and Group mode find conversation  Group Map
        if (mode.equals(CommonConstants.ModeSelectionConstants.GROUP))
            conversation = groupConversation.get(username);
        else
            conversation = userConversation.get(username);

        // Send Response to second party if conversation exist
        // or send a error response to the sending party
        if (conversation == null)
            return false;

        conversation.relayChat(username, message, fromUsername);
        return true;
    }

    /**
     * Lists out users that are busy conversing with other users.
     *
     * @return user name is returned
     */
    public List<String> getBusyUsers() {
        return userConversation.keySet().stream().collect(Collectors.toList());
    }

    /**
     * Kick user is supposed to remove a user from the conversation
     * @param username
     */
    public void kickUser(String username) {
        Conversation conversation = userConversation.get(username);
        if (conversation != null && conversation.getConversationType().equals(CommonConstants.ModeSelectionConstants.USER)) {
            conversation.kickUser(null);
            userConversation.remove(conversation); //@todo check that this gets removed
            return;
        }

        conversation = groupConversation.get(username);
        if (conversation != null ) {
            conversation.kickUser(username);
        }

    }

    public void addConversation(AddConversationDTO addConversationDTO) throws Exception {
        if (addConversationDTO.getMode().equals("G")) {
            addGroupConversation(addConversationDTO);
        } else {
            addUserConversation(addConversationDTO);
        }
    }

    /**
     * Checks is user isn't active in other conversation. Each user can only chat with one user or group at a time.
     *
     * @param username
     * @return
     */
    boolean isUserAvailable(String username) {
        Optional<String> optUser = userConversation.keySet().stream().filter(s -> s.equals(username)).findAny();
        Optional<String> optGroup = groupConversation.keySet().stream().filter(s -> s.equals(username)).findAny();
        return !(optGroup.isPresent() && optUser.isPresent());
    }

    /**
     * Adds user conversation.
     * @throws UserUnavailableException
     */
    private void addUserConversation(AddConversationDTO addConversationDTO) throws UserUnavailableException {
        log.debug("Adding User Conversation");

        String userOne = addConversationDTO.getUserOne();
        String userTwo = addConversationDTO.getMessageParam();
        List<String> availableList = addConversationDTO.getAllUsers();

        long userCount = availableList.stream().filter(s -> s.equals(addConversationDTO.getMessageParam())).count();

        if (userCount == 0)
            throw new UserUnavailableException("User Not available");

        if (userOne.equals(addConversationDTO.getMessageParam())) {
            throw new UserUnavailableException("You cannot connect to same user");
        }

        if (!(isUserAvailable(userOne))) {
            String message = String.format("The user %s unavailable", userOne);
            throw new UserUnavailableException(message);
        }

        if (!isUserAvailable(userTwo)) {
            String message = String.format("The user %s unavailable", userTwo);
            throw new UserUnavailableException(message);
        }

        try {
            Conversation conversation = new Conversation(addConversationDTO.getMode());
            ConcurrentChatThread userOneServer = activeServers.stream().filter(concurrentChatServer -> concurrentChatServer.getUsername().equals(userOne)).findAny().get();
            ConcurrentChatThread userTwoServer = activeServers.stream().filter(concurrentChatServer -> concurrentChatServer.getUsername().equals(userTwo)).findAny().get();
            conversation.addChatThreads(userOne, userOneServer);
            conversation.addChatThreads(userTwo, userTwoServer);
            userConversation.put(userOne, conversation);
            userConversation.put(userTwo, conversation);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The method adds users to GroupConversation upon request. It sees if a GroupConversation exists.
     * If it does, it adds user to existing group conversation, if it doesn't it adds it to new group conversation.
     *
     */
    private void addGroupConversation(AddConversationDTO addConversationDTO) throws UserUnavailableException {
        log.debug("Adding Group Conversation");

        String user = addConversationDTO.getUserOne();
        String groupName = addConversationDTO.getMessageParam();
        String mode = addConversationDTO.getMode();

        if (!(isUserAvailable(user))) {
            String message = String.format("The user %s unavailable", user);
            throw new UserUnavailableException(message);
        }

        // Find Concurrent server based on group name
        Conversation conversation = findGroupConversation(groupName);

        //there is an existing group conversation
        if (conversation != null) {
            ConcurrentChatThread concurrentChatServer = getUsersConcurrentChatServer(user);
            conversation.addChatThreads(user, concurrentChatServer);
            groupConversation.put(user, conversation);
        } else {
            //There is no group conversation and we need to create a new conversation.
            try {
                conversation = new Conversation(mode);
                ConcurrentChatThread userServer = getUsersConcurrentChatServer(user);
                conversation.addChatThreads(user, userServer);
                conversation.setConversationName(groupName);
                groupConversation.put(user, conversation);
            } catch (NoSuchElementException ex) {
                log.debug("Error: ", ex);
            }
        }
    }

    /**
     * Method Finds Group Conversation that is available in the server
     *
     * @param groupName
     * @return
     */
    private Conversation findGroupConversation(String groupName) {

        for (String key : groupConversation.keySet()) {
            Conversation conversation = groupConversation.get(key);
            if (conversation.getConversationName().equals(groupName))
                return conversation;
        }
        return null;

    }

    /**
     * Finds ConcurrentServer that is mapped to a user and returns it.
     *
     * @param user
     * @return
     */
    private ConcurrentChatThread getUsersConcurrentChatServer(String user) {
        return activeServers.stream().filter(concurrentChatServer -> {
                    if (concurrentChatServer.getUsername() == null)
                        return false;
                    else {
                        return user.equals(concurrentChatServer.getUsername());
                    }
                }
        ).findAny().get();
    }
}

