package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.exceptions.LeaderIndexOutOfBoundException;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.messages.requests.leader.RemoveLeaderMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveLeadersGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(RemoveLeadersGUI.class);

    public ImageView leader1;
    public ImageView leader2;
    public ImageView leader3;
    public ImageView leader4;
    private final List<ImageView> leaders = new ArrayList<>();

    public boolean[] selected = {false, false, false, false};
    int count = 0;
    public Button doneBtn;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().getMainPlayer().getLocalBoard().overrideObserver(this);
        leaders.add(leader1);
        leaders.add(leader2);
        leaders.add(leader3);
        leaders.add(leader4);
        ArrayList<LocalCard> cards = ui.getLocalGame().getMainPlayer().getLocalBoard().getLeaderCards();
        for(int i = 0; i < 4; i++){
            int j = i;
            leaders.get(i).setImage(cards.get(i).getImage());
            leaders.get(i).setOnMouseClicked(mouseEvent -> onImageClicked(j));
        }
        doneBtn.setOnMouseClicked(mouseEvent -> {
            Integer[] indexes = {0, 0};
            int ind = 0;
            for(int i = 0; i < 4; i++){
                if(selected[i]) {
                    indexes[ind] = i + 1;
                    ind++;
                }
            }
            try {
                RemoveLeaderPrepMessage removeLeaderMessage = InputHelper.getRemoveLeaderPrepMessage(ui.getLocalGame(), indexes[0].toString(), indexes[1].toString());
                ui.getGameHandler().dealWithMessage(removeLeaderMessage);
            } catch (LeaderIndexOutOfBoundException e) {
                logger.error("Error while selecting leaders to be kept: " + e);
            } catch (IOException e) {
                logger.error("Error while handling request: " + e);
            }
        });
    }

    private void onImageClicked(int ind){
        selected[ind] = !selected[ind];
        if(selected[ind]){
            count++;
            Platform.runLater(() -> leaders.get(ind).setStyle("-fx-effect: dropshadow(three-pass-box, rgba(199,17,17,0.8), 20, 0, 0, 0)"));
        }else{
            count--;
            Platform.runLater(() -> leaders.get(ind).setStyle("-fx-background-radius: 0"));
        }
        doneBtn.setDisable(count != 2);
    }

    @Override
    public void notifyUpdate() {
        synchronized (ui.getLocalGame()) {
            if (ui.getLocalGame().getMainPlayer().getLocalBoard().getLeaderCards().size() == 2) {
                BuildGUI.getInstance().toBoard(stage, ui);
            }
            // todo: if else, something by another player happened
        }
    }

    @Override
    public void notifyError() {
        logger.error("Error notification has arrived. Not expected");
    }
}
