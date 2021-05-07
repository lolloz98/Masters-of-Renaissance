package it.polimi.ingsw.messages.answers;

public class ErrorAnswer extends Answer{
    private static final long serialVersionUID = 48L;

    private final String message;

    public ErrorAnswer(String message){
        this.message = (message == null)? "": message;
    }

    public String getMessage() {
        return message;
    }
}
