package ece651.riskgame.client.controllers;

import java.util.List;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.shared.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
  
  public GameController(GUIPlayer p) {
    guiPlayer = p;
    username = p.getColor();
  }

  public void setScene(Scene s) {
    scene = s;
  }

  @FXML
  public void showTerritoryInfo(MouseEvent me) {
    String territoryName = ((Button)me.getSource()).getText();
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

    int unitNum = guiPlayer.getGame().getBoard().getTerritory(territoryName).getUnits().get(0).getNum();  // territory is ensured to be found,  but units are not ensured to have
    Label text = new Label("30");
    text.setMaxWidth(Double.MAX_VALUE);
    text.setAlignment(Pos.CENTER);
    text.setFont(new Font(15));
    hbox.getChildren().addAll(labelUnit, text);
    HBox.setHgrow(labelUnit, Priority.ALWAYS);
    HBox.setHgrow(text, Priority.ALWAYS);
    
    infoView.getChildren().addAll(label, hbox);
  }

  public void updateTerritoryColors() {
    for (String color: guiPlayer.getGame().getClans().keySet()) {
      for (Territory t: guiPlayer.getGame().getClans().get(color).getOccupies()) {
        //System.out.println(t.getName());
        ((Button)scene.lookup("#"+t.getName())).setStyle("-fx-background-color: red;");
      }
    }
    // TODO
    
  }
  
}
