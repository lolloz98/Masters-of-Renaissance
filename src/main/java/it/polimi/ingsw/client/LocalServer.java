package it.polimi.ingsw.client;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Persist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * LocalServer is used only for singlePlayer games which do not require connection to the internet
 */
public final class LocalServer {
    private static final Logger logger = LogManager.getLogger(LocalServer.class);

    private int port;
    private static LocalServer INSTANCE = null;

    public static LocalServer getInstance() {
        if (INSTANCE == null) return INSTANCE = new LocalServer();
        else return INSTANCE;
    }

    private LocalServer() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(0);
        } catch (IOException e) {
            logger.error("Cannot find a port: " + e.getMessage());
            System.exit(1);
            return;
        }
        port = socket.getLocalPort();
        new Thread(() -> Server.run_server(socket)).start();
    }

    public int getPort() {
        return port;
    }
}
