package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.enums.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static it.polimi.ingsw.client.gui.componentsgui.ImageCache.*;

public class DepotComponent extends Pane {
    private static final Logger logger = LogManager.getLogger(FaithTrackComponent.class);

    public ImageView d1;
    public ImageView d2_0;
    public ImageView d2_1;
    public ImageView d3_0;
    public ImageView d3_1;
    public ImageView d3_2;

    private final List<ImageView> depot2;
    private final List<ImageView> depot3;





    public void setImages(TreeMap<Resource, Integer> resInDepot){
        Resource[] res = {Resource.NOTHING, Resource.NOTHING, Resource.NOTHING};
        for(Resource r: resInDepot.keySet()){
            int tmp = resInDepot.get(r) - 1;
            if(res[tmp] == Resource.NOTHING) res[tmp] = r;
            else if(res[tmp + 1] == Resource.NOTHING) res[tmp + 1] = r;
            else if(res[tmp + 2] == Resource.NOTHING) res[tmp + 2] = r;
        }
        for(int i = 0; i < 3; i++){
            int tmp = resInDepot.get(res[i]);
            while(tmp != 0){
                tmp--;
                if(i == 0) {
                    ImageCache.setImage(res[i], d1);
                }
                else if(i == 1) {
                    setImage(res[i], depot2.get(tmp));
                }
                else setImage(res[i], depot3.get(tmp));
            }
        }
    }



    public DepotComponent(){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/depot.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        depot2 = new ArrayList<>(){{
            add(d2_0);
            add(d2_1);
        }};
        depot3 = new ArrayList<>(){{
            add(d3_0);
            add(d3_1);
            add(d3_2);
        }};
//        NOTHINGIMG = new Image("/png/res/no_res.png");
//        ROCKIMG = new Image("/png/res/rock.png");
//        SERVANTIMG = new Image("/png/res/servant.png");
//        GOLDIMG = new Image("/png/res/gold.png");
//        SHIELDIMG = new Image("/png/res/shield.png");
    }
}
