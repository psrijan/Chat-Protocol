/**
 * ----ChatServer-----
 * The Main Class For Chat Server
 * This class initializaes StudDatabase, Conversations and chat threads
 * 03/20/2020
 */
package com.srijan.pandey.server;

import com.srijan.pandey.server.core.server.ConcurrentChatThread;
import com.srijan.pandey.server.core.conversation.ConversationModule;
import com.srijan.pandey.server.stubs.ServerDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Actual Chat Server uses TLS
 * to ensure security
 */
public class ChatServer {

    private List<ConcurrentChatThread> activeServerSockets = new ArrayList<>();
    private ConversationModule conversationModule = null;
    private Logger log = LoggerFactory.getLogger(ChatServer.class);
    private ServerDatabase serverDatabase;

    public static void main(String[] args) {
        System.out.println("Chat Server Started...");
        ChatServer chatServer = new ChatServer();
        chatServer.init();
    }

    /**
     * Initialize a stubDatabase
     * Initializes a ConversationModule.
     */
    private void init() {
        log.debug("Starting Chat Server...");
        serverDatabase = new ServerDatabase();
        conversationModule = new ConversationModule(activeServerSockets);
        try {
            ServerSocket serverSocket = new ServerSocket(5001); //SERVICE
            for (int i = 0; i < 1000; i++) {
                log.debug("Initializing Connection Instance - {} ", i);
                ConcurrentChatThread concurrentChatServer = new ConcurrentChatThread(serverSocket.accept() , conversationModule , serverDatabase); // will wait until a user connects to the server
                log.debug("Accepted New Chat Client");
                Thread thread = new Thread(concurrentChatServer);
                activeServerSockets.add(concurrentChatServer);
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
