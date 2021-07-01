package it.polimi.ingsw.messages.answers;

public class ErrorAnswer extends Answer{
    private static final long serialVersionUID = 44L;
    /**
     * error message to print
     */
    private final String message;

    public ErrorAnswer(int gameId, int playerId, String message){
        super(gameId, playerId);
        this.message = (message == null)? "": message;
    }

    public String getMessage() {
        return message;
    }
}
