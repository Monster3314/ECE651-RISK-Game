package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class PlacementPaneController {

  @FXML
  Pane pane;
  
  GameController gameController;

  List<Unit> totalUnit;
  
  /**
   * Set labels(territory names) for
   */
  public void setPlacementPaneLabels() throws ClassNotFoundException, IOException {
    int i = 1;
    for (Territory t : gameController.guiPlayer.getOccupies()) {
      String labelId = "label" + i;
      Label label = (Label) pane.lookup("#" + labelId);
      label.setText(t.getName());
      i++;
    }
    Label title = ((Label) pane.lookup("#title"));
    totalUnit = gameController.gameIO.recvUnitsToPlace();
    title.setText("Place your " + totalUnit.get(0).getNum() + " units");
  }

  
  @FXML
  public void submitPlacement(MouseEvent me) throws ClassNotFoundException, InterruptedException {
    // get inputs
    try {
      gameController.updateHint("Submitted! Waiting for other players");
      // call guiplayer
      Map<String, List<Unit>> placements = new HashMap();
      for (int i = 1; i <= 3; i++) {
        String territory = ((Label) pane.lookup("#label" + i)).getText();
        int num = Integer.parseInt(((TextField) pane.lookup("#field"+i)).getText());
        Unit unit = new BasicUnit(num, 1);
        placements.put(territory, List.of(unit));
      }
      // TODO: The wait needs Task.setOnSucceded, learn later
      String result = gameController.guiPlayer.tryPlace(placements, totalUnit);
      if (result == null) {
        gameController.gameIO.sendPlacements(placements);
        // update map
        gameController.guiPlayer.updateGame(gameController.gameIO.recvGame());        
        gameController.updateHint("Welcome to game world! Let's crush enemies!");
        gameController.fromPlacementToAction();        
        gameController.isLostOrWin();        
      } else {
        gameController.updateHint(result);
      }
    } catch (IOException e) {
      gameController.updateHint("IOException occurs during connection to server...");
    } catch (NumberFormatException e) {
      gameController.updateHint("Please type number to place");
    } catch (IllegalArgumentException e) {
      gameController.updateHint("Type non-negative number only");
    }
  }

}
