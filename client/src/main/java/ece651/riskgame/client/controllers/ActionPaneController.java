package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Move;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ActionPaneController {
  @FXML
  Pane pane;

  GameController gameController;
  
  @FXML
  public void submitAction() throws IOException {
    try {
      String from = ((MenuButton) pane.lookup("#from")).getText();
      String to = ((MenuButton) pane.lookup("#to")).getText();

      List<Action> acts = new ArrayList<Action>();

      // TODO check all fields
      int num = Integer.parseInt(((TextField) pane.lookup("#field1")).getText());

      Action act = gameController.ifMoveInAction ? new Move(new BasicUnit(num), from, to, gameController.guiPlayer.getColor())
          : new Attack(new BasicUnit(num), from, to, gameController.guiPlayer.getColor());
      String result = gameController.guiPlayer.tryApplyAction(act);
      if (result != null) {
        gameController.updateHint(result);
      } else {
        acts.add(act);
        gameController.guiPlayer.addActionToSend(act);
        gameController.updateCurrentTerritoryInfo();
        gameController.updateHint("Action submitted!");
      }
    } // end try
    catch (NumberFormatException e) {
      gameController.updateHint("Assign numbers please");
    }
  }

}
