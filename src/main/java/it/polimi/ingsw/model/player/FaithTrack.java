package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.model.exception.InvalidStepsExeption;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.SinglePlayer;

/**
 *class that models the faithpath of each player(including Leonardo)
 */
public class FaithTrack implements VictoryPointCalculator {
    private final VaticanFigure[] figures;
    private int position;


    public FaithTrack() {
        this.position=0;
        this.figures= new VaticanFigure[3];
        this.figures[0]=new VaticanFigure(2);
        this.figures[1]=new VaticanFigure(3);
        this.figures[2]=new VaticanFigure(4);
    }

    public int getPosition() {
        return position;
    }

    public VaticanFigure[] getFigures() {
        return figures;
    }

    @Override
    public int getVictoryPoints(){
        int points=0;
        for(int i=0;i<3;i++){
            if(figures[i].isActive())
                points+=figures[i].getLevel();
        }
        if(position<3)
            return points;
        else if(position<6)
                return points+1;
        else if(position<9)
            return points+2;
        else if(position<12)
            return points+4;
        else if(position<15)
            return points+6;
        else if(position<18)
            return points+9;
        else if(position<21)
            return points+12;
        else if(position<24)
            return points+16;
        else if(position==24)
            return points+20;
        return points;
    }

    /**
     * method that moves the player through the path and handle the vaticanfigures activation
     */
    public void move(int steps, Game game){
        advance(steps);
        if(iHaveToHandleChecks(steps)){
            if(game instanceof MultiPlayer)
                checkpointHandling(steps,(MultiPlayer) game);
            else
                checkpointHandling(steps,(SinglePlayer) game);
        }

    }

    /**
     * method that @returns true if i have reached a checkpoint and if i'm actually the first that has reached
     * that checkpoint
     */
    private boolean iHaveToHandleChecks(int steps){
        int checkpointnumber = whichCheckpointIsReached(steps);
        return checkpointnumber!=-1&&amiTheFirst(checkpointnumber);
    }

    /**
     * method that activate the VaticanFigures of the player that has the rights (in a single player game)
     */
    private void checkpointHandling(int steps, SinglePlayer game){
        int checkpointnumber = whichCheckpointIsReached(steps);
            if(game.getTurn().isLorenzoPlaying()){
                game.getLorenzo().getFaithTrack().activateVatican(checkpointnumber);
            }
            else{
                game.getPlayer().getBoard().getFaithtrack().activateVatican(checkpointnumber);
            }
        }


    /**
     * method that activate the VaticanFigures of the player that has the rights (in a multi player game)
     */
    private void checkpointHandling(int steps, MultiPlayer game){
        int checkpointnumber = whichCheckpointIsReached(steps);
            for (Player p:
                    game.getPlayers()) {
                if(p.getBoard().getFaithtrack().hasRights(checkpointnumber))
                    p.getBoard().getFaithtrack().activateVatican(checkpointnumber);
                else
                    p.getBoard().getFaithtrack().discardVatican(checkpointnumber);
            }
        }


    /**
     * method that advance the piece of n-steps
     */
    private void advance(int n){
        if(n<=0) throw new InvalidStepsExeption();
        if(position+n<=24)
            this.position+=n;
        else
            this.position=24;
    }

    public boolean isEndReached(){
        return position == 24;
    }


    /**
     * method that checks if the player has reached the checkpoint
     * @return -1 if no checkpoint is reached, or returns the number of the checkpoint reached
     */
    private int whichCheckpointIsReached(int steps){
        int oldposition=position-steps;

        if(oldposition<24&&position>=24)
            return 3;

        if(oldposition<16&&position>=16)
            return 2;

        if(oldposition<8&&position>=8)
            return 1;

        return -1;
    }

    /**
     * method that checks if the player has the rights to activate the vaticanfigure
     * @param checkpointnumber is the number of the checkpoint which i'm analyzing
     */
    private boolean hasRights(int checkpointnumber) {
        switch (checkpointnumber) {
            case 1: {
                if (this.position>=5)
                return true;
            }

            case 2:{
                if (this.position>=12)
                    return true;
            }

            case 3 :{
                if (this.position>=19)
                    return true;
            }

            default:
                return false;
        }
    }


    /**method that checks if the current player is the first that has reached the checkpoint
    * @param whichcheckpoint is the number of the checkpoint to control
    */
    private boolean amiTheFirst(int whichcheckpoint){
        return figures[whichcheckpoint-1].isInactive();
    }

    /**method that activates the vatican figure*/
    private void activateVatican( int whichvf){
        this.figures[whichvf-1].activate();
    }

    /**method that discards the vatican figure*/
    private void discardVatican(int whichvf) {
        this.figures[whichvf - 1].discard();
    }

}
