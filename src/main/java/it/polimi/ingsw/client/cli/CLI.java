package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.LocalSingleGameHandler;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.states.*;
import it.polimi.ingsw.client.cli.states.creation.JoinGameView;
import it.polimi.ingsw.client.cli.states.creation.NewMultiView;
import it.polimi.ingsw.client.cli.states.creation.NewSingleView;
import it.polimi.ingsw.client.localmodel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public class CLI extends UI {
    private static final Logger logger = LogManager.getLogger(CLI.class);
    private View<CLI> state;

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
        gameOver = false;
        this.input = new Scanner(System.in);
        setup();
        String ans;
        while (!gameOver) {
            state.draw();
            ans = input.nextLine();
            System.out.println(localGame.getState());
            state.handleCommand(ans);
        }
    }

    void setup() {
        CLIutils.clearScreen();
        boolean valid;
        System.out.println("Welcome to Masters of Renaissance");
        do {
            System.out.println("Do you want to play a local single game, or to connect to a server?");
            System.out.println("1. Play locally");
            System.out.println("2. Connect to a server\n");
            System.out.println("Enter your choice:\n");
            Scanner input = new Scanner(System.in);
            String ans = input.nextLine();
            try {
                int ansNumber = Integer.parseInt(ans);
                if (ansNumber < 1 || ansNumber > 2) {
                    System.out.println("Invalid answer, try again:");
                    valid = false;
                } else if (ansNumber == 1) {
                    valid = true;
                    gameHandler = new LocalSingleGameHandler();
                    newSinglePlayer();
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
                            System.out.println("Do you want to join a game or create a new one?");
                            System.out.println("1. Join game");
                            System.out.println("2. Create a new game\n");
                            System.out.println("Enter your choice:\n");
                            boolean valid2;
                            do {
                                ans = input.nextLine();
                                try {
                                    ansNumber = Integer.parseInt(ans);
                                    if (ansNumber < 1 || ansNumber > 2) {
                                        System.out.println("Invalid answer, try again:");
                                        valid2 = false;
                                    } else if (ansNumber == 1) {
                                        joinGame();
                                        valid2 = true;
                                    } else {
                                        choseNumberOfPlayers();
                                        valid2 = true;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid answer, try again:");
                                    valid2 = false;
                                }
                            } while (!valid2);
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
            }
        } while (!valid);
    }

    @Override
    protected void joinGame() {
        super.joinGame();
        state = new JoinGameView(this, (LocalMulti) localGame);
    }

    protected void choseNumberOfPlayers() {
        System.out.println("Type the number of players:\n");
        boolean valid;
        do {
            String ansString = input.nextLine();
            try {
                int ansNumber = Integer.parseInt(ansString);
                if (ansNumber < 1 || ansNumber > 4) {
                    System.out.println("Invalid answer, try again:");
                    valid = false;
                } else if (ansNumber == 1) {
                    newSinglePlayer();
                    valid = true;
                } else {
                    newMultiPlayer(ansNumber);
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid answer, try again:");
                valid = false;
            }
        } while (!valid);
    }

    @Override
    protected void newSinglePlayer() {
        super.newSinglePlayer();
        state = new NewSingleView(this, (LocalSingle) localGame);
        ((NewSingleView) state).launch(this);
    }

    @Override
    protected void newMultiPlayer(int numberOfPlayers) {
        super.newMultiPlayer(numberOfPlayers);
        state = new NewMultiView(this, (LocalMulti) localGame, numberOfPlayers);
    }
}
