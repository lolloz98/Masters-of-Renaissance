package it.polimi.ingsw.client.localmodel;

public class Error extends Observable{
    String errorMsg;

    public synchronized String getErrorMsg() {
        return errorMsg;
    }

    public synchronized void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Error(){
    }
}
