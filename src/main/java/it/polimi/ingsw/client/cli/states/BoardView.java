package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;

public class BoardView extends GameView {
    private LocalPlayer localPlayer;
    private CLI cli;

    public BoardView(CLI cli, LocalGame localGame, LocalPlayer localPlayer){
        this.localGame = localGame;
        this.cli = cli;
        this.localPlayer = localPlayer;
        // todo add observer
        localGame.getError().addObserver(this);
    }

    @Override
    public void draw(){
        CLI.clearScreen();
        // todo make this good looking
        System.out.println(localPlayer.getName() + "'s board:");
        System.out.println("");
        System.out.println("Resources in depot:" + localPlayer.getLocalBoard().getResInNormalDeposit());
        System.out.println("Resources in box:" + localPlayer.getLocalBoard().getResInStrongBox());
        System.out.println("Faith points:" + localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore());
        super.drawTurn();
    }

    @Override
    public void notifyUpdate(){
        draw();
    }

    @Override
    public void notifyError() {}

    @Override
    public void handleCommand(int ans){
        switch (ans){
            // todo handle activate production (only if loadBoard.getPlayerId() == playerId)
            default:
                System.out.println("not valid");
        }
    }
}
