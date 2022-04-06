package ece651.riskgame.client.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class PlacementPanelController {

  @FXML
  Node placementPanel;

  @FXML
  Node actionPanel;
  
  
  public void confirmPlacement(Event event) {
    // organize all the data in placement panel
    // check the numbers
    // send them to server

    // get data back
    // change panel
    this.placementPanel.setVisible(false);
    this.actionPanel.setVisible(true);
  }
  
}
