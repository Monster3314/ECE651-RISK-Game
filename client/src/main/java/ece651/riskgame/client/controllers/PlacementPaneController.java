package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class PlacementPaneController {

  @FXML
  Pane pane;
  
  GameController gameController;
  
  @FXML
  public void submitPlacement(MouseEvent me) throws ClassNotFoundException, InterruptedException {
    // get inputs
    try {
      gameController.updateHint("Submitted! Waiting for other players");
      // call guiplayer
      Map<String, Integer> placements = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).stream().collect(
          Collectors.toMap(i -> ((Label) pane.lookup("#label" + i)).getText(),
                           i -> Integer.parseInt(((TextField) pane.lookup("#field"+i)).getText())));            
      // TODO: The wait needs Task.setOnSucceded, learn later
      String result = gameController.guiPlayer.tryPlace(placements);
      if (result == null) {
        gameController.gameIO.sendPlacements(gameController.guiPlayer.adaptPlacements(placements));
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
    }
  }

}
