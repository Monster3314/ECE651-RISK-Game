package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;

public class UpgradePaneController {
  @FXML
  Pane pane;

  GameController gameController;

  /**
   * Method to update upgrade pane territories
   */
  public void updateUpgradePane() throws ClassNotFoundException, IOException {
    MenuButton territories = (MenuButton) pane.lookup("#territory");
    gameController.updateMenuButton(territories, gameController.guiPlayer.getOccupies().stream().map(t -> gameController.createMenuItem(t.getName(), territories)).collect(Collectors.toList()));    
  }
  
  @FXML
  public void submitAction() throws IOException {
    String terr = ((MenuButton) pane.lookup("#territory")).getText();
    // TODO after action settled
  }
}
