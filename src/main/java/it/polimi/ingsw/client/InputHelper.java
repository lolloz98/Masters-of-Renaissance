package it.polimi.ingsw.client;

import it.polimi.ingsw.client.exceptions.*;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;

import java.util.ArrayList;

public class InputHelper {

    /**
     * creates a new CreateGameMessage if the parameters are correct
     *
     * @param numberOfPlayers string to be parsed, containing the number of players
     * @param nickname        string containing the nickname of the player
     * @return CreateGameMessage to be sent to the gameHandler if the input is correct
     * @throws NumberFormatException           if numberOfPlayers is not a number
     * @throws InvalidNumberOfPlayersException if numberOfPlayers is not between 1 and 4
     */
    public static CreateGameMessage getCreateGameMessage(String numberOfPlayers, String nickname) throws NumberFormatException, InvalidNumberOfPlayersException {
        // todo check nickname length
        int number = Integer.parseInt(numberOfPlayers);
        if (number < 1 || number > 4) {
            throw new InvalidNumberOfPlayersException();
        } else
            return new CreateGameMessage(number, nickname);
    }

    /**
     * creates a new JoinGameMessage if the parameters are correct
     *
     * @param gameIdString string to be parsed, containing the id of the game
     * @param nickname     string containing the nickname of the player
     * @return JoinGameMessage to be sent to the gameHandler if the input is correct
     * @throws NumberFormatException if gameIdString is not a number
     */
    public static JoinGameMessage getJoinGameMessage(String gameIdString, String nickname) throws NumberFormatException {
        int gameIdNumber = Integer.parseInt(gameIdString);
        return new JoinGameMessage(gameIdNumber, nickname);
    }

    /**
     * creates a new RemoveLeaderPrepMessage if the parameters are correct
     *
     * @param leader1 string containing the index of the first leader to be kept
     * @param leader2 string containing the index of the first leader to be kept
     * @throws NumberFormatException          if leader1 or leader2 are not numbers
     * @throws LeadersAlreadyPickedException  if there are 2 leaders on the board
     * @throws LeaderIndexOutOfBoundException if leader1 or leader2 are out of bound
     */
    public static RemoveLeaderPrepMessage getRemoveLeaderPrepMessage(LocalGame<?> localGame, String leader1, String leader2) throws NumberFormatException, LeaderIndexOutOfBoundException {
        if (localGame.getMainPlayer().getLocalBoard().getLeaderCards().size() == 2) {
            throw new LeadersAlreadyPickedException();
        }
        int leaderNumber1 = Integer.parseInt(leader1);
        int leaderNumber2 = Integer.parseInt(leader2);
        if (leaderNumber1 < 5 && leaderNumber1 > 0 && leaderNumber2 < 5 && leaderNumber2 > 0 && leaderNumber1 != leaderNumber2) {
            ArrayList<Integer> leadersPositions = new ArrayList<>() {{ // position of leaders to be removed
                add(1);
                add(2);
                add(3);
                add(4);
            }};
            leadersPositions.removeAll(new ArrayList<Integer>() {{
                add(leaderNumber1);
                add(leaderNumber2);
            }});
            ArrayList<Integer> leaderCardIds = new ArrayList<>(); // ids of leaders to be removed
            leaderCardIds.add(localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(leadersPositions.get(0) - 1).getId());
            leaderCardIds.add(localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(leadersPositions.get(1) - 1).getId());
            return new RemoveLeaderPrepMessage(
                    localGame.getGameId(),
                    localGame.getMainPlayer().getId(),
                    leaderCardIds
            );
        } else {
            throw new LeaderIndexOutOfBoundException();
        }
    }

