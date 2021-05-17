package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.messages.requests.actions.FlushProductionResMessage;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class BoardView extends GameView {
    private final LocalPlayer localPlayer;

    public BoardView(CLI cli, LocalGame<?> localGame, LocalPlayer localPlayer) {
        this.localGame = localGame;
        this.cli = cli;
        this.localPlayer = localPlayer;
        localGame.getError().addObserver(this);
        localPlayer.getLocalBoard().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
        waiting = false;
    }

    public BoardView(CLI cli, LocalGame<?> localGame, LocalPlayer localPlayer, boolean waiting) {
        this.localGame = localGame;
        this.cli = cli;
        this.localPlayer = localPlayer;
        localGame.getError().addObserver(this);
        localPlayer.getLocalBoard().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
        this.waiting = waiting;
    }

    @Override
    public synchronized void draw() {
        if (waiting) {
            System.out.println("Please wait");
        } else {
            CLI.clearScreen();
            // todo make this good looking
            System.out.println(localPlayer.getName() + "'s board:");
            System.out.println("Resources in depot:" + localPlayer.getLocalBoard().getResInNormalDepot());
            System.out.println("Resources in box:" + localPlayer.getLocalBoard().getResInStrongBox());
            if (localGame instanceof LocalSingle) {
                LocalSingle localSingle = (LocalSingle) localGame;
                System.out.println("Lorenzo's faith points:" + localSingle.getLorenzoTrack().getFaithTrackScore());
                System.out.println("Your faith points:" + localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore());
            } else
                System.out.println("Faith points:" + localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore());
            // base production
            System.out.print("Base production: res to give: " + localPlayer.getLocalBoard().getBaseProduction().getResToGive());
            System.out.print(", res to gain: " + localPlayer.getLocalBoard().getBaseProduction().getResToGain());
            System.out.print(", res to flush: " + localPlayer.getLocalBoard().getBaseProduction().getResToFlush() + "\n");
            int i;
            for (i = 0; i < 3; i++) {
                if (localPlayer.getLocalBoard().getDevelopCards().get(i).size() == 0) {
                    System.out.println("No cards in " + i + "° slot");
                } else {
                    LocalProduction localProduction = localPlayer.getLocalBoard().getDevelopCards().get(i).get(localPlayer.getLocalBoard().getDevelopCards().get(i).size() - 1).getProduction();
                    System.out.print((i + 1) + "° production slot: res to give: " + localProduction.getResToGive());
                    System.out.print(", res to gain: " + localProduction.getResToGain());
                    System.out.print(", res to flush: " + localProduction.getResToFlush());
                    int sum = 0;
                    for (LocalDevelopCard c : localPlayer.getLocalBoard().getDevelopCards().get(i))
                        sum = sum + c.getVictoryPoints();
                    System.out.print(", total vp in this slot: " + sum + "\n");
                }
            }
            System.out.println("Leader cards:");
            for (LocalCard c : localPlayer.getLocalBoard().getLeaderCards()) {
                if (c instanceof LocalConcealedCard) {
                    System.out.print("This card is not activated yet");
                } else {
                    LocalLeaderCard localLeaderCard = (LocalLeaderCard) c;
                    if (c instanceof LocalDiscountLeader) {
                        LocalDiscountLeader localDiscountLeader = (LocalDiscountLeader) c;
                        System.out.print("DiscountLeader");
                        System.out.print(", prod requirement: " + localDiscountLeader.getProdRequirement());
                        System.out.print(", discounted res: " + localDiscountLeader.getQuantityToDiscount() + " " + localDiscountLeader.getDiscountedRes());
                    } else if (c instanceof LocalMarbleLeader) {
                        LocalMarbleLeader localMarbleLeader = (LocalMarbleLeader) c;
                        System.out.print("MarbleLeader");
                        System.out.print(", prod requirement: " + localMarbleLeader.getProdRequirement());
                        System.out.print(", marble: " + localMarbleLeader.getMarbleResource());
                    } else if (c instanceof LocalDepotLeader) {
                        LocalDepotLeader localDepotLeader = (LocalDepotLeader) c;
                        System.out.print("DepotLeader");
                        System.out.print(", requirement: " + localDepotLeader.getReqQuantity() + " " + localDepotLeader.getResRequirement());
                        System.out.print(", depot: " + localDepotLeader.getNumberOfRes() + " " + localDepotLeader.getResType());
                    } else if (c instanceof LocalProductionLeader) {
                        LocalProductionLeader localProductionLeader = (LocalProductionLeader) c;
                        System.out.print("ProductionLeader");
                        System.out.print(", prod requirement: " + localProductionLeader.getColorRequirement() + " at level " + localProductionLeader.getLevelReq());
                        System.out.print(", production: " + localProductionLeader.getProduction().getResToGive() +" -> "+localProductionLeader.getProduction().getResToGain());
                    }
                    if (localLeaderCard.isDiscarded()){
                        System.out.print(", this card is discarded");
                    }
                    else if (localLeaderCard.isActive()) {
                        System.out.print(", this card is active");
                    } else {
                        System.out.print(", this card is not active");
                    }
                }
                System.out.print("\n");
            }
            super.drawTurn();
        }
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localPlayer.getLocalBoard().removeObserver();
        localGame.getLocalTurn().removeObserver();
    }

    @Override
    public synchronized void notifyUpdate() {
        draw();
        waiting = false;
    }

    @Override
    public synchronized void helpScreen() {
        super.helpScreen();
        System.out.println("'leader', followed by a number, to activate a leader card");
        System.out.println("'prod', followed by a number, to activate a production");
        System.out.println("'flush', to move all the resources currently in a production to the strongbox");

    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localGame.getError().getErrorMessage());
        waiting = false;
    }

    @Override
    public synchronized void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            if (localPlayer == localGame.getMainPlayer()) { // if i am on my board i can activate leaders or productions
                if (ansList.size() > 2) {
                    writeErrText();
                } else {
                    switch (ansList.get(0)) {
                        case "LEADER":
                            activateLeader(ansList.get(1));
                            break;
                        case "PROD":
                            activateProduction(ansList.get(1));
                            break;
                        case "FLUSH":
                            flushProduction();
                            break;
                        default:
                            super.handleCommand(ansList);
                    }
                }
            } else { // otherwise i can only move
                if (ansList.size() > 2) {
                    writeErrText();
                } else {
                    super.handleCommand(ansList);
                }
            }
        }
    }

    private void flushProduction() {
        try {
            cli.getServerListener().sendMessage(new FlushProductionResMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void activateProduction(String s) {
        int number = -1;
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            writeErrText();
        }
        if (number >= 0 && number < 6) {
            if (number == 0) {
                removeObserved();
                cli.setState(new ActivateProductionView(cli, localGame, 0));
            } else if (number > 1 && number < 4) {
                if (localPlayer.getLocalBoard().getDevelopCards().get(number - 1).size() == 0) {
                    writeErrText(); // there are no develop cards in this slot
                } else {
                    removeObserved();
                    cli.setState(new ActivateProductionView(cli, localGame, number));
                }
            } else {
                if ((number - 4) < localPlayer.getLocalBoard().getLeaderCards().size() || !(localPlayer.getLocalBoard().getLeaderCards().get(number - 4) instanceof LocalProductionLeader)) {
                    writeErrText();
                } else {
                    removeObserved();
                    cli.setState(new ActivateProductionView(cli, localGame, number));
                }
            }
        }
    }

    private void activateLeader(String s) {
        int number = 0;
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            writeErrText();
        }
        if (number > 0 && number < localPlayer.getLocalBoard().getLeaderCards().size()) {
            try {
                cli.getServerListener().sendMessage(new ActivateLeaderMessage(localGame.getGameId(), localPlayer.getId(), localPlayer.getLocalBoard().getLeaderCards().get(number - 1).getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        waiting = true;
    }
}
