package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.enums.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.TreeMap;

public class StrongBoxComponent extends Pane {

    public VBox vBox;

    public void updateRes(TreeMap<Resource, Integer> strongboxRes){
        for(Resource r: strongboxRes.keySet()){
            updateRes(r, strongboxRes.get(r));
        }
    }

    public void updateRes(Resource r, Integer q) {
        boolean found = false;
        for(Node c: vBox.getChildren()){
            ResAmountComponent tmp = (ResAmountComponent) c;
            if(tmp.getRes() == r){
                found = true;
                tmp.setRes(r, q);
            }
        }
        if(!found){
            ResAmountComponent resAmountComponent = new ResAmountComponent();
            resAmountComponent.setRes(r, q);
            vBox.getChildren().add(resAmountComponent);
        }
    }


    public StrongBoxComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/strongbox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
