package ece651.riskgame.client.controllers;

import ece651.riskgame.client.models.TerritoryInfo;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GameMapController {

  // source where we click
  @FXML
  ListView<String> territoryList;

  // target where data is changed
  TerritoryInfo demoInfo;

  // TerritoryInformation might be needed, use string for now

  public GameMapController(TerritoryInfo sp) {
    this.demoInfo = sp;
  }  

  public void showTerritoryInfo(Event event) {
    demoInfo.getInfo().set("clicked on " + territoryList.getSelectionModel().getSelectedItem());
  }  
}
