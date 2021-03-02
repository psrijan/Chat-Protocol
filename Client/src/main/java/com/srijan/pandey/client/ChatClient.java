/**
 * ---- Chat Client -----
 * Client main class. This is the start class of the client.
 * 03/1/2020
 */
package com.srijan.pandey.client;

import com.srijan.pandey.client.model.request.*;
import com.srijan.pandey.client.util.ClientMenuUtil;
import com.srijan.pandey.common.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatClient extends AbstractClient {

    //Option available for Client
    private final String LIST_GROUP_CLIENTS = "2";
    private final String CHAT = "3";
    private final String EXIT = "1";
    private final String VERSION = "4";

    private Logger log = LoggerFactory.getLogger(ChatClient.class);

    public ChatClient(String address, int port, boolean dynamicDiscovery) throws Exception {
        super(address, port, dynamicDiscovery);
        init();
    }


    private void init() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1. Super User 2. Chat User");
        String userType = sc.nextLine();
        // Super user don't chat but can add new users.
        boolean isSuperUser = userType.equals("1");
        clientState.setSuperUser(isSuperUser);
        log.debug("Client Super User?  {} ", isSuperUser);

        //Client loop that is used to display the UI or Login
        while (true) {
            log.debug("Checking display ");
            if (clientState.loginSuccess()) {
                displayUI(isSuperUser); // Displays the User Options if login is successful
            } else {
                login(); // Requests the user to login if the login state has not been successful
            }
            Thread.sleep(2000);
        }
    }


    /**
     * Displays the UI
     *
     * @param isSuperUser
     */
    private void displayUI(boolean isSuperUser) {
        if (!isSuperUser) {
            userFlow();
        } else {
            superUserFlow();
        }
    }

    /**
     * This is the chat user flow. Chat user can
     * choose between the following options mentioned
     */
    public void userFlow() {
        String option = ClientMenuUtil.mainMenu();
        log.debug("UI Option {}", option);

        switch (option) {
            case EXIT:
                System.exit(0);
            case LIST_GROUP_CLIENTS:
                displayOptions();
                break;
            case CHAT:
                chat();
                break;
            case VERSION:
                version();
                break;
            default:
                System.out.println("Option not available");
        }

    }

    /**
     * Display version information if available to the client
     * or request the server for version information and display it
     */
    public void version() {
        log.debug("Send Version request");
        if (clientState.getVersion() == null) {
            requestMediator.versionInfo();
        } else {
            System.out.println("Version: " + clientState.getVersion());
        }
    }

    /**
     * Super user flow, where the super user will
     * enter new user to register to the server
     */
    public void superUserFlow() {
        System.out.println("-----USER REGISTRATION----");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter New Username");
        String username = sc.nextLine();
        System.out.println("Enter New Password");
        String password = sc.nextLine();
        System.out.println("Retype Password");
        String retypePassword = sc.nextLine();

        AddUserRequest addUserRequest = new AddUserRequest(username, password);

        if (password.equals(retypePassword)) {
            requestMediator.addNewUser(addUserRequest);
        }
    }

    /**
     * Request sent to server to display
     * Client list or Group List
     */
    private void displayOptions() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1. Active Chatrooms 2. Active Users");
        String option = sc.nextLine();
        char msgOption = option.equals("1") ? 'G' : 'U';
        GroupClientRequest groupClientRequest = new GroupClientRequest(msgOption);
        requestMediator.listOptions(groupClientRequest);
    }

    /**
     * Login for chat as well as super user
     */
    private void login() {
        if (clientState.getPassMessage() == null) {
            System.out.println("---- Login ---- ");
            System.out.println("Enter Username :");
            String username = ClientMenuUtil.getInputStr();
            System.out.println("Enter Password: ");
            String password = ClientMenuUtil.getInputStr();
            LoginRequest loginModel = new LoginRequest(username, password, clientState.isSuperUser());
            requestMediator.authenticateUser(loginModel, clientState.isSuperUser());
            clientState.setAuthInitiated(true);
            clientState.setLoginModel(loginModel);
        } else {
            requestMediator.authenticatePassword(clientState.getPassMessage(), clientState.isSuperUser());
        }
    }


    /**
     * Chat message sent to the server
     */
    private void chat() {
        try {
            System.out.println("----Chat------");
            modeChoice();
            log.debug("Inside Chat Thread ");
            Scanner sc = new Scanner(System.in);
            String curLine = "";
            while ((curLine = sc.nextLine()) != null) {
                if (isChatInvalid(curLine)) {
                    System.out.println("Don't use < or > or |");
                } else {
                    ChatRequest chatModel = new ChatRequest(curLine);
                    requestMediator.initiateChat(chatModel);
                }
                Thread.sleep(100); // Flow control mechanism to client side so that UI doesn't flood the messages
            }
        } catch (InterruptedException ex) {
            log.error("Error: {}", ex);
        }
    }

    boolean isChatInvalid(String chatText) {
        boolean invalidText = chatText.contains("|") || chatText.contains("<") || chatText.contains(">");
        return invalidText;
    }


    /**
     * Selects either group mode or user mode.
     * In the user mode a user can chat one on one 
     * with another user. In a group mode multiple 
     * users chat with one another.
     */
    private void modeChoice() {
        boolean modeSelectRequried = true;
        while (modeSelectRequried) {
            // Mode Selection Part
            Scanner sc = new Scanner(System.in);
            System.out.println("Select Mode 1. User Mode 2. Group Mode");
            String modeChoice = sc.nextLine();

            if (modeChoice.equals("1")) {
                System.out.println("--- USER MODE ---");
                System.out.println("Enter User: ");
                String user = sc.nextLine();
                ModeSelectRequest modeSelectRequest = new ModeSelectRequest(CommonConstants.ModeSelectionConstants.USER, user);
                requestMediator.modeSelect(modeSelectRequest);
                modeSelectRequried = false;
            } else if (modeChoice.equals("2")) {
                System.out.println("--- Group MODE ---");
                System.out.println("Enter Group:");
                String group = sc.nextLine();
                ModeSelectRequest modeSelectRequest = new ModeSelectRequest(CommonConstants.ModeSelectionConstants.GROUP, group);
                requestMediator.modeSelect(modeSelectRequest);
                modeSelectRequried = false;
            } else {
                System.out.println("Mode Not Available...");
            }
        }
    }


    //CLIENT
    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Do you want to dynamically discover server? (options: yes, no)");
        String option = sc.nextLine();

        boolean isDynamicDiscovery = option.equalsIgnoreCase("yes");

        String ipAddress = "";
        //send dynamic discovery flag to the client to
        // automatically search for server
        if (!isDynamicDiscovery) {
            System.out.println("Enter Ip Address: ");
            ipAddress = sc.nextLine();
            // IP address regex used to validate the user entered ip address is valid
            String ipRegex = "[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}";
            Pattern pattern = Pattern.compile(ipRegex);
            Matcher m = pattern.matcher(ipAddress);
            // Match Pattern
            if (m.matches()) {
                ChatClient client = new ChatClient(ipAddress, 5001, false);
            } else {
                System.out.println("Invalid IP restart Client ...");
            }
        } else {
            ChatClient client = new ChatClient(ipAddress, 5001, true); /// true indicates dynamic discovery and chat client tries to auto detect the server in network
        }
    }

}
