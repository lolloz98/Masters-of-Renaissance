package it.polimi.ingsw.client;

import it.polimi.ingsw.client.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

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
     * @param nickname        string containing the nickname of the player
     * @return JoinGameMessage to be sent to the gameHandler if the input is correct
     * @throws NumberFormatException           if gameIdString is not a number
     */
    public JoinGameMessage getJoinGameMessage(String gameIdString, String nickname) throws NumberFormatException {
        int gameIdNumber = Integer.parseInt(gameIdString);
        return new JoinGameMessage(gameIdNumber, nickname);
    }
}
