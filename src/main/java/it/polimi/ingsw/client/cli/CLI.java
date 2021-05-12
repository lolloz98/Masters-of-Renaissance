package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.states.*;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.messages.requests.CreateGameMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI extends UI implements Runnable {
    private LocalGame localGame;
    private View state;
    private boolean gameOver;
    private Scanner input;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private Client client;

    public LocalGame getLocalGame() {
        return localGame;
    }

    public void setLocalGame(LocalGame localGame) {
        this.localGame = localGame;
    }
    public View getState() {
        return state;
    }

    public void setState(View state) {
        this.state = state;
    }

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run();
    }

    @Override
    public void run(){
        this.input = new Scanner(System.in);
        setup();
        while(!gameOver){
            state.draw();
            state.handleCommand(input.nextInt());
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
            } else {
                input.nextLine(); // needed to use nextLine() after nextInt()
                try {
                    System.out.println("Enter server ip");
                    String ip = input.nextLine();
                    System.out.println("Enter server port");
                    int port = input.nextInt();
                    setClient(new Client(ip, port));
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
                            LocalMulti localMulti = new LocalMulti();
                            state = new JoinGameView(this, localMulti);
                            this.localGame = localMulti;
                            valid2 = true;
                        } else {
                            // choice for number of players
                            System.out.println("Type the number of players:\n");
                            boolean valid3;
                            int ans;
                            do {
                                ans = input.nextInt();
                                if (ans < 1 || ans > 4) {
                                    System.out.println("Invalid answer, try again:");
                                    valid3 = false;
                                } else if (ans == 1) {
                                    LocalSingle localSingle = new LocalSingle();
                                    state = new NewSingleView(this, localSingle);
                                    localGame = localSingle;
                                    valid3 = true;
                                } else {
                                    LocalMulti localMulti = new LocalMulti();
                                    state = new NewMultiView(this, localMulti, ans);
                                    localGame = localMulti;
                                    valid3 = true;
                                }
                            } while (valid3 == false);
                            valid2 = true;
                        }
                    } while (valid2 == false);
                } catch(IOException e){
                    System.out.println("error connecting to the server, try again");
                    valid = false;
                }
            }
        } while (valid == false);
        // todo initiate server handler passing localgame
    }

    public static void clearScreen() {
        /* for windows cmd
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.print(CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE);
    }

/*    void handleCommand(String line){
        switch(line) {
            case "end":
                gameOver = true;
                break;
            case "market":
                state = new MarketView(localMarket, localGame);
                break;
            case "develop":
                state = new DevelopmentGridView(localDevelopmentGrid, localGame);
                break;
            case "board 0":
                state = new BoardView(localBoards.get(0), localGame);
                break;
            case "board 1":
                state = new BoardView(localBoards.get(1), localGame);
                break;
            case "board 2":
                if(localGame.getNumberOfPlayers() < 3){
                    System.out.println("there is no player 2");
                }
                else state = new BoardView(localBoards.get(2), localGame);
                break;
            case "board 3":
                if(localGame.getNumberOfPlayers() < 3) {
                    System.out.println("there is no player 3");
                }
                else state = new BoardView(localBoards.get(3), localGame);
                break;
            default:
                // case for state-specific commands
                state.handleCommand(line);
        }
    } */

    public static void print(ArrayList<String> out){
        for(String o : out){
            System.out.println(o);
        }
    }
}
