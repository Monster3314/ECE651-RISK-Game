package ece651.riskgame.client.controllers;

import ece651.riskgame.client.models.TerritoryInfo;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MapButtonController implements EventHandler {

  @FXML
  ListView<String> territoryList;

  TerritoryInfo demoInfo;

  // TerritoryInformation might be needed, use string for now

  public MapButtonController(TerritoryInfo sp) {
    this.demoInfo = sp;
  }  

  @Override
  public void handle(Event event) {
    demoInfo.getInfo().set("clicked on " + territoryList.getSelectionModel().getSelectedItem());
  }  
}
