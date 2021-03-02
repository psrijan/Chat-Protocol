/**
 * ----Abstract Client -----
 * Inside class of the client main class.
 * This class hides the implementation detail of the client
 * 03/1/2020
 */
package com.srijan.pandey.client;

import com.srijan.pandey.client.core.RequestMediator;
import com.srijan.pandey.client.core.ResponseMediator;
import com.srijan.pandey.client.state.ClientState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class AbstractClient {

    private Socket socket = null; // Client socket to connect to server
    private DataInputStream userInputStream = null; // Input Stream to receive from server
    protected ClientState clientState = null; // state of the client

    protected RequestMediator requestMediator = null;
    protected ResponseMediator responseMediator = null;

    Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    private String address; // address of the server to connect to
    private int port; // port address of the server
    private boolean dynamicDiscovery; //is dynamic discovery required

    /**
     * Constructor
     * @param address - ipaddress
     * @param port
     * @param dynamicDiscovery
     */
    public AbstractClient(String address, int port, boolean dynamicDiscovery) {
        clientState = new ClientState();
        this.address = address;
        this.port = port;
        this.dynamicDiscovery = dynamicDiscovery;

        initiaize();
    }

    /**
     * Checks if dynamic discovery of server is required,
     * If not it opens a socket port. If dynamic discovery is set. Then
     * requests to discover server by itself
     */
    public void initiaize() {

        if (!dynamicDiscovery) {
            openSocket(address, port);
        } else {
            dynamicDiscovery(port);
        }
        createResponseHandler();
    }

    /**
     * Creates response Mediator thread that handles responses from the
     * server side
     */
    private void createResponseHandler() {
        Thread responseHandlerThread = new Thread(responseMediator);
        responseHandlerThread.start();
    }


    /**
     * Dynamic Discovery enables automatic discovery of the port 
     * number inside a network
     * @param port
     */
    private void dynamicDiscovery(int port) {
        logger.debug("Trying to discover Server Address ");
        boolean discoveryComplete = false;
        try {

            InetAddress inetAddress = InetAddress.getLocalHost();
            logger.debug("IP Address:- " + inetAddress.getHostAddress());
            logger.debug("Host Name:- " + inetAddress.getHostName());

            String ipAddress = inetAddress.getHostAddress();
            int firstDotIndex = ipAddress.indexOf(".");
            int secondDotIndex = ipAddress.indexOf(".", firstDotIndex + 1);
            String networkAddress = ipAddress.substring(0, secondDotIndex);

            for (int j = 0; j <= 255; j++) {
                String address1 = String.valueOf(j);
                for (int i = 0; i <= 255; i++) {
                    String address2 = String.valueOf(i);
                    String address = networkAddress + "." + address1 + "." + address2;

                    if (openSocket(address, port)) {
                        discoveryComplete = true;
                        break;
                    }
                }
                if (discoveryComplete)
                    break;
            }
        } catch (UnknownHostException ex) {
            logger.error("Error: {} ", ex);
        }

        if(!discoveryComplete) {
            System.out.println("Can't Discover Server in the network");
        }
    }


    /**
     * Opens a socket in the ipaddress and port number given as parameter
     * @param address
     * @param port
     * @return
     */
    private boolean openSocket(String address, int port) {
        logger.debug("opening socket {}:{}" , address ,port);
        try {
            socket = new Socket(address, port);
            userInputStream = new DataInputStream(System.in);
            requestMediator = new RequestMediator(socket.getOutputStream(), clientState);
            responseMediator = new ResponseMediator(socket.getInputStream(), clientState);
            System.out.println("Successfully Connected to Server...");
            return true;
        } catch (IOException ex) {
            logger.error("Error: {}", ex);
        }
        return false;
    }


    private boolean closeConnectionRequest(String msg) {
        return false;
    }


}
