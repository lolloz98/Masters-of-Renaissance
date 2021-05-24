package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.enums.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ResAmountComponent extends HBox {
    private static final Logger logger = LogManager.getLogger(FaithTrackComponent.class);

    public Label label;
    public ImageView img;
    private Resource res = Resource.NOTHING;
    private int quantity = 0;

    public Resource getRes() {
        return res;
    }

    public void setRes(Resource r, Integer q){
        res = r;
        quantity = q;
        label.setText("x " + q.toString());
        ImageCache.setImageInStore(r, img);
    }

    public ResAmountComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/res_amount.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
