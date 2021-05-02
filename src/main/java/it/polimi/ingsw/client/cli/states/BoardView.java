package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalModelAbstract;

public class BoardView extends View{
    private LocalBoard localBoard;

    public BoardView(LocalBoard localBoard, LocalGame localGame){
        this.localBoard = localBoard;
        this.localGame = localGame;
        draw();
    }

    @Override
    public void draw(){
        CLI.clearScreen();
        // todo make this good looking
        System.out.println(localBoard.getPlayerName() + "'s board:");
        System.out.println("");
        System.out.println("Resources in depot:" + localBoard.getResInNormalDeposit());
        System.out.println("Resources in box:" + localBoard.getResInStrongBox());
        System.out.println("Faith points:" + localBoard.getFaithTrackScore());
        super.drawTurn();
    }

    @Override
    public void notifyAction(LocalModelAbstract localModelAbstract){
        if (localModelAbstract == this.localBoard || localModelAbstract instanceof LocalGame) {
            draw();
        }
    }

    @Override
    public void handleCommand(String line){
        switch (line){
            // todo handle activate production (only if loadBoard.getPlayerId() == playerId)
            default:
                System.out.println("not valid");
        }
    }
}
