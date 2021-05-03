package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.states.BoardView;
import it.polimi.ingsw.client.cli.states.DevelopmentGridView;
import it.polimi.ingsw.client.cli.states.MarketView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI extends UI implements Runnable {
    private ArrayList<LocalBoard> localBoards;
    private LocalMarket localMarket;
    private LocalGame localGame;
    private LocalDevelopmentGrid localDevelopmentGrid;
    private View state;
    private boolean gameOver;
    private Scanner input;

    protected ArrayList<LocalBoard> getLocalBoards() {
        return localBoards;
    }

    protected LocalMarket getLocalMarket() {
        return localMarket;
    }

    protected LocalGame getLocalGame() {
        return localGame;
    }

    protected LocalDevelopmentGrid getLocalDevelopmentGrid() {
        return localDevelopmentGrid;
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
        setup();
        state = new BoardView(localBoards.get(0), localGame); // fixme default state, to be removed
        // todo: delete this, is just to simulate someone modifying the market
        new Thread(() -> {
            try {
                Thread.sleep(7000);
                localBoards.get(0).setFaithTrackScore(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                localGame.setCurrentPlayerId(1);
                Thread.sleep(10000);
                localGame.setCurrentPlayerId(2);
                Thread.sleep(10000);
                localGame.setCurrentPlayerId(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        this.input = new Scanner(System.in);
        while(!gameOver){
            handleCommand(input.nextLine());
        }
    }

    void setup(){
        /* for windows cmd
        try {
            new ProcessBuilder("cmd", "/c", "mode con:cols=120 lines=40").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        localGame = new LocalGame(this);
        localMarket = new LocalMarket(this);
        localDevelopmentGrid = new LocalDevelopmentGrid(this);
        localBoards = new ArrayList<>();
        this.localBoards.add(new LocalBoard(this));
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

    @Override
    public void notifyAction(LocalModelAbstract localModelAbstract){
        state.notifyAction(localModelAbstract);
    }

    void handleCommand(String line){
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
    }

    public static void print(ArrayList<String> out){
        for(String o : out){
            System.out.println(o);
        }
    }
}
