package ece651.riskgame.client.models;

import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TerritoryList {
  ObservableList<String> list;

  public TerritoryList() {
    list = FXCollections.observableArrayList();    
  }

  public TerritoryList(Collection<String> data) {
    list = FXCollections.observableArrayList(data);
  }

  public ObservableList<String> getList() {
    return list;
  }

  /**
   * Replace data in list with data
   */
  public void setList(Collection<String> data) {
    list.setAll(data);
  }
}
