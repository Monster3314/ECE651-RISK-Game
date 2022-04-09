package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.UpgradeUnitAction;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class UpgradePaneController {
  @FXML
  Pane pane;

  GameController gameController;

  final List<String> unitLevelNames = List.of("Vale Knights", "North Army", "Second Sons", "Iron Fleet", "Unsullied",
      "Golden Company", "Dead Army");

  /**
   * Method to update upgrade pane territories
   */
  public void updateUpgradePane() throws ClassNotFoundException, IOException {
    MenuButton territories = (MenuButton) pane.lookup("#territory");
    gameController.updateMenuButton(territories, gameController.guiPlayer.getOccupies().stream()
        .map(t -> gameController.createMenuItem(t.getName(), territories)).collect(Collectors.toList()));
  }

  /**
   * Method to set menu items for upgrade territory
   */
  public void setUpgradePane() {
    MenuButton from = (MenuButton) pane.lookup("#from");
    MenuButton to = (MenuButton) pane.lookup("#to");
    createMenuItems(from, unitLevelNames);
    createMenuItems(to, unitLevelNames);
  }

  private void createMenuItems(MenuButton mb, List<String> names) {
    for (String name : names) {
      MenuItem mi = new MenuItem(name);
      mb.getItems().add(mi);
      mi.setOnAction(a -> {
        mb.setText(name);
      });
    }
  }

  @FXML
  public void submitAction() throws IOException, ClassNotFoundException {
    String terr = ((MenuButton) pane.lookup("#territory")).getText();
    String from = ((MenuButton) pane.lookup("#from")).getText();
    String to = ((MenuButton) pane.lookup("#to")).getText();
    String numInput = ((TextField) pane.lookup("#number")).getText();
    
    try {
      int fromL = unitLevelNames.indexOf(from);
      int toL = unitLevelNames.indexOf(to);
      int num = Integer.parseInt(numInput);
      if (fromL == -1 || toL == -1 || fromL >= toL) {
        throw new IndexOutOfBoundsException();
      }
      if (num < 0) {
        throw new NumberFormatException();
      }
      Action act = new UpgradeUnitAction(terr, fromL, toL, num, gameController.guiPlayer.getColor());
      String result = gameController.guiPlayer.tryApplyAction(act);
      if (result == null) {
        gameController.guiPlayer.addActionToSend(act);
        gameController.updateHint("Upgrade successfully");
        gameController.updateCurrentTerritoryInfo();
        gameController.updateTopBar();
      }
      else {
        gameController.updateHint(result);
      }            
    } catch (IndexOutOfBoundsException e) {
      gameController.updateHint("Select valid unit level");
    }catch (NumberFormatException e) {
      gameController.updateHint("Type positive number");
    }
    

  }
}