    /**
     * creates a new RemoveLeaderPrepMessage if the parameters are correct
     *
     * @param resString index of the res to be picked: 1 SHIELD, 2 GOLD, 3 SERVANT, 4 ROCK
     * @throws NumberFormatException             if resString is not a number
     * @throws ResourceNumberOutOfBoundException if the number is not between 1 and 4
     */
    public static ChooseOneResPrepMessage getChooseOneResPrepMessage(LocalGame<?> localGame, String resString) throws NumberFormatException, ResourceNumberOutOfBoundException {
        int resNumber = Integer.parseInt(resString);
        if (resNumber < 5 && resNumber > 0) {
            Resource pickedRes = intToRes(resNumber);
            return new ChooseOneResPrepMessage(localGame.getGameId(), localGame.getMainPlayer().getId(), pickedRes);
        } else {
            throw new ResourceNumberOutOfBoundException();
        }
    }

    private static Resource intToRes(int resNumber) {
        switch (resNumber) {
            case 1:
                return Resource.SHIELD;
            case 2:
                return Resource.GOLD;
            case 3:
                return Resource.SERVANT;
            case 4:
                return Resource.ROCK;
            default:
                return null; // i already did the check
        }
    }

    /**
     * creates a new ActivateLeaderMessage if the parameters are correct
     *
     * @param leaderPositionString index of the leader card to be activated
     * @throws NumberFormatException          if resString is not a number
     * @throws InvalidLeaderPositionException if the leader position is not valid
     */
    public static ActivateLeaderMessage getActivateLeaderMessage(LocalGame<?> localGame, String leaderPositionString) throws NumberFormatException, InvalidLeaderPositionException {
        int leaderPositionNumber;
        leaderPositionNumber = Integer.parseInt(leaderPositionString);
        if (leaderPositionNumber > 0 && leaderPositionNumber <= localGame.getMainPlayer().getLocalBoard().getLeaderCards().size()) {
            return new ActivateLeaderMessage(localGame.getGameId(),
                    localGame.getMainPlayer().getId(),
                    localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(leaderPositionNumber - 1).getId());
        } else
            throw new InvalidLeaderPositionException();
    }

    /**
     * creates a new DiscardLeaderMessage if the parameters are correct
     *
     * @param leaderPositionString index of the leader card to be discarded
     * @throws NumberFormatException          if resString is not a number
     * @throws InvalidLeaderPositionException if the leader position is not valid
     */
    public static DiscardLeaderMessage getDiscardLeaderMessage(LocalGame<?> localGame, String leaderPositionString) throws NumberFormatException, InvalidLeaderPositionException {
        int leaderPositionNumber;
        leaderPositionNumber = Integer.parseInt(leaderPositionString);
        if (leaderPositionNumber > 0 && leaderPositionNumber <= localGame.getMainPlayer().getLocalBoard().getLeaderCards().size()) {
            return new DiscardLeaderMessage(localGame.getGameId(),
                    localGame.getMainPlayer().getId(),
                    localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(leaderPositionNumber - 1).getId());
        } else
            throw new InvalidLeaderPositionException();
    }

    /**
     * creates a new DiscardLeaderMessage if the parameters are correct
     *
     * @param indexString index of the market to be pushed, the  allowed values are A, B, C, 1, 2, 3, 4
     * @throws InvalidMarketIndexException if the leader position is not valid
     */
    public static UseMarketMessage getUseMarketMessage(LocalGame<?> localGame, String indexString) throws InvalidMarketIndexException {
        boolean onRow;
        int indexNumber;
        switch (indexString) {
            case "A":
                indexNumber = 0;
                onRow = true;
                break;
            case "B":
                indexNumber = 1;
                onRow = true;
                break;
            case "C":
                indexNumber = 2;
                onRow = true;
                break;
            case "1":
                indexNumber = 0;
                onRow = false;
                break;
            case "2":
                indexNumber = 1;
                onRow = false;
                break;
            case "3":
                indexNumber = 2;
                onRow = false;
                break;
            case "4":
                indexNumber = 3;
                onRow = false;
                break;
            default:
                throw new InvalidMarketIndexException();
        }
        return new UseMarketMessage(
                localGame.getGameId(),
                localGame.getMainPlayer().getId(),
                onRow,
                indexNumber
        );
    }
}
