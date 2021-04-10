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
     * @param game
     * @return null if the game is over, otherwise returns the next turn.
     */
    @Override
    public TurnSingle nextTurn(Game<? extends Turn> game){
        SinglePlayer singlePlayer = (SinglePlayer) game;
        singlePlayer.checkEndConditions();
        if (singlePlayer.isLastTurn()) return null;
        else {
            if (lorenzoPlaying) {
                return new TurnSingle(false);
            }
            else {
                return new TurnSingle(true);
            }
        }
    }
}
