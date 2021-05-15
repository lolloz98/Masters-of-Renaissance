package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;

public class PrepResFirstView extends View {
    private CLI cli;
    private LocalMulti localMulti;

    public PrepResFirstView(CLI cli, LocalMulti localMulti) {
        this.cli = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
    }

    @Override
    public void notifyUpdate() {
        if(localMulti.getState() == LocalGameState.PREP_LEADERS){
            localMulti.removeObserver();
            cli.setState(new PrepLeaderView(cli, localMulti));
            cli.getState().draw();
        }
    }

    @Override
    public void notifyError() {

    }

    @Override
    public void handleCommand(String ans) {

    }

    @Override
    public void draw() {
        // todo inform player of the order
        System.out.println("Please wait");
    }
}
