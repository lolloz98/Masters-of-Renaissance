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
    private boolean gameOver;
    private Scanner input;
    private LocalMarket localMarket;
    private LocalGame localGame;
    private LocalDevelopmentGrid localDevelopmentGrid;

    public View getState() {
        return state;
    }

    public void setState(View state) {
        this.state = state;
    }

    private View state;

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run();
    }

    @Override
    public void run(){
        setup();
        // todo: delete this, is just to simulate someone modifying the market
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                localBoards.get(0).setFaithTrackScore(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(6000);
                localGame.setCurrentPlayerId(1);
                Thread.sleep(6000);
                localGame.setCurrentPlayerId(2);
                Thread.sleep(6000);
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

    public void setup(){
        localGame = new LocalGame(this);
        localMarket = new LocalMarket(this);
        localDevelopmentGrid = new LocalDevelopmentGrid(this);
        localBoards = new ArrayList<>();
        this.localBoards.add(new LocalBoard(this));
        state = new BoardView(localBoards.get(0), localGame);
        state.draw();
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void notifyAction(LocalModelAbstract localModelAbstract){
        state.notifyAction(localModelAbstract);
    }

    private void handleCommand(String line){
        switch(line) {
            case "end":
                gameOver = true;
                break;
            case "market":
                state = new MarketView(localMarket, localGame);
                state.draw();
                break;
            case "develop":
                state = new DevelopmentGridView(localDevelopmentGrid, localGame);
                state.draw();
                break;
            default:
                System.out.println("not valid");
        }
    }
}
