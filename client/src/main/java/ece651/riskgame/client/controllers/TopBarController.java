package ece651.riskgame.client.controllers;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.UpgradeTechAction;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class TopBarController {

  @FXML
  Label playerName;
  @FXML
  Label playerFood;
  @FXML
  Label playerGold;
  @FXML
  Label playerLevel;
  @FXML
  Button levelUp;

  GameController gameController;
  GUIPlayer guiPlayer;

  @FXML
  public void levelUp(MouseEvent me) {
    Action lu = new UpgradeTechAction(guiPlayer.getColor());
    String result = guiPlayer.tryApplyAction(lu);
    if (result != null) {
      gameController.updateHint(result);
    } else {
      gameController.updateHint("Level up!");
      updateTopBar();
      guiPlayer.addActionToSend(lu);
      inactivateLevelUpButton();
    }
  }

  public void inactivateLevelUpButton() {
    levelUp.setDisable(true);
  }

  public void activateLevelUpButton() {
    levelUp.setDisable(false);
  }

  /**
   * Update food, gold, food
   */
  public void updateTopBar() {
    System.out.println("topbar update");
    System.out.println(guiPlayer.getFood());
    playerFood.setText("Food: " + Integer.toString(guiPlayer.getFood()));
    playerGold.setText("Gold: " + Integer.toString(guiPlayer.getGold()));
    playerLevel.setText("Level: " + Integer.toString(guiPlayer.getTechLevel()));
  }

  /**
   * Set username of the game
   */
  public void setUsername(String name) {
    playerName.setText(name);
  }
}
