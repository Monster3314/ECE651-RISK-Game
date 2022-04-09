package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ActionPaneController {
  boolean ifMove = true;

  @FXML
  Pane pane;

  GameController gameController;

  public void moveMode() {
    ifMove = true;
  }

  public void attackMode() {
    ifMove = false;
  }
  
  /**
   * Update action pane base on current gameinfo
   */
  public void updateActionPane() {
    MenuButton from = (MenuButton) pane.lookup("#from");
    MenuButton to = (MenuButton) pane.lookup("#to");
    // update two drop down menus
    gameController.updateMenuButton(from, gameController.guiPlayer.getOccupies().stream().map(t -> gameController.createMenuItem(t.getName(), from)).collect(Collectors.toList()));
    if (ifMove) {
      gameController.updateMenuButton(to, gameController.guiPlayer.getOccupies().stream().map(t -> gameController.createMenuItem(t.getName(), to)).collect(Collectors.toList()));
    } else { // attack
      gameController.updateMenuButton(to, gameController.guiPlayer.getEnemyTerritoryNames().stream().map(t -> gameController.createMenuItem(t, to)).collect(Collectors.toList()));
    }
    // TODO : advanced feature, adjust unit numbers based on selected territory
  }

  
  @FXML
  public void submitAction() throws IOException {
    try {
      String from = ((MenuButton) pane.lookup("#from")).getText();
      String to = ((MenuButton) pane.lookup("#to")).getText();

      List<Unit> units = new ArrayList<Unit>();
      for (int i = 1; i <= 7; i++) {
        try {
          int num = Integer.parseInt(((TextField) pane.lookup("#field"+i)).getText());
          Unit unit = new BasicUnit(num, i-1);
          units.add(unit);
          System.out.println(unit.getNum());
        }
        catch (NullPointerException e) {
          // ignore it
        }
        catch (NumberFormatException e) {
          // ignore it
        }
      }
      if (units.isEmpty()) {
        throw new IllegalArgumentException("Type number to submit");
      }
      
      Action act = ifMove ? new Move(units, from, to, gameController.guiPlayer.getColor())
          : new Attack(units, from, to, gameController.guiPlayer.getColor());
      String result = gameController.guiPlayer.tryApplyAction(act);
      if (result != null) {
        gameController.updateHint(result);
      } else {
        gameController.guiPlayer.addActionToSend(act);                
        gameController.updateCurrentTerritoryInfo();
        gameController.updateHint("Action submitted!");
      }
      gameController.updateTopBar();
    } // end try
    catch (IllegalArgumentException e) {
      gameController.updateHint(e.getMessage());
    }
  }

}
