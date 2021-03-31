package it.polimi.ingsw.model.game;

public class TurnSingle extends Turn{
    private final boolean lorenzoPlaying;

    public TurnSingle(boolean lorenzoPlaying) {
        super();
        this.lorenzoPlaying = lorenzoPlaying;
    }

    public boolean isLorenzoPlaying() {
        return lorenzoPlaying;
    }

    /**
     * Method that computes the next turn.
     *
     * @return null if the game is over, otherwise returns the next turn.
     */
    @Override
    public TurnSingle nextTurn(Game game){
        SinglePlayer singlePlayer = (SinglePlayer) game;
        singlePlayer.checkEndConditions();
        if (singlePlayer.isLastTurn()) return null;
        else {
            if (lorenzoPlaying) return new TurnSingle(false);
            else return new TurnSingle(true);
        }
    }

    public void performLorenzoAction(SinglePlayer singlePlayer){
        //TODO when player package is completed
    }
}
