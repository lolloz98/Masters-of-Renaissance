package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.requests.CreateGameMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private static final Logger logger = LogManager.getLogger(Client.class);

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        Socket server;
        try {
            // FIXME: how do we know the server port? -> in prof's project it said not to put constant the number of the port.
            logger.debug("server port: " + Server.PORT);
            server = new Socket(ip, Server.PORT);
        } catch (IOException e) {
            logger.error("Server unreachable: " + e.getMessage());
            return;
        }
        logger.info("client connected");

        try {
            ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(server.getInputStream());

            // todo: handle the requests and the answers. Decouple the behaviour create a "controller" to do so
            System.out.println("input a number, -1 to exit: ");
            int number = 0;
            while (number != -1) {
                number = Integer.parseInt(scanner.nextLine());
                // todo modify
                output.writeObject(new CreateGameMessage(number, "Lollo"));
                Answer answerMsg = (Answer)input.readObject();
                System.out.println(answerMsg);
            }
        } catch (IOException e) {
            logger.error("server has died");
        } catch (ClassNotFoundException e) {
            logger.error("failed parsing data from server: " + e.getMessage());
        }

        try {
            server.close();
        } catch (IOException e) {
            logger.warn("Error happened while closing a connection: " + e.getMessage());
        }
    }
}
