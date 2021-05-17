package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LocalServer;
import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.states.*;
import it.polimi.ingsw.client.cli.states.creation.JoinGameView;
import it.polimi.ingsw.client.cli.states.creation.NewMultiView;
import it.polimi.ingsw.client.cli.states.creation.NewSingleView;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI extends UI implements Runnable {
    private static final Logger logger = LogManager.getLogger(CLI.class);

    private LocalGame<?> localGame;
    private View state;
    private boolean gameOver;
    private Scanner input;

    public ServerListener getServerListener() {
        return serverListener;
    }

    private ServerListener serverListener;

    public LocalGame<?> getLocalGame() {
        return localGame;
    }

    public void setLocalGame(LocalGame<?> localGame) {
        this.localGame = localGame;
    }

    public View getState() {
            return state;
    }

    public void setState(View state) {
            this.state = state;
    }

    public static void main(String[] args) {
        logger.debug("CLI Started");
        CLI cli = new CLI();
        cli.run();
    }

    @Override
    public void run(){
        gameOver = false;
        this.input = new Scanner(System.in);
        setup();
        String ans;
        while(!gameOver){
            state.draw();
            ans = input.nextLine();
            state.handleCommand(ans);
        }
    }

    void setup(){
        clearScreen();
        boolean valid;
        System.out.println("Welcome to Masters of Renaissance");
        do{
            System.out.println("Do you want to play a local single game, or to connect to a server?");
            System.out.println("1. Play locally");
            System.out.println("2. Connect to a server\n");
            System.out.println("Enter your choice:\n");
            Scanner input = new Scanner(System.in);
            int choice;
            choice = input.nextInt();
            if (choice<1 || choice>2) {
                System.out.println("Invalid answer, try again:");
                valid = false;
            }
            else if (choice == 1) {
                // todo when implemented local single player
                valid = true;
                int port = LocalServer.getInstance().getPort();
                try {
                    serverListener = new ServerListener("localhost", port);
                    new Thread(serverListener).start();
                    newSinglePlayer();
                }catch (IOException e){
                    logger.error("error connecting to localhost, port: " + port);
                    System.out.println("Error creating a new SinglePlayer locally");
                    valid = false;
                }
            } else {
                input.nextLine(); // needed to use nextLine() after nextInt()
                try {
                    System.out.println("Enter server ip");
                    String ip = input.nextLine();
                    System.out.println("Enter server port");
                    int port = input.nextInt();
                    serverListener = new ServerListener(ip, port);
                    new Thread(serverListener).start();
                    valid = true;
                    // choice for join or create game
                    System.out.println("Do you want to join a game or create a new one?");
                    System.out.println("1. Join game");
                    System.out.println("2. Create a new game\n");
                    System.out.println("Enter your choice:\n");
                    int choice2;
                    boolean valid2;
                    do {
                        choice2 = input.nextInt();
                        if (choice2 < 1 || choice2 > 2) {
                            System.out.println("Invalid answer, try again:");
                            valid2 = false;
                        } else if (choice2 == 1) {
                            joinGame();
                            valid2 = true;
                        } else {
                            choseNumberOfPlayers();
                            valid2 = true;
                        }
                    } while (!valid2);
                } catch(IOException e){
                    System.out.println("error connecting to the server, try again");
                    valid = false;
                }
            }
        } while (!valid);
    }

    private void joinGame() {
        LocalMulti localMulti = new LocalMulti();
        this.localGame = localMulti;
        serverListener.setLocalGame(localGame);
        state = new JoinGameView(this, localMulti);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.print(CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE);
    }

    public static void print(ArrayList<String> out){
        for(String o : out){
            System.out.println(o);
        }
    }

    private void choseNumberOfPlayers(){
        System.out.println("Type the number of players:\n");
        boolean valid;
        int ans;
        do {
            ans = input.nextInt();
            if (ans < 1 || ans > 4) {
                System.out.println("Invalid answer, try again:");
                valid = false;
            } else if (ans == 1) {
                newSinglePlayer();
                valid = true;
            } else {
                LocalMulti localMulti = new LocalMulti();
                localGame = localMulti;
                serverListener.setLocalGame(localGame);
                state = new NewMultiView(this, localMulti, ans);
                valid = true;
            }
        } while (!valid);
    }

    public void newSinglePlayer(){
        LocalSingle localSingle = new LocalSingle();
        localGame = localSingle;
        serverListener.setLocalGame(localGame);
        state = new NewSingleView(this, localSingle);
    }
}
