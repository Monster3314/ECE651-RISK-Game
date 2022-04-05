package ece651.riskgame.client.models;

import javafx.beans.property.StringProperty;

public class TerritoryInfo {
  private StringProperty info;

  public StringProperty getInfo() {
    return info;
  }

  public void setInfo(StringProperty info) {
    this.info = info;
  }
}
