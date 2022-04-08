package ece651.riskgame.client.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
  Parent scene;
  Label hint;

  // flags
  boolean ifMoveInAction = true;
  
  public GameController(GUIPlayer p) {
    guiPlayer = p;
    username = p.getColor();
  }

  public void setScene(Parent s) {
    scene = s;
  }

  public void setHint() {
    hint = (Label) scene.lookup("#hint");
  }

  @FXML
  public void showTerritoryInfo(MouseEvent me) {
    String territoryName = ((Button) me.getSource()).getText();
    try {
      updateTerritoryInfo(territoryName);
    }
    catch (IndexOutOfBoundsException e) {
      hint.setText("You cannot see territory details before placement");
    }
  }

  /**
   * Method to update
   */
  private void updateTerritoryInfo(String territoryName) {
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
    String msg = null;
    try {
      String from = ((MenuButton)scene.lookup("#actionPane #from")).getText();
      String to = ((MenuButton)scene.lookup("#actionPane #to")).getText();

      List<Action> acts = new ArrayList<Action>();
      
      // TODO check all fields
      int num = Integer.parseInt(((TextField) scene.lookup("#actionPane #field1")).getText());
    
      Action act = this.ifMoveInAction ? new Move(new BasicUnit(num), from, to, guiPlayer.getColor()) : new Attack(new BasicUnit(num), from, to, guiPlayer.getColor());
      String result = guiPlayer.tryApplyAction(act);
      if (result != null) {
        msg = result;
      }
      else {
        acts.add(act);
        guiPlayer.sendActions(acts);
        // TODO update territory info
        msg = "Action submitted!";
      }
    } // end try
    catch (NumberFormatException e) {
      msg = "Assign numbers please";
    }
    hint.setText(msg);
  }

  public void initializeGame() throws IOException, ClassNotFoundException {
    setUsername(scene, guiPlayer.getColor());
    setAvailableTerritories(scene, guiPlayer.getTerritoryNames()); 
    disableButtonsInPlacement();    
    setPlacementPaneLabels();
    setHint();

    updateTerritoryColors();
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
  public void setUsername(Parent scene, String name) {
    ((Labeled) scene.lookup("#playerName")).textProperty().setValue(name);
  }

  /**
   * Set available territories based on game ppl
   */
  private void setAvailableTerritories(Parent scene, Set<String> names) {
    Set<Node> nodes = scene.lookupAll("Button");
    
    nodes.stream().filter(node -> (node.getId() != null))
      .filter(node -> ((Button) node).getId().toString().endsWith("Territory"))
      .filter(node -> !names.contains(((Button)node).getText())).forEach(node -> {
        node.setVisible(false);
        });
  }

  /**
   * Disable button in placement phase to avoid undefined actions
   */
  private void disableButtonsInPlacement() {
    List<String> btns = new ArrayList<>(Arrays.asList("nextTurn", "switchRoom", "logout", "newRoom", "moveButton", "attackButton", "upgradeButton", "levelUp"));
    btns.stream().forEach(s -> scene.lookup("#"+s).setDisable(true));
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
    MenuButton from = (MenuButton) scene.lookup("#actionPane #from");
    MenuButton to = (MenuButton) scene.lookup("#actionPane #to");
    // update two drop down menus
    updateMenuButton(from, guiPlayer.getOccupies().stream().map(t -> createMenuItem(t.getName(), from)).collect(Collectors.toList()));
    if (ifMoveInAction) {
      updateMenuButton(to, guiPlayer.getOccupies().stream().map(t -> createMenuItem(t.getName(), to)).collect(Collectors.toList()));
    } else { // attack
      updateMenuButton(to, guiPlayer.getEnemyTerritoryNames().stream().map(t -> createMenuItem(t, to)).collect(Collectors.toList()));
    }
    // TODO : advanced feature, adjust unit numbers based on selected territory
  }

  private MenuItem createMenuItem(String name, MenuButton button) {
    MenuItem mb = new MenuItem(name);
    mb.setOnAction(a -> {
        button.setText(name);
      });
    return mb;
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
    updateTerritoryColors();
    activateButtonsAfterPlacement();
    set3ButtonsUnselected();
    set3ActionPanesInvisible();
  }

  /**
   * Active button after placement phase
   */
  private void activateButtonsAfterPlacement() {
    List<String> btns = new ArrayList<>(Arrays.asList("nextTurn", "switchRoom", "logout", "newRoom", "moveButton", "attackButton", "upgradeButton", "levelUp"));
    btns.stream().forEach(s -> scene.lookup("#"+s).setDisable(false));
  }

  @FXML
  public void selectMovePane() {
    // set upgrade to invisible and action to visible
    set3ActionPanesInvisible();
    scene.lookup("#actionPane").setVisible(true);
    set3ButtonsUnselected();
    scene.lookup("#moveButton").setStyle("-fx-background-color: #ab7011");
    ifMoveInAction = true;
    updateActionPane();
  }

  @FXML
  public void selectAttackPane() {
    set3ActionPanesInvisible();
    scene.lookup("#actionPane").setVisible(true);
    set3ButtonsUnselected();
    scene.lookup("#attackButton").setStyle("-fx-background-color: #ab7011");
    ifMoveInAction = false;
    updateActionPane();
  }

  @FXML
  public void selectUpgradePane() {
    set3ActionPanesInvisible();    
    scene.lookup("#upgradePane").setVisible(true);
    set3ButtonsUnselected();
    scene.lookup("#upgradeButton").setStyle("-fx-background-color: #ab7011");
    // TODO update upgrade pane
  }

  /**
   * Method to set placmentPane, actionpane and upgradePane invisible
   */
  private void set3ActionPanesInvisible() {
    scene.lookup("#upgradePane").setVisible(false);
    scene.lookup("#placementPane").setVisible(false);
    scene.lookup("#actionPane").setVisible(false);
  }
  
  /**
   * Method to set move, attack, upgrade buttons to unselected mode
   */
  private void set3ButtonsUnselected() {
    scene.lookup("#moveButton").setStyle("-fx-background-color: #af9468");
    scene.lookup("#attackButton").setStyle("-fx-background-color: #af9468");
    scene.lookup("#upgradeButton").setStyle("-fx-background-color: #af9468");
  }

  @FXML
  public void nextTurn() throws IOException, ClassNotFoundException {
    guiPlayer.doEndOfTurn();
    // check when or lose

    //update game
    updateTerritoryColors();
    set3ActionPanesInvisible();
    set3ButtonsUnselected();
    // update territory info
    // TODO update level
  }
  
}
