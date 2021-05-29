package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.LocalSingleGameHandler;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.states.*;
import it.polimi.ingsw.client.cli.states.creation.JoinGameView;
import it.polimi.ingsw.client.cli.states.creation.NewMultiView;
import it.polimi.ingsw.client.cli.states.creation.NewSingleView;
import it.polimi.ingsw.client.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public class CLI extends UI {
    private static final Logger logger = LogManager.getLogger(CLI.class);
    private View<CLI> state;
    private String nickname;

    public View<CLI> getState() {
        return state;
    }

    public void setState(View<CLI> state) {
        this.state = state;
    }

    public static void main(String[] args) {
        logger.debug("CLI Started");
        CLI cli = new CLI();
        cli.run();
    }

    public void run() {
        CLIutils.clearScreen();
        System.out.println("Welcome to Masters of Renaissance");
        quit = false;
        this.input = new Scanner(System.in);
        setup();
        String ans;
        while (!quit) {
            state.draw();
            ans = input.nextLine();
            state.handleCommand(ans);
        }
        // todo close socket
    }

    private void setup() {
        boolean valid;
        input = new Scanner(System.in);
        System.out.println("");
        System.out.println("");
        pickNickname();
        do {
            System.out.println("Do you want to play a local single game, or to connect to a server?");
            System.out.println("1. Play locally");
            System.out.println("2. Connect to a server\n");
            System.out.println("Enter your choice:\n");
            String ans = input.nextLine();
            try {
                int ansNumber = Integer.parseInt(ans);
                if (ansNumber < 1 || ansNumber > 2) {
                    System.out.println("Invalid answer, try again:");
                    valid = false;
                } else if (ansNumber == 1) {
                    valid = true;
                    gameHandler = new LocalSingleGameHandler();
                    CreateGameMessage createGameMessage = InputHelper.getCreateGameMessage("1", nickname);
                    newSinglePlayer();
                    gameHandler.dealWithMessage(createGameMessage);
                } else {
                    try {
                        System.out.println("Enter server ip");
                        String ip = input.nextLine();
                        System.out.println("Enter server port");
                        String portString = input.nextLine();
                        try {
                            int portNumber = Integer.parseInt(portString);
                            setServerListener(ip, portNumber);
                            valid = true;
                            // choice for join or create game
                            joinOrCreate();
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid answer, try again:");
                            valid = false;
                        }
                    } catch (IOException e) {
                        System.out.println("error connecting to the server, try again");
                        valid = false;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid answer, try again:");
                valid = false;
            } catch (InvalidNumberOfPlayersException | IOException e) {
                e.printStackTrace();
                valid = false; // actually never happens
            }
        } while (!valid);
    }

    @Override
    protected void joinGame() {
        super.joinGame();
        state = new JoinGameView(this, (LocalMulti) localGame, nickname);
    }

    protected void choseNumberOfPlayers() {
        System.out.println("Type the number of players:\n");
        String numberOfPlayers;
        boolean valid;
        do {
            try {
                numberOfPlayers = input.nextLine();
                CreateGameMessage createGameMessage = InputHelper.getCreateGameMessage(numberOfPlayers, nickname);
                if (createGameMessage.getPlayersNumber() == 1) {
                    newSinglePlayer();
                } else {
                    newMultiPlayer();
                }
                try {
                    gameHandler.dealWithMessage(createGameMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid answer, try again:");
                valid = false;
            } catch (InvalidNumberOfPlayersException e) {
                System.out.println("Invalid number of players, try again:");
                valid = false;
            }
        } while (!valid);
    }

    @Override
    public void newSinglePlayer() {
        super.newSinglePlayer();
        state = new NewSingleView(this, (LocalSingle) localGame);
    }

    public void newMultiPlayer() {
        super.newMultiPlayer();
        state = new NewMultiView(this, (LocalMulti) localGame);
    }

    /**
     * asks the player if he wants to create a new game or join an already existing one
     */
    private void joinOrCreate() {
        System.out.println("Do you want to join a game or create a new one?");
        System.out.println("1. Join game");
        System.out.println("2. Create a new game\n");
        System.out.println("Enter your choice:\n");
        boolean valid;
        String ans;
        int ansNumber;
        do {
            ans = input.nextLine();
            try {
                ansNumber = Integer.parseInt(ans);
                if (ansNumber < 1 || ansNumber > 2) {
                    System.out.println("Invalid answer, try again:");
                    valid = false;
                } else if (ansNumber == 1) {
                    joinGame();
                    System.out.println("Insert the id of the game you want to join:");
                    ans = input.nextLine();
                    state.handleCommand(ans);
                    valid = true;
                } else {
                    choseNumberOfPlayers();
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid answer, try again:");
                valid = false;
            }
        } while (!valid);
    }

    private void pickNickname() {
        System.out.println("Insert your nickname");
        boolean valid;
        do {
            String nameTemp = input.nextLine();
            if (nameTemp.length() < 1) {// todo set max length
                System.out.println("Invalid nickname, retry:");
                valid = false;
            } else {
                nickname = nameTemp;
                valid = true;
            }
        } while (!valid);
    }
}
