package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.shared.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    }
    catch (IndexOutOfBoundsException e) {
      hint.setText("You cannot see territory details before placement");
    }
  }

  @FXML
  public void submitPlacement(MouseEvent me) throws ClassNotFoundException {
    // get inputs
    int[] nums = new int[3];
    String msg = null;
    try {
      nums[0] = Integer.parseInt(((TextField) scene.lookup("#placementPane").lookup("#field1")).getText());
      nums[1] = Integer.parseInt(((TextField) scene.lookup("#placementPane").lookup("#field2")).getText());
      nums[2] = Integer.parseInt(((TextField) scene.lookup("#placementPane").lookup("#field3")).getText());
      // call guiplayer
      Map<String, Integer> placements = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).stream().collect(
          Collectors.toMap(i -> ((Label) scene.lookup("#placementPane #label" + i)).getText(), i -> nums[i - 1]));
      try {
        System.out.println("sendplacemnt");
        guiPlayer.sendPlacements(placements);
        // update map
        guiPlayer.updateGame();
        updateTerritoryColors();
        // activate many button
        
        // TODO
        scene.lookup("#placementPane").setVisible(false);
        scene.lookup("#actionPane").setVisible(true);

        return;
      } catch (IOException e) {
        msg = "IOException occurs during connection to server...";
      }
    } catch (NumberFormatException e) {
      msg = "Please type number to place";
    }
    // set bad message
    hint.setText(msg);
  }

  @FXML
  private void submitAction() {
    System.out.println("Action submitted");
  }

  
  public void setPlacementPaneLabels() throws ClassNotFoundException, IOException {
    int i = 1;
    for (Territory t : guiPlayer.getMyTerritories()) {
      String labelId = "label" + i;
      Label label = (Label) scene.lookup("#placementPane").lookup("#" + labelId);
      label.setText(t.getName());
      i++;
    }
    Label title = ((Label) scene.lookup("#placementPane").lookup("#title"));
    title.setText("Place your " + guiPlayer.getUnitsToPlace() + " units");
  }

  public void updateTerritoryColors() {
    for (String color : guiPlayer.getGame().getClans().keySet()) {
      for (Territory t : guiPlayer.getGame().getClans().get(color).getOccupies()) {
        ((Button) scene.lookup("#" + t.getName() + "Territory"))
            .setStyle("-fx-background-color:" + guiPlayer.getColor());
      }
    }
  }

}
