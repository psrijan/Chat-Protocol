/**
 * ---Conversation----
 * Conversations are connections between two users.
 * There can be two type of Conversation, UserConversation and GroupConversation
 * which can be identified by the conversation type.
 * GroupConversations contains more than two ConcurrentChatThreads, where as UserConversations
 * will only have two ConcurrentChatThread.
 * 02/25/2020
 */
package com.srijan.pandey.server.core.conversation;

import com.srijan.pandey.common.constants.CommonConstants;
import com.srijan.pandey.server.core.server.ConcurrentChatThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Conversation {
    private String conversationType; //U or G depending on UserConversation or GroupConversation
    //contains group name
    private String conversationName; // Group Name of the conversation if its of type G
    private Map<String, ConcurrentChatThread> concurrentChatServers; // Number of ConcurrentChatServers of different users that is used to send message to indivdual users

    private Logger logger = LoggerFactory.getLogger(Conversation.class);

    public Conversation(String conversationType) {
        this.conversationType = conversationType;
        this.concurrentChatServers = new HashMap<>();
    }

    /**
     * Add chat thread to the conversation. Add thread of users that want to communicate in the conversation
     *
     * @param username
     * @param concurrentChatThread
     */
    public void addChatThreads(String username, ConcurrentChatThread concurrentChatThread) {
        this.concurrentChatServers.put(username, concurrentChatThread);
    }

    /**
     * Send Messages two the second party
     *
     * @param username
     * @param message
     */
    public void relayChat(String username, String message, String fromUsername) {
        logger.debug("Conversation Class Relay");
        for (String key : concurrentChatServers.keySet()) {
            if (!key.equals(username)) {
                ConcurrentChatThread concurrentChatServer = concurrentChatServers.get(key);
                logger.debug("Sending Chat {} to {}", message, key);
                concurrentChatServer.getResponseModerator().sendChat(message, fromUsername);
            }
        }
    }

    /**
     * Kick all users if conversation type is user
     * Kick the particular user in case of a Group Conversation
     * @param username
     */
    public void kickUser(String username) {
        if (conversationType.equals(CommonConstants.ModeSelectionConstants.USER)) {
            concurrentChatServers.forEach((s, concurrentChatThread) -> {
                concurrentChatThread.closeChatThread();
            });
            concurrentChatServers = null; // freeing up memory
        } else if (conversationType.equals(CommonConstants.ModeSelectionConstants.GROUP)) {
            ConcurrentChatThread concurrentChatThread= concurrentChatServers.get(username);
            concurrentChatThread.closeChatThread();
            concurrentChatServers.remove(username);
        }
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }
}
