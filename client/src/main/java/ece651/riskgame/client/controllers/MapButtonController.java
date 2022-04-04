package ece651.riskgame.client.controllers;

import org.w3c.dom.events.MouseEvent;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

public class MapButtonController {

  StringProperty demoInfo;

  // TerritoryInformation might be needed, use string for now

  public MapButtonController(StringProperty sp) {
    this.demoInfo = sp;
  }
  
  @FXML
  public void onTerritoryButton(MouseEvent ae) {
    demoInfo.set("This is territory 007 ");
  }

  
}
