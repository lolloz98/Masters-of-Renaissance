package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

import java.io.IOException;

public class JoinGameView extends View<CLI> {
    private final LocalMulti localMulti;
    private final String nickname;
    private boolean valid;

    public JoinGameView(CLI cli, LocalMulti localMulti, String nickname) {
        this.ui = cli;
        this.localMulti = localMulti;
        this.nickname = nickname;
        localMulti.overrideObserver(this);
        localMulti.getError().addObserver(this);
        valid = true;
    }

    @Override
    public synchronized void notifyUpdate() {
        if (localMulti.getState() == LocalGameState.PREP_LEADERS) {
            localMulti.removeObservers();
            localMulti.getError().removeObserver();
            ui.setState(new BoardView(ui, localMulti, localMulti.getMainPlayer()));
            ui.getState().draw();
        } else draw();
    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localMulti.getError().getErrorMessage());
        System.out.println("Insert another id:");
    }

    @Override
    public synchronized void handleCommand(String ans) {
        try {
            JoinGameMessage joinGameMessage = InputHelper.getJoinGameMessage(ans, nickname);
            ui.getGameHandler().dealWithMessage(joinGameMessage);
            valid = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id, try again:");
            valid = false;
        }
    }

    @Override
    public synchronized void draw() {
        if (valid) {
            if (localMulti.getState() == LocalGameState.NEW) {
                System.out.println("Please wait");
            } else if (localMulti.getState() == LocalGameState.WAITING_PLAYERS) {
                System.out.println("The id of the game is\n" + localMulti.getGameId());
                System.out.println("Players currently connected:");
                for (LocalPlayer p : localMulti.getLocalPlayers()) {
                    System.out.println(p.getName());
                }
            }
        }
    }
}