package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.shared.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameController {

  @FXML
  VBox infoView;

  String username;
  GUIPlayer guiPlayer;
  Scene scene;
  Label hint;

  // flags
  boolean ifMoveInAction = true;
  
  public GameController(GUIPlayer p) {
    guiPlayer = p;
    username = p.getColor();
  }

  public void setScene(Scene s) {
    scene = s;
  }

  public void setHint() {
    hint = (Label) scene.lookup("#hint");
  }

  @FXML
  public void showTerritoryInfo(MouseEvent me) {
    String territoryName = ((Button) me.getSource()).getText();
    // check after get from guiplayer
    // display territory information
    infoView.getChildren().clear();
    infoView.setAlignment(Pos.TOP_CENTER);

    Label label = new Label(territoryName);
    label.setTextAlignment(TextAlignment.CENTER);
    label.setFont(new Font(20));

    HBox hbox = new HBox();
    Label labelUnit = new Label("Units");
    labelUnit.setMaxWidth(Double.MAX_VALUE);
    labelUnit.setAlignment(Pos.CENTER);
    labelUnit.setFont(new Font(15));

    try {
      int unitNum = guiPlayer.getGame().getBoard().getTerritory(territoryName).getUnits().get(0).getNum();

      Label text = new Label(Integer.toString(unitNum));
      text.setMaxWidth(Double.MAX_VALUE);
      text.setAlignment(Pos.CENTER);
      text.setFont(new Font(15));
      hbox.getChildren().addAll(labelUnit, text);
      HBox.setHgrow(labelUnit, Priority.ALWAYS);
      HBox.setHgrow(text, Priority.ALWAYS);

      infoView.getChildren().addAll(label, hbox);
    } catch (IndexOutOfBoundsException e) {
      hint.setText("You cannot see territory details before placement");
    }
  }

  @FXML
  public void submitPlacement(MouseEvent me) throws ClassNotFoundException, InterruptedException {
    // get inputs
    String msg = null;
    try {
      hint.setText("Submitted! Waiting for other players");
      // call guiplayer
      Map<String, Integer> placements = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).stream().collect(
          Collectors.toMap(i -> ((Label) scene.lookup("#placementPane #label" + i)).getText(),
                           i -> Integer.parseInt(((TextField) scene.lookup("#placementPane").lookup("#field"+i)).getText())));      
      // wait(50);
      // TODO: The wait needs Task.setOnSucceded, learn later
      msg = guiPlayer.tryPlace(placements);
      if (msg == null) {
        guiPlayer.sendPlacements(placements);
        // update map
        guiPlayer.updateGame();
        updateTerritoryColors();

        fromPlacementToAction();
        
        msg = "Welcome to game world! Let's crush enemies!";
      }

    } catch (IOException e) {
      msg = "IOException occurs during connection to server...";
    } catch (NumberFormatException e) {
      msg = "Please type number to place";
    }
    // set bad message
    hint.setText(msg);
  }

  @FXML
  private void submitAction() throws IOException {
    // check move or attack
    // if move
    // create Move
    // else attack
    // create Attack
    
    // gather data and put into Action
    // call
    guiPlayer.sendActions(null);
  }

  /**
   * Set labels(territory names) for
   */
  public void setPlacementPaneLabels() throws ClassNotFoundException, IOException {
    int i = 1;
    for (Territory t : guiPlayer.getOccupies()) {
      String labelId = "label" + i;
      Label label = (Label) scene.lookup("#placementPane").lookup("#" + labelId);
      label.setText(t.getName());
      i++;
    }
    Label title = ((Label) scene.lookup("#placementPane").lookup("#title"));
    title.setText("Place your " + guiPlayer.getUnitsToPlace() + " units");
  }

  /**
   * Set username of the game
   */
  public void setUsername(Scene scene, String name) {
    ((Labeled) scene.lookup("#playerName")).textProperty().setValue(name);
  }

  /**
   * Update Territory colors, called at the beginning of each round
   */
  public void updateTerritoryColors() {
    for (String color : guiPlayer.getGame().getClans().keySet()) {
      for (Territory t : guiPlayer.getGame().getClans().get(color).getOccupies()) {
        ((Button) scene.lookup("#" + t.getName() + "Territory")).setStyle("-fx-background-color:" + color);
      }
    }
  }

  /**
   * Update action pane base on current gameinfo
   */
  public void updateActionPane() {    
    // update two drop down menus
    updateMenuButton((MenuButton) scene.lookup("#actionPane #from"), guiPlayer.getOccupies().stream().map(t -> new MenuItem(t.getName())).collect(Collectors.toList()));
    if (ifMoveInAction) {
      updateMenuButton((MenuButton) scene.lookup("#actionPane #to"), guiPlayer.getOccupies().stream().map(t -> new MenuItem(t.getName())).collect(Collectors.toList()));
    } else { // attack
      // TODO
    }
    // TODO : advanced feature, adjust unit numbers based on selected territory
  }

  /**
   * Helper function to update MenuButton with given contents
   */
  private void updateMenuButton(MenuButton mb, List<MenuItem> contents) {
    mb.getItems().clear();
    contents.stream().forEach(c -> mb.getItems().add(c));
  }

  /**
   * Helper function to call function from placement phase to action phase
   */
  public void fromPlacementToAction() {
    // TODO update upgrade pane
    updateActionPane();
    updateTerritoryColors();
    // TODO activate many button

    scene.lookup("#placementPane").setVisible(false);
    scene.lookup("#actionPane").setVisible(true);

  }

  @FXML
  public void selectMovePane() {
    // set upgrade to invisible and action to visible
    scene.lookup("#upgradePane").setVisible(false);
    scene.lookup("#actionPane").setVisible(true);
    // TODO  darken the Button the enlight other 2
    ifMoveInAction = true;
    updateActionPane();
  }

}
