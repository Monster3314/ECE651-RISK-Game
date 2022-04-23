package ece651.riskgame.client.controllers;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.shared.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GameController implements Initializable {

  final List<String> territoryNameList = List.of("North", "Dorne", "Vale", "Stormlands", "Riverlands", "Reach", "Asshai", "Qarth", "Slaverbay", "Freecities", "Crownlands", "Beyondthewall", "Westerlands", "Dothrakisea", "Ironislands");

  @FXML
  VBox infoView;

  @FXML
  PlacementPaneController placementPaneController;
  @FXML
  ActionPaneController actionPaneController;
  @FXML
  UpgradePaneController upgradePaneController;
  @FXML
  TopBarController topBarController;

  GUIPlayer guiPlayer;
  GameIO gameIO;
  Parent roomPane;

  @FXML
  Parent scene;
  @FXML
  Label hint;
  @FXML
  Pane helpBox;
  @FXML
  VBox cloakBtn;
  @FXML
  Text logText;


  public GameController(GUIPlayer p, GameIO gameIO) {
    guiPlayer = p;
    this.gameIO = gameIO;
  }

  public GameController(GUIPlayer p) {
    guiPlayer = p;
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
  public void showTerritoryInfo(MouseEvent me) {
    String territoryName = ((Button) me.getSource()).getText();
    updateTerritoryInfo(territoryName);
  }

  /**
   * Method to update territoryinfo pane according given territory name
   */
  public void updateTerritoryInfo(String territoryName) {
    Label territoryNameLabel = (Label) infoView.lookup("#territoryName");
    territoryNameLabel.setText(territoryName);
    // territory units
    for (int i = 0; i < 7; i++) {
      Label numLabel = (Label)infoView.lookup("#unitNum"+i);
      numLabel.setText("0");
      try {
        int unitNum = guiPlayer.getTerritory(territoryName).getUnitByLevel(i).getNum();
        numLabel.setText(Integer.toString(unitNum));
      } catch (Exception e) {
      }
    }

    Label spyNum = (Label) infoView.lookup("#spyNum");
    spyNum.setText(String.valueOf(guiPlayer.getSpyNumOnTerritory(territoryName)));

    // set resources
    System.out.println(territoryName);
    ((Label)infoView.lookup("#goldProduction"))
            .setText(String.valueOf(guiPlayer.getTerritory(territoryName).getProduction().getResourceNum(Resource.GOLD)));
    ((Label)infoView.lookup("#foodProduction"))
            .setText(String.valueOf(guiPlayer.getTerritory(territoryName).getProduction().getResourceNum(Resource.FOOD)));
    ((Label)infoView.lookup("#size"))
            .setText(String.valueOf(guiPlayer.getTerritory(territoryName).getSize()));

    // neighbors
    VBox neighborBox = (VBox) infoView.lookup("#neighborsBox");
    List.of(0,1,2,3).stream().forEach(i -> ((Label)infoView.lookup("#neighbor"+i)).setText(""));
    int i = 0;
    for (Territory neighbor : guiPlayer.getGame().getBoard()
        .getNeighbors(guiPlayer.getTerritory(territoryName))) {
      ((Label)infoView.lookup("#neighbor"+i)).setText(neighbor.getName());
      i++;
    }

    Label cloakInfo = (Label) infoView.lookup("#cloakInfo");
    try {
      if (guiPlayer.occupyTerritory(territoryName)) {
        int remainingCloaks = guiPlayer.getTerritory(territoryName).getCloakNum();
        if (remainingCloaks <= 0) {
          cloakInfo.setText("Not cloaked");
        } else {
          cloakInfo.setText("Cloak : " + (remainingCloaks-1));
        }
      }
      else {
        cloakInfo.setText("Unknown");
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to remove clouds, used in dead player watching the game
   */
  public void removeClouds() {
    territoryNameList.stream().forEach(name -> {
      //System.out.println(name.toLowerCase()+"cloud");
      scene.lookup("#"+name.toLowerCase()+"cloud").setVisible(false);
    });
  }

  /**
   * Method called to update if a cloud shold show up
   */
  public void updateClouds() {
    territoryNameList.stream().forEach(name -> {
      //System.out.println(name.toLowerCase()+"cloud");
      scene.lookup("#"+name.toLowerCase()+"cloud").setVisible(false);
    });
    for (String territoryName: territoryNameList) {
      if (guiPlayer.getGame().getBoard().containsTerritory(territoryName)) {
        if (!guiPlayer.hasVisibilityOf(territoryName)) {
          scene.lookup("#"+territoryName.toLowerCase()+"cloud").setVisible(true);
        }
      }
    }
  }

  public void updateCurrentTerritoryInfo() {
    try {
      updateTerritoryInfo(((Label) infoView.lookup("#territoryName")).getText());
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (IllegalStateException e) {
      hint.setText("Action done. Please click on territory to show correct information.");
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      hint.setText("Action done. Please click on territory to show correct information.");
      e.printStackTrace();
    }
  }

  /**
   * Method called at the beginning of the game
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void initializeGame() throws IOException, ClassNotFoundException {
    topBarController.setUsername(guiPlayer.getColor());
    setAvailableTerritories(scene, guiPlayer.getTerritoryNames());
    disableButtonsInPlacement();
    placementPaneController.setPlacementPaneLabels();
    upgradePaneController.setUpgradePane();
    setHint();

    updateClouds();
    updateTerritoryColors();
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
        Arrays.asList("nextTurn", "moveButton", "attackButton", "upgradeButton"));
    btns.stream().forEach(s -> scene.lookup("#" + s).setDisable(true));
    topBarController.inactivateLevelUpButton();
    cloakBtn.setDisable(true);
  }

  /**
   * Disable buttons in placement phase
   */
  public void disableButtonsInPlacement() {
    disableButtonsButLogout();
  }

  public void disableActableButtons() {
    set3ActionPanesInvisible();
    set3ButtonsUnselected();
    disableButtonsButLogout();
    scene.lookup("#logout").setDisable(true);
  }

  /**
   * Active button after placement phase
   */
  public void activateButtons() {
    List<String> btns = new ArrayList<>(
            Arrays.asList("nextTurn", "logout", "moveButton", "attackButton", "upgradeButton"));
    btns.stream().forEach(s -> scene.lookup("#" + s).setDisable(false));
    topBarController.activateLevelUpButton();
    scene.lookup("#logout").setDisable(false);
    activateCloakDevelopButtons();
  }

  /**
   * Active button after placement phase
   */
  public void activateButtonsAfterPlacement() {
    activateButtons();
  }

  public void activateCloakDevelopButtons() {
    ImageView cloakImg = (ImageView) cloakBtn.lookup("#img");
    if (guiPlayer.hasCloakAbility()) {
      try {
        cloakImg.setImage(new javafx.scene.image.Image(getClass().getResource("/img/cloak.png").toURI().toString()));
        cloakBtn.setDisable(false);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    } else {
      if (guiPlayer.getTechLevel() >= 3) {
        cloakBtn.setDisable(false);
      } else {
        cloakBtn.setDisable(true);
      }
    }
  }

  /**
   * Update Territory colors, called at the beginning of each round
   */
  public void updateTerritoryColors() {
    for (String color : guiPlayer.getGame().getClans().keySet()) {
      for (Territory t : guiPlayer.getGame().getClans().get(color).getOccupies()) {
        Button target = ((Button) scene.lookup("#" + t.getName() + "Territory"));
        if (!scene.lookup("#"+t.getName().toLowerCase()+"cloud").isVisible()) {
          target.setStyle("-fx-background-color:" + color);
        } else {
          target.setStyle("");
        }
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
    updateClouds();
    updateTerritoryColors();
    activateButtonsAfterPlacement();
    set3ButtonsUnselected();
    set3ActionPanesInvisible();
    topBarController.updateTopBar();
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
    disableActableButtons();
    hint.setText("Your turn ends. Wait for other players...");
    Thread th = new Thread(new Task() {
      @Override
      protected Object call() throws Exception {
        gameIO.sendActions(guiPlayer.getActionsToSend());
        guiPlayer.clearActionsToSend();
        System.out.print("actions sent to server");
        guiPlayer.updateGame(gameIO.recvGame());
        System.out.println("receive actions from server");
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            try {
              goToNextTurn();
            } catch (IOException e) {
              e.printStackTrace();
            } catch (ClassNotFoundException e) {
              e.printStackTrace();
            }
          }
        });
        return null;
      }
    });
    th.setDaemon(true);
    th.start();
  }

  public void goToNextTurn() throws IOException, ClassNotFoundException {
    // update game
    hint.setText("New Turn! Do your actions!");
    updateLog();
    updateClouds();
    updateTerritoryColors();
    //updateCurrentTerritoryInfo();
    topBarController.updateTopBar();
    activateButtons();
    isLostOrWin();
  }

  /**
   * Display color and game info from 
   */
  public void displayGame() {
    disableButtonsButLogout();
    topBarController.setUsername(guiPlayer.getColor());
    setAvailableTerritories(scene, guiPlayer.getTerritoryNames());
    setHint();

    upgradePaneController.setUpgradePane();
    // update/display information
    topBarController.updateTopBar();
    updateClouds();
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
      set3ButtonsUnselected();
      set3ActionPanesInvisible();
      updateHint("Woops. You have lost.");
      Thread thread = new Thread(new Task<>() {
        @Override
        protected Object call() throws Exception {
          while (!guiPlayer.isGameOver()) {
            guiPlayer.updateGame(gameIO.recvGame());
            System.out.println("Received game from server");
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                hint.setText("Game updated");
                topBarController.updateTopBar();
                updateTerritoryColors();
              }
            });
          }
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              hint.setText("Game Over");
            }
          });
          return null;
        }
      });
      thread.setDaemon(true);
      thread.start();
      System.out.println("thread starts");
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
    topBarController.gameController = this;
    topBarController.guiPlayer = this.guiPlayer;
    //playMusic();
  }

  public void playMusic() {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        Media media = null;
        try {
          media = new Media(getClass().getResource("/maintitle.mp3").toURI().toString());
          MediaPlayer mediaPlayer = new MediaPlayer(media);
          mediaPlayer.play();
        } catch (URISyntaxException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          System.out.println("Cannot find music lol");
          e.printStackTrace();
        }
      }
    });
    thread.start();
  }

  @FXML
  public void cloak() {
    if (guiPlayer.hasCloakAbility()) {
      String territoryName = ((Label)infoView.lookup("#territoryName")).getText();
      DoCloakAction doCloakAction = new DoCloakAction(guiPlayer.getColor(), territoryName);
      String result = guiPlayer.tryApplyAction(doCloakAction);
      if (result == null) {
        guiPlayer.addActionToSend(doCloakAction);
        updateCurrentTerritoryInfo();
        activateCloakDevelopButtons();
        topBarController.updateTopBar();
        updateHint("Territory cloaked!");
      } else {
        updateHint(result);
      }
    } else {
      GetCloakAction getCloakAction  = new GetCloakAction(guiPlayer.getColor());
      String result = guiPlayer.tryApplyAction(getCloakAction);
      if (result == null) {
        guiPlayer.addActionToSend(getCloakAction);
        updateCurrentTerritoryInfo();
        activateCloakDevelopButtons();
        topBarController.updateTopBar();
        updateHint("Cloak ability developed.");
      } else {
        updateHint(result);
      }
    }
  }

  @FXML
  public void surprise() {
    scene.lookup("#surpriseImg").setVisible(true);
  }

  @FXML
  public void hideSurprise() {
    scene.lookup("#surpriseImg").setVisible(false);
  }

  @FXML
  public void logout() throws IOException {
    if(gameIO == null) scene.getScene().setRoot(roomPane);
    else {
      gameIO.close();
      scene.getScene().setRoot(roomPane);
    }
  }

  @FXML
  public void showHelp() {
    helpBox.setVisible(true);
  }

  @FXML
  public void hideHelp() {
    helpBox.setVisible(false);
  }

  public void updateLog() {
    logText.setText(guiPlayer.getMessage());
  }
}
