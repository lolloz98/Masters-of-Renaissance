package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.server.model.game.Resource;

import java.io.IOException;
import java.util.Scanner;

public class PrepResourcesView extends View {
    private CLI cli;
    private LocalMulti localMulti;
    private Scanner input;

    public PrepResourcesView(CLI cli, LocalMulti localMulti){
        this.cli = cli;
        this.localMulti = localMulti;
        this.input = new Scanner(System.in);
        localMulti.addObserver(this);
        if(localMulti.getMainPlayerPosition() == 2 || localMulti.getMainPlayerPosition() == 3){
            System.out.println("Pick a free resource:");
            printResLists();
            boolean valid;
            Resource pickedRes = null;
            do {
                int ans = input.nextInt();
                switch (ans) {
                    case 1: pickedRes = Resource.SHIELD; valid = true; break;
                    case 2: pickedRes = Resource.GOLD; valid = true; break;
                    case 3: pickedRes = Resource.SERVANT; valid = true; break;
                    case 4: pickedRes = Resource.ROCK; valid = true; break;
                    default:
                        System.out.println("Invalid choice, try again");
                        valid = false;
                }
            } while (valid == false);
            try {
                cli.getClient().sendMessage(new ChooseOneResPrepMessage(localMulti.getGameId(), localMulti.getMainPlayerId(), pickedRes));
            } catch (IOException e) {
                System.out.println("No connection from server");
                e.printStackTrace();
            }
        }
        if(localMulti.getMainPlayerPosition() == 4){
            System.out.println("Pick the first free resource:");
            printResLists();
            boolean valid;
            Resource pickedRes1 = null;
            do {
                int ans = input.nextInt();
                switch (ans) {
                    case 1: pickedRes1 = Resource.SHIELD; valid = true; break;
                    case 2: pickedRes1 = Resource.GOLD; valid = true; break;
                    case 3: pickedRes1 = Resource.SERVANT; valid = true; break;
                    case 4: pickedRes1 = Resource.ROCK; valid = true; break;
                    default:
                        System.out.println("Invalid choice, try again:");
                        valid = false;
                }
            } while (valid == false);
            System.out.println("Pick the second free resource:");
            valid = false;
            Resource pickedRes2 = null;
            do {
                int ans = input.nextInt();
                switch (ans) {
                    case 1: pickedRes2 = Resource.SHIELD; valid = true; break;
                    case 2: pickedRes2 = Resource.GOLD; valid = true; break;
                    case 3: pickedRes2 = Resource.SERVANT; valid = true; break;
                    case 4: pickedRes2 = Resource.ROCK; valid = true; break;
                    default:
                        System.out.println("Invalid choice, try again:");
                        valid = false;
                }
            } while (valid == false);
            try {
                cli.getClient().sendMessage(new ChooseOneResPrepMessage(localMulti.getGameId(), localMulti.getMainPlayerId(), pickedRes1));
                cli.getClient().sendMessage(new ChooseOneResPrepMessage(localMulti.getGameId(), localMulti.getMainPlayerId(), pickedRes2));
            } catch (IOException e) {
                System.out.println("No connection from server");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyUpdate() {
        if(localMulti.getState() == LocalGameState.PREP_LEADERS){
            // todo load leader prep view
        }
    }

    @Override
    public void notifyError() {
        // there are no errors in this view
    }

    @Override
    public void handleCommand(String ans) {

    }

    @Override
    public void draw() {
        System.out.println("Please wait");
    }

    public void printResLists(){
        System.out.println("1. Shield");
        System.out.println("2. Gold");
        System.out.println("3. Servant");
        System.out.println("4. Rock");
    }
}
