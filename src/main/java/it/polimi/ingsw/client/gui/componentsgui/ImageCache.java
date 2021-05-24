package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.enums.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public final class ImageCache {
    private static final Logger logger = LogManager.getLogger(FaithTrackComponent.class);

    public static final Image NOTHINGIMG = new Image("/png/res/no_res.png");;
    public static final Image ROCKIMG = new Image("/png/res/rock.png");
    public static final Image SERVANTIMG = new Image("/png/res/servant.png");
    public static final Image GOLDIMG = new Image("/png/res/gold.png");
    public static final Image SHIELDIMG = new Image("/png/res/shield.png");

    public static void setImage(Resource r, ImageView im){
        switch (r){
            case NOTHING:
                im.setImage(NOTHINGIMG);
                break;
            case SHIELD:
                im.setImage(SHIELDIMG);
                break;
            case GOLD:
                im.setImage(GOLDIMG);
                break;
            case SERVANT:
                im.setImage(SERVANTIMG);
                break;
            case ROCK:
                im.setImage(ROCKIMG);
                break;
            default:
                logger.error("Cannot store this type of res: " + r);
        }
    }
}
