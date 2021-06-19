package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ControllerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Server
{
    private static final Logger logger = LogManager.getLogger(Server.class);

    private static int port = 16509;

    private static final int maxPort = 65535;

    public static int getPort(){
        return port;
    }

    /**
     * parse port from arg
     * @param arg string from which to get the port
     */
    private static void portFromArg(String arg){
        try {
            port = Integer.parseInt(arg);
            if(port <= 0 || port > maxPort){
                throw new IllegalArgumentException();
            }
        }catch (IllegalArgumentException e){
            boolean isPortInvalid = true;
            Scanner in = new Scanner(System.in);
            while(isPortInvalid){
                System.out.println("The argument passed is not an integer (or it is out of bound [1 - "+ maxPort+"]). \nPlease input the port to which you would like to connect (-1 for default): ");
                try{
                    port = Integer.parseInt(in.next());
                    if(port == -1){
                        port = 16509;
                        isPortInvalid = false;
                    }
                    if(port >= 1 && port <= maxPort){
                        isPortInvalid = false;
                    }
                }
                catch (IllegalArgumentException ignore){
                    logger.debug("IllegalArgumentException exception launched");
                }
            }
        }
    }

    public static void main(String[] args)
    {
        // init controller manager
        ControllerManager.getInstance();
        ServerSocket socket;

        if(args.length == 1){
            portFromArg(args[0]);
        }

        try {
            socket = new ServerSocket(port);
            logger.info("Server listening on port: " + port);
        } catch (IOException e) {
            logger.error("Cannot connect to selected port: " + e.getMessage());
            System.exit(1);
            return;
        }
        run_server(socket);
    }

    public static void run_server(ServerSocket socket){
        while (true) {
            try {
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                logger.error("Connection dropped: " + e.getMessage());
            }
        }
    }
}
