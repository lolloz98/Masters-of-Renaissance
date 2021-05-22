package it.polimi.ingsw.client;

import it.polimi.ingsw.client.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.client.exceptions.LeaderIndexOutOfBoundException;
import it.polimi.ingsw.client.exceptions.LeadersAlreadyPickedException;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.messages.requests.leader.LeaderMessage;

import java.util.ArrayList;

public class InputHelper {

    public InputHelper() {
    }

    /**
     * creates a new CreateGameMessage if the parameters are correct
     *
     * @param numberOfPlayers string to be parsed, containing the number of players
     * @param nickname        string containing the nickname of the player
     * @return CreateGameMessage to be sent to the gameHandler if the input is correct
     * @throws NumberFormatException           if numberOfPlayers is not a number
     * @throws InvalidNumberOfPlayersException if numberOfPlayers is not between 1 and 4
     */
    public CreateGameMessage getCreateGameMessage(String numberOfPlayers, String nickname) throws NumberFormatException, InvalidNumberOfPlayersException {
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
    public JoinGameMessage getJoinGameMessage(String gameIdString, String nickname) throws NumberFormatException {
        int gameIdNumber = Integer.parseInt(gameIdString);
        return new JoinGameMessage(gameIdNumber, nickname);
    }

    /**
     * creates a new RemoveLeaderPrepMessage if the parameters are correct
     *
     * @param localGame string containing the index of the first leader to be kept
     * @param leader1   string containing the index of the first leader to be kept
     * @param leader2   string containing the index of the first leader to be kept
     * @throws NumberFormatException if leader1 or leader2 are not numbers
     * @throws LeadersAlreadyPickedException if there are 2 leaders on the board
     * @throws LeaderIndexOutOfBoundException if leader1 or leader2 are out of bound
     */
    public RemoveLeaderPrepMessage getRemoveLeaderPrepMessage(LocalGame<?> localGame, String leader1, String leader2) throws NumberFormatException, LeaderIndexOutOfBoundException {
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
            leadersPositions.removeAll(new ArrayList<Integer>(){{
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
}
