package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.enums.Resource;

import java.io.IOException;
import java.util.TreeMap;

public class PrepResSecondView extends View {
    private CLI cli;
    private LocalMulti localMulti;
    private LocalBoard localBoard;
    boolean picked;

    public PrepResSecondView(CLI cli, LocalMulti localMulti, LocalBoard localBoard) {
        this.cli = cli;
        this.localMulti = localMulti;
        this.localBoard = localBoard;
        this.localBoard.addObserver(this);
        this.localMulti.addObserver(this);
        this.localMulti.getError().addObserver(this);
        picked = false;
    }

    @Override
    public void notifyUpdate() {
        if(localMulti.getState() == LocalGameState.PREP_LEADERS){
            localMulti.removeObserver();
            localBoard.removeObserver();
            localMulti.getError().removeObserver();
            cli.setState(new PrepLeaderView(cli, localMulti));
            cli.getState().draw();
        }
    }

    @Override
    public void notifyError() {
        System.out.println(localMulti.getError().getErrorMessage());
        picked = false;
    }

    @Override
    public void handleCommand(String ans) {
        if(!picked){
            Resource pickedRes = null;
            switch (ans) {
                case "1": pickedRes = Resource.SHIELD; break;
                case "2": pickedRes = Resource.GOLD; break;
                case "3": pickedRes = Resource.SERVANT; break;
                case "4": pickedRes = Resource.ROCK; break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
            if(pickedRes != null) {
                try {
                    picked = true;
                    cli.getServerListener().sendMessage(new ChooseOneResPrepMessage(localMulti.getGameId(), localMulti.getMainPlayerId(), pickedRes));
                } catch (IOException e) {
                    System.out.println("No connection from server");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void draw() {
        if(!picked){
            // todo inform player of the order
            System.out.println("Pick a free resource:");
            System.out.println("1. Shield");
            System.out.println("2. Gold");
            System.out.println("3. Servant");
            System.out.println("4. Rock");
        }
        else {
            System.out.println("Please wait");
        }
    }

    private static void addResToTreeMap(TreeMap<Resource, Integer> toBeAdded, TreeMap<Resource, Integer> whereToAdd){
        for(Resource r: toBeAdded.keySet()){
            if(whereToAdd.containsKey(r)){
                whereToAdd.replace(r, whereToAdd.get(r) + toBeAdded.get(r));
            }else{
                whereToAdd.put(r, toBeAdded.get(r));
            }
        }
    }
}
