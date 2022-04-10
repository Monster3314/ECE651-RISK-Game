package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Resource;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
import ece651.riskgame.shared.UpgradeTechAction;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameController implements Initializable {

  @FXML
  VBox infoView;

  @FXML
  PlacementPaneController placementPaneController;
  @FXML
  ActionPaneController actionPaneController;
  @FXML
  UpgradePaneController upgradePaneController;

  String username;
  GUIPlayer guiPlayer;
  GameIO gameIO;
  Parent roomPane;

  @FXML
  Parent scene;
  @FXML
  Label hint;

  public GameController(GUIPlayer p, GameIO gameIO) {
    guiPlayer = p;
    this.gameIO = gameIO;
    username = p.getColor();
  }

  public GameController(GUIPlayer p) {
    guiPlayer = p;
    username = p.getColor();
  }

  public void setGameIO(GameIO gameIO) {
    this.gameIO = gameIO;
  }

  public void setRoomPane(Parent roomPane) {
    this.roomPane = roomPane;
  }

  public void setScene(Parent s) {
    scene = s;
  }

  public void setHint() {
    hint = (Label) scene.lookup("#hint");
  }

  public void updateHint(String s) {
    hint.setText(s);
  }

  @FXML
  public void levelUp(MouseEvent me) {
    Action lu = new UpgradeTechAction(guiPlayer.getColor());
    String result = guiPlayer.tryApplyAction(lu);
    if (result != null) {
      updateHint(result);
    } else {
      updateHint("Level up!");
      updateTopBar();
      guiPlayer.addActionToSend(lu);
      disableLevelUpButton();
    }
  }

  public void disableLevelUpButton() {
    ((Button) scene.lookup("#levelUp")).setDisable(true);
  }

  public void activateLevelUpButton() {
    ((Button) scene.lookup("#levelUp")).setDisable(false);
  }

  @FXML
  public void showTerritoryInfo(MouseEvent me) {
    String territoryName = ((Button) me.getSource()).getText();
    updateTerritoryInfo(territoryName);
  }

  /**
   * Method to update territoryinfo pane according given territory name
   */
  public void updateTerritoryInfo(String territoryName) {
    ObservableList<Node> children = infoView.getChildren();
    children.clear();
    infoView.setAlignment(Pos.TOP_CENTER);
    // territory name
    Label label = new Label(territoryName);
    label.setTextAlignment(TextAlignment.CENTER);
    label.setFont(new Font(20));
    label.setId("territoryName");
    children.add(label);
    // territory units
    for (int i = 0; i < 7; i++) {
      String armyName = Unit.NAME[i];
      HBox hbox = new HBox();
      Label labelUnit = new Label(armyName);
      labelUnit.setMaxWidth(Double.MAX_VALUE);
      labelUnit.setAlignment(Pos.CENTER_LEFT);
      labelUnit.setFont(new Font(15));

      Label text = new Label("0");
      text.setMaxWidth(Double.MAX_VALUE);
      text.setAlignment(Pos.CENTER_RIGHT);
      text.setFont(new Font(15));
      try {
        int unitNum = guiPlayer.getTerritory(territoryName).getUnitByLevel(i).getNum();
        text.setText(Integer.toString(unitNum));
      } catch (Exception e) {
      }
      hbox.getChildren().addAll(labelUnit, text);
      HBox.setHgrow(labelUnit, Priority.ALWAYS);
      HBox.setHgrow(text, Priority.ALWAYS);
      children.add(hbox);
    }
    // resource production
    Label title2 = new Label("Resource Production");
    title2.setTextAlignment(TextAlignment.CENTER);
    title2.setFont(new Font(20));
    children.add(title2);
    
    for (String resourceName : new String[] { Resource.FOOD, Resource.GOLD }) {
      HBox hbox = new HBox();
      Label labelUnit = new Label(resourceName);
      labelUnit.setMaxWidth(Double.MAX_VALUE);
      labelUnit.setAlignment(Pos.CENTER);
      labelUnit.setFont(new Font(15));
     
      Label text = new Label(Integer.toString(
          guiPlayer.getTerritory(territoryName).getProduction().getResourceNum(resourceName)));
      text.setMaxWidth(Double.MAX_VALUE);
      text.setAlignment(Pos.CENTER);
      text.setFont(new Font(15));
      hbox.getChildren().addAll(labelUnit, text);
      HBox.setHgrow(labelUnit, Priority.ALWAYS);
      HBox.setHgrow(text, Priority.ALWAYS);
      children.add(hbox);            
    }

    // territory size
    Label titleSize = new Label("Territory Size(Food Consumption)");
    titleSize.setTextAlignment(TextAlignment.CENTER);
    titleSize.setFont(new Font(20));
    children.add(titleSize);
    Label size = new Label(Integer.toString(guiPlayer.getTerritory(territoryName).getSize()));
    size.setTextAlignment(TextAlignment.CENTER);
    size.setFont(new Font(15));
    children.add(size);
    

    // neighbors
    // This should be removed in the future
    Label title3 = new Label("Neighbors");
    title3.setTextAlignment(TextAlignment.CENTER);
    title3.setFont(new Font(20));
    children.add(title3);

    for (Territory neighbor : guiPlayer.getGame().getBoard()
        .getNeighbors(guiPlayer.getTerritory(territoryName))) {
      Label neigh = new Label(neighbor.getName());
      neigh.setTextAlignment(TextAlignment.CENTER);
      neigh.setFont(new Font(15));
      children.add(neigh);
    }
  }

  public void updateCurrentTerritoryInfo() {
    try {
      updateTerritoryInfo(((Label) infoView.lookup("#territoryName")).getText());
    } catch (NullPointerException e) {
    }
  }

  public void initializeGame() throws IOException, ClassNotFoundException {
    setUsername(scene, guiPlayer.getColor());
    setAvailableTerritories(scene, guiPlayer.getTerritoryNames());
    disableButtonsInPlacement();
    placementPaneController.setPlacementPaneLabels();
    upgradePaneController.setUpgradePane();
    setHint();

    updateTerritoryColors();
  }

  /**
   * Set username of the game
   */
  public void setUsername(Parent scene, String name) {
    ((Labeled) scene.lookup("#playerName")).setText(name);
  }

  /**
   * Update food, gold, food
   */
  public void updateTopBar() {
    System.out.println("topbar update");
    System.out.println(guiPlayer.getFood());
    ((Label) scene.lookup("#playerFood")).setText("Food: " + Integer.toString(guiPlayer.getFood()));
    ((Label) scene.lookup("#playerGold")).setText("Gold: " + Integer.toString(guiPlayer.getGold()));
    ((Label) scene.lookup("#playerLevel")).setText("Level: " + Integer.toString(guiPlayer.getTechLevel()));
  }

  /**
   * Set available territories based on game ppl
   */
  public void setAvailableTerritories(Parent scene, Set<String> names) {
    Set<Node> nodes = scene.lookupAll("Button");

    nodes.stream().filter(node -> (node.getId() != null))
        .filter(node -> ((Button) node).getId().toString().endsWith("Territory"))
        .filter(node -> !names.contains(((Button) node).getText())).forEach(node -> {
          node.setVisible(false);
        });
  }

  /**
   * Disable all buttons except for logout
   */
  public void disableButtonsButLogout() {
    List<String> btns = new ArrayList<>(
        Arrays.asList("nextTurn", "moveButton", "attackButton", "upgradeButton", "levelUp"));
    btns.stream().forEach(s -> scene.lookup("#" + s).setDisable(true));
  }

  /**
   * Disable buttons in placement phase
   */
  public void disableButtonsInPlacement() {
    disableButtonsButLogout();
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

  public MenuItem createMenuItem(String name, MenuButton button) {
    MenuItem mb = new MenuItem(name);
    mb.setOnAction(a -> {
      button.setText(name);
    });
    return mb;
  }

  /**
   * Helper function to update MenuButton with given contents
   */
  public void updateMenuButton(MenuButton mb, List<MenuItem> contents) {
    mb.getItems().clear();
    contents.stream().forEach(c -> mb.getItems().add(c));
  }

  /**
   * Helper function to call function from placement phase to action phase
   */
  public void fromPlacementToAction() throws IOException, ClassNotFoundException {
    updateTerritoryColors();
    activateButtonsAfterPlacement();
    set3ButtonsUnselected();
    set3ActionPanesInvisible();
    updateTopBar();
  }

  /**
   * Active button after placement phase
   */
  public void activateButtons() {
    List<String> btns = new ArrayList<>(
        Arrays.asList("nextTurn", "logout", "moveButton", "attackButton", "upgradeButton", "levelUp"));
    btns.stream().forEach(s -> scene.lookup("#" + s).setDisable(false));
  }

  /**
   * Active button after placement phase
   */
  public void activateButtonsAfterPlacement() {
    activateButtons();
  }

  @FXML
  public void selectMovePane() {
    // set upgrade to invisible and action to visible
    set3ActionPanesInvisible();
    scene.lookup("#actionPane").setVisible(true);
    set3ButtonsUnselected();
    scene.lookup("#moveButton").setStyle("-fx-background-color: #ab7011");
    actionPaneController.moveMode();
    actionPaneController.updateActionPane();
  }

  @FXML
  public void selectAttackPane() {
    set3ActionPanesInvisible();
    scene.lookup("#actionPane").setVisible(true);
    set3ButtonsUnselected();
    scene.lookup("#attackButton").setStyle("-fx-background-color: #ab7011");
    actionPaneController.attackMode();
    actionPaneController.updateActionPane();
  }

  @FXML
  public void selectUpgradePane() throws IOException, ClassNotFoundException {
    set3ActionPanesInvisible();
    scene.lookup("#upgradePane").setVisible(true);
    set3ButtonsUnselected();
    scene.lookup("#upgradeButton").setStyle("-fx-background-color: #ab7011");
    upgradePaneController.updateUpgradePane();
  }

  /**
   * Method to set placmentPane, actionpane and upgradePane invisible
   */
  public void set3ActionPanesInvisible() {
    scene.lookup("#upgradePane").setVisible(false);
    scene.lookup("#placementPane").setVisible(false);
    scene.lookup("#actionPane").setVisible(false);
  }

  /**
   * Method to set move, attack, upgrade buttons to unselected mode
   */
  public void set3ButtonsUnselected() {
    scene.lookup("#moveButton").setStyle("-fx-background-color: #af9468");
    scene.lookup("#attackButton").setStyle("-fx-background-color: #af9468");
    scene.lookup("#upgradeButton").setStyle("-fx-background-color: #af9468");
  }

  @FXML
  public void nextTurn() throws IOException, ClassNotFoundException {
    gameIO.sendActions(guiPlayer.getActionsToSend());
    guiPlayer.clearActionsToSend();
    guiPlayer.updateGame(gameIO.recvGame());
    // update game
    updateTerritoryColors();
    set3ActionPanesInvisible();
    set3ButtonsUnselected();
    updateCurrentTerritoryInfo();
    updateTopBar();
    activateLevelUpButton();
    // TODO update level
    isLostOrWin();
  }

  /**
   * Display color and game info from 
   */
  public void displayGame() {
    disableButtonsButLogout();
    setUsername(scene, guiPlayer.getColor());
    setAvailableTerritories(scene, guiPlayer.getTerritoryNames());
    setHint();

    upgradePaneController.setUpgradePane();
    // update/display information
    updateTopBar();
    updateTerritoryColors();
    set3ActionPanesInvisible();
    set3ButtonsUnselected();
  }
  
  public void reconnect() throws ClassNotFoundException, IOException {    
    guiPlayer.updateGame(gameIO.recvGame());
    displayGame();
    activateButtons();
    // check lose or win
    isLostOrWin();
  }

  public void isLostOrWin() throws IOException, ClassNotFoundException {
    if (guiPlayer.isLost()) {
      disableButtonsButLogout();
      updateHint("Woops. You have lost.");
      while (!guiPlayer.isGameOver()) {
        guiPlayer.updateGame(gameIO.recvGame());
        // TODO update everything and do 2 threads
      }
    } else if (guiPlayer.isGameOver()) { // winner
      updateHint("Congratulations! You are the winner");
      disableButtonsButLogout();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    placementPaneController.gameController = this;
    placementPaneController.pane = (Pane) scene.lookup("#placementPane");
    actionPaneController.gameController = this;
    actionPaneController.pane = (Pane) scene.lookup("#actionPane");
    upgradePaneController.gameController = this;
    upgradePaneController.pane = (Pane) scene.lookup("#upgradePane");
  }

  @FXML
  public void logout() throws IOException {
    if(gameIO == null) scene.getScene().setRoot(roomPane);
    else {
      gameIO.close();
      scene.getScene().setRoot(roomPane);
    }
  }

}
