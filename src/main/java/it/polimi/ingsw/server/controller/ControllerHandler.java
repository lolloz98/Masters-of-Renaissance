package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.exception.PlayersOutOfBoundException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.requests.CreateGameMessage;
import it.polimi.ingsw.server.model.game.GameManager;

/**
 * class that handles the creations of the model with his associated controller
 */
public class ControllerHandler {
    private static ControllerHandler instance=null;
    private final GameManager gameManager=GameManager.getInstance();
    /**
     * treemap containing the id of the game and the ControllerActions associated
     */
    //private TreeMap<Integer,ControllerActions> controllerActionsTreeMap;

   private void ControllerHandler(){
       //controllerActionsTreeMap=new TreeMap<>();
   }

    /**
     * @return the only instance of ControllerHandler
     */
    public static synchronized ControllerHandler getInstance(){
        if (instance == null) instance = new ControllerHandler();
        return instance;
    }

    /**
     * method that handle the request of creation a new game
     * @param message message by the first player
     * @return ControllerActions of the game
     */
    public synchronized ControllerActions createGame(CreateGameMessage message) throws ControllerException {
        int id;
        try{
            id=gameManager.reserveId(message.getPlayersNumber(), message.getUserName());
        }catch(PlayersOutOfBoundException e){
            //todo
            throw new ControllerException();
        }

        if(message.getPlayersNumber()==1){
            SinglePlayer game= (SinglePlayer) gameManager.getGameFromMap(id);
            ControllerActions controllerActions=new ControllerActions(game,id, this.gameManager);
            //controllerActionsTreeMap.put(id,controllerActions);
            return controllerActions;
        }
        else{
            ControllerActions controllerActions=new ControllerActions(this.gameManager);
            return controllerActions;
        }
    }
}
