package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class DepotPrinter {

    public static ArrayList<String> toStringBlock(LocalPlayer localPlayer) {
        ArrayList<String> out = new ArrayList<>();
        TreeMap<Resource, Integer> resDepot = new TreeMap<>(localPlayer.getLocalBoard().getResInNormalDepot());
        ArrayList<String> square = new ArrayList<>();
        square.add("┏━━┓");
        square.add("┃  ┃");
        square.add("┗━━┛");
        out.add("         ");
        out.add("         ");
        out.add("         ");
        out.add("       ");
        out.add("       ");
        out.add("       ");
        out.add("     ");
        out.add("     ");
        out.add("     ");
        if(resDepot.size()>0){
            int max = 0;
            Resource resMax = Resource.NOTHING;
            for(Resource r : resDepot.keySet()){
                if(resDepot.get(r)>max){
                    resMax = r;
                    max = resDepot.get(r);
                }
            }
            if(max==0){
                CLIutils.append(out, 6, square);
                CLIutils.append(out, 6, square);
                CLIutils.append(out, 6, square);
            }
            else if(max==1){
                CLIutils.append(out, 6, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 6, square);
                CLIutils.append(out, 6, square);
            }
            else if(max==2){
                CLIutils.append(out, 6, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 6, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 6, square);
            }
            else if(max==3){
                CLIutils.append(out, 6, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 6, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 6, MarblePrinter.toStringBlock(resMax));
            }
            resDepot.remove(resMax);
        } else {
            CLIutils.append(out, 6, square);
            CLIutils.append(out, 6, square);
            CLIutils.append(out, 6, square);
        }
        if(resDepot.size()>0){
            int max = 0;
            Resource resMax = Resource.NOTHING;
            for(Resource r : resDepot.keySet()){
                if(resDepot.get(r)>max){
                    resMax = r;
                    max = resDepot.get(r);
                }
            }
            if(max==0){
                CLIutils.append(out, 3, square);
                CLIutils.append(out, 3, square);
            }
            else if(max==1){
                CLIutils.append(out, 3, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 3, square);
            }
            else if(max==2){
                CLIutils.append(out, 3, MarblePrinter.toStringBlock(resMax));
                CLIutils.append(out, 3, MarblePrinter.toStringBlock(resMax));
            }
            resDepot.remove(resMax);
        } else {
            CLIutils.append(out, 3, square);
            CLIutils.append(out, 3, square);
        }
        if(resDepot.size()>0){
            int max = 0;
            Resource resMax = Resource.NOTHING;
            for(Resource r : resDepot.keySet()){
                if(resDepot.get(r)>max){
                    resMax = r;
                    max = resDepot.get(r);
                }
            }
            if(max==0){
                CLIutils.append(out, 0, square);
            }
            else if(max==1){
                CLIutils.append(out, 0, MarblePrinter.toStringBlock(resMax));
            }
            resDepot.remove(resMax);
        } else {
            CLIutils.append(out, 0, square);
        }
        CLIutils.append(out, 0,"           ");
        CLIutils.append(out, 1,"           ");
        CLIutils.append(out, 2,"           ");
        CLIutils.append(out, 3,"         ");
        CLIutils.append(out, 4,"         ");
        CLIutils.append(out, 5,"         ");
        CLIutils.append(out, 6,"       ");
        CLIutils.append(out, 7,"       ");
        CLIutils.append(out, 8,"       ");
        return out;
    }
}
