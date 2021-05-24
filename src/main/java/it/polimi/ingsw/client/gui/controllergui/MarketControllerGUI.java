package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.enums.Resource;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class MarketControllerGUI extends ControllerGUI implements Observer {
    public GridPane market_grid;
    @FXML
    public ImageView free_marble;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        this.root = root;
        market_grid.setHgap(1); //horizontal gap in pixels => that's what you are asking for
        market_grid.setVgap(1); //vertical gap in pixels
        ui.getLocalGame().overrideObserver(this);
        Resource[][] marbleMatrix = ui.getLocalGame().getLocalMarket().getMarbleMatrix();
        Image marbleImage;
        String path;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        path = Objects.requireNonNull(classLoader.getResource("png/punchboard/marbles/" + ui.getLocalGame().getLocalMarket().getFreeMarble() + ".png")).getPath();
        File freeMarbleFile = new File(path);
        marbleImage = new Image(freeMarbleFile.toURI().toString());
        free_marble.setImage(marbleImage);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                ImageView imgView = new ImageView();
                path = Objects.requireNonNull(classLoader.getResource("png/punchboard/marbles/" + marbleMatrix[y][x] + ".png")).getPath();
                File file = new File(path);
                marbleImage = new Image(file.toURI().toString());
                imgView.setImage(marbleImage);
                imgView.setFitHeight(56);
                imgView.setFitWidth(56);
                market_grid.add(imgView, x, y);
            }
        }
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }
}
