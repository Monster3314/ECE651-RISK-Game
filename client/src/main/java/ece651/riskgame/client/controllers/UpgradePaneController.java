package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Unit;
import ece651.riskgame.shared.UpgradeUnitAction;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
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
  public void submitAction() throws IOException, ClassNotFoundException {
    String terr = ((MenuButton) pane.lookup("#territory")).getText();    
    // TODO change viewer later, upgrade 1 group at a time
    for (int i = 2; i <= 7; i++) {
      try {
        int num = Integer.parseInt(((TextField) pane.lookup("#field"+i)).getText());
        Action act = new UpgradeUnitAction(terr, i-1, i, num, gameController.guiPlayer.getColor());
        String result = gameController.guiPlayer.tryApplyAction(act);
        if (result != null) {
          // very buggy now
          gameController.updateHint(result);
          System.out.println(result);
        }
        else {
          gameController.updateHint("Submitted!");
          System.out.println("submitted");
          gameController.guiPlayer.addActionToSend(act);
          gameController.updateTopBar();
          gameController.updateCurrentTerritoryInfo();
          updateUpgradePane();
        }
      }
      catch (NumberFormatException e) {
        // ignore for now
        System.out.println("number format wrong");
      }
      catch (NullPointerException e) {
        System.out.println("Null Pointer found");
      }
    }    
    
  }
}
