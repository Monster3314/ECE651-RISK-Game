package ece651.riskgame.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

public class GameController implements Initializable {

  @FXML
  GridPane placementPanel;

  @FXML
  GridPane actionPanel;

  @FXML
  PlacementPanelController placementPanelController;
  
  @FXML
  GameMapController gameMapController;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println(placementPanel);
    System.out.println(gameMapController);
    System.out.println(placementPanelController);
                                                               
    placementPanelController.placementPanel = placementPanel;
    placementPanelController.actionPanel = actionPanel;
  }

}
