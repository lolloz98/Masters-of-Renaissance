package it.polimi.ingsw.client.localmodel;

public class Error extends Observable{
    private String errorMsg;
    /**
     * if errorId = 0 there are no errors
     */
    private int errorId;

    public synchronized String getErrorMsg() {
        return errorMsg;
    }

    public synchronized void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        notifyObserver();
    }

    public synchronized void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public synchronized int getErrorId() {
        return errorId;
    }

    public Error(){
        errorId = 0;
    }
}
