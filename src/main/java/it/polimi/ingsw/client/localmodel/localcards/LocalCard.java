package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.Observable;
import javafx.scene.image.Image;
import java.io.File;

public class LocalCard extends Observable {
    protected final int id;
    protected Image image;

    public int getId() {
        return id;
    }

    public LocalCard(int id) {
        this.id = id;
        this .image = null;
    }

    public Image getImage(){
        if (image == null) {
            String path = String.format("src/main/resources/png/cards_front/%03d.png", id);
            File file = new File(path);
            this.image = new Image(file.toURI().toString());
        }
        return image;
    }
}
