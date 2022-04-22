package ece651.riskgame.client.controllers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Resource;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class GameControllerTest {
  private VBox infoView;
  private GameController gameController;
  private GUIPlayer guiPlayer;
  private String username;
  private Parent scene;
  private Label hint;
  private GameIO gameIO;
  private Thread serverT;

  private ActionPaneController actionPaneController;
  private PlacementPaneController placementPaneController;
  private UpgradePaneController upgradePaneController;
  private TopBarController topBarController;

  private void mockPrepare() {
    gameIO = mock(GameIO.class);
    guiPlayer = mock(GUIPlayer.class);
    scene = mock(Parent.class);
    hint = mock(Label.class);
    infoView = mock(VBox.class);
    gameController = spy(new GameController(guiPlayer, gameIO));
    gameController.hint = hint;
    gameController.infoView = infoView;
    gameController.scene = scene;
    placementPaneController = mock(PlacementPaneController.class);
    actionPaneController = mock(ActionPaneController.class);
    upgradePaneController = mock(UpgradePaneController.class);
    topBarController = mock(TopBarController.class);
    gameController.placementPaneController = placementPaneController;
    gameController.actionPaneController = actionPaneController;
    gameController.upgradePaneController = upgradePaneController;
    gameController.topBarController = topBarController;
    try {
      doNothing().when(gameController).goToNextTurn();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  private void afterone() {
    // System.out.println("1 complete");
  }

  @AfterAll
  public static void finish() {
    // System.out.println("finish");
  }

  @Start
  private void start(Stage stage) throws IOException {
    scene = new Region();
  }

  @Test
  public void test_setGameIO() {
    GUIPlayer guiPlayer = mock(GUIPlayer.class);
    GameIO gameIO = mock(GameIO.class);
    GameController gameController = new GameController(guiPlayer);
    gameController.setGameIO(gameIO);
    assertSame(gameIO, gameController.gameIO);
  }
  
  @Test
  public void test_SetScene() throws IOException, ClassNotFoundException, InterruptedException {
    mockPrepare();
    gameController.setScene(null);
    assertNull(gameController.scene);
  }

  @Test
  public void test_setHint() throws IOException, ClassNotFoundException, InterruptedException {
    mockPrepare();
    gameController.setHint();
  }

  @Test
  public void test_updateHint() throws IOException, ClassNotFoundException, InterruptedException {
    mockPrepare();
    gameController.updateHint("hintt");
  }

  @Test
  public void test_showTerritoryInfoInPlacement(FxRobot robot)
      throws IOException, ClassNotFoundException, InterruptedException {
    mockPrepare();
    MouseEvent me = mock(MouseEvent.class);
    when(me.getSource()).thenReturn(new Button("durham"));
    doNothing().when(gameController).updateTerritoryInfo(any());
    gameController.showTerritoryInfo(me);
  }

  @Test
  public void test_updateTerritoryInfo() {
    mockPrepare();
    ObservableList<Node> kids = mock(ObservableList.class);
    when(infoView.getChildren()).thenReturn(kids);
    GameInfo game = mock(GameInfo.class);
    when(guiPlayer.getGame()).thenReturn(game);
    Board borad = mock(Board.class);
    doReturn(borad).when(game).getBoard();
    Territory ter = mock(Territory.class);
    when(guiPlayer.getTerritory(any())).thenReturn(ter);
    doReturn(ter).when(borad).getTerritory(any());
    when(ter.getName()).thenReturn("Durham");
    Unit unit = mock(Unit.class);
    //doReturn(unit).when(ter).getUnitByLevel(any());
    when(ter.getUnitByLevel(anyInt())).thenReturn(unit);
    when(unit.getNum()).thenReturn(10);
    Resource res = mock(Resource.class);
    doReturn(res).when(ter).getProduction();
    when(res.getResourceNum(any())).thenReturn(50);

    List<Territory> neighbors = List.of(new BasicTerritory("Durham"));
    when(borad.getNeighbors(any())).thenReturn(neighbors);
    when(infoView.lookup("#territoryName")).thenReturn(mock(Label.class));
    when(infoView.lookup("#neighborsBox")).thenReturn(mock(VBox.class));
    List.of(0,1,2,3,4,5,6).stream().forEach(i ->
            when(infoView.lookup("#unitNum"+i)).thenReturn(mock(Label.class)));
    when(infoView.lookup("#goldProduction")).thenReturn(mock(Label.class));
    when(infoView.lookup("#foodProduction")).thenReturn(mock(Label.class));
    when(infoView.lookup("#size")).thenReturn(mock(Label.class));
    List.of(0,1,2,3).stream().forEach(i ->
            when(infoView.lookup("#neighbor"+i)).thenReturn(mock(Label.class)));

    gameController.updateTerritoryInfo("Durham");
  }

  @Test
  public void test_updateCurrentTerritoryInfo() {
    mockPrepare();
    gameController.updateCurrentTerritoryInfo();
    when(infoView.lookup(any())).thenThrow(NullPointerException.class);
    gameController.updateCurrentTerritoryInfo();
  }

  @Test
  public void test_initializeGame() throws IOException, ClassNotFoundException {
    mockPrepare();
    when(scene.lookup("#hint")).thenReturn(new Label());
    when(scene.lookup("#playerName")).thenReturn(new Label());
    when(scene.lookup("#redTerritory")).thenReturn(new Button());
    List<String> btns = List.of("nextTurn", "switchRoom", "logout", "newRoom", "moveButton", "attackButton",
        "upgradeButton", "levelUp");
    btns.stream().forEach(s -> when(scene.lookup("#" + s)).thenReturn(new Label()));
    Map<String, Clan> clans = mock(Map.class);
    GameInfo gi = mock(GameInfo.class);
    Clan clan = mock(Clan.class);
    when(guiPlayer.getGame()).thenReturn(gi);
    when(gi.getClans()).thenReturn(clans);
    when(clans.get(any())).thenReturn(clan);
    Set<String> set = new HashSet(Arrays.asList(new String[] { "Durham" }));
    when(clans.keySet()).thenReturn(set);
    List<Territory> ts = new ArrayList(Arrays.asList(new Territory[] { new BasicTerritory("red") }));
    when(clan.getOccupies()).thenReturn(ts);
    doNothing().when(gameController).updateClouds();
    doNothing().when(gameController).updateTerritoryColors();

    gameController.initializeGame();
  }

  @Test
  public void test_createMenuItem() {
    mockPrepare();
    MenuButton mb = new MenuButton();
    gameController.createMenuItem("name", mb);
  }

  @Test
  public void test_updateMenuButton() {
    mockPrepare();
    MenuButton mb = new MenuButton();
    List<MenuItem> btns = List.of(new MenuItem(), new MenuItem());
    gameController.updateMenuButton(mb, btns);
  }

  @Test
  public void test_fromplacetoAction() {
    mockPrepare();
    doNothing().when(gameController).updateTerritoryColors();
    doNothing().when(gameController).activateButtonsAfterPlacement();
    doNothing().when(gameController).set3ActionPanesInvisible();
    doNothing().when(gameController).set3ButtonsUnselected();
    try {
      gameController.fromPlacementToAction();;
    } catch (Exception e) {
    }
  }

  @Test
  public void test_activatebuttonsAfterPlacement() {
    mockPrepare();
    when(scene.lookup(any())).thenReturn(new Label());
    gameController.activateButtonsAfterPlacement();
  }

  @Test
  public void test_set3ActionpanesInvisible() {
    mockPrepare();
    when(scene.lookup(any())).thenReturn(new Label());
    gameController.set3ActionPanesInvisible();
  }

  @Test
  public void test_set3ButtonUnselected() {
    mockPrepare();
    when(scene.lookup(any())).thenReturn(new Label());
    gameController.set3ButtonsUnselected();
  }

  @Test
  public void test_selectMovePane() {
    mockPrepare();
    when(scene.lookup(any())).thenReturn(new Label());
    gameController.selectMovePane();
    verify(actionPaneController).moveMode();
    verify(actionPaneController).updateActionPane();
  }

  @Test
  public void test_selectAttackPane() {
    mockPrepare();
    when(scene.lookup(any())).thenReturn(new Label());
    gameController.selectAttackPane();
    verify(actionPaneController).attackMode();
    verify(actionPaneController).updateActionPane();
  }

  @Test
  public void test_upgradepane() throws IOException, ClassNotFoundException{
    mockPrepare();
    when(scene.lookup(any())).thenReturn(new Label());
    gameController.selectUpgradePane();
    verify(upgradePaneController).updateUpgradePane();
  }

  @Test
  public void test_nextTurn() throws IOException, ClassNotFoundException {
    mockPrepare();
    doNothing().when(gameController).updateTerritoryColors();
    doNothing().when(gameController).isLostOrWin();
    doNothing().when(gameController).updateCurrentTerritoryInfo();
    when(scene.lookup(any())).thenReturn(new Label());
    when(scene.lookup("#levelUp")).thenReturn(new Button());
    gameController.nextTurn();
  }

  @Test
  public void test_displayGame() {
    mockPrepare();
    doNothing().when(gameController).disableButtonsButLogout();
    doNothing().when(gameController).activateButtons();
    doNothing().when(gameController).setAvailableTerritories(any(), any());
    doNothing().when(gameController).setHint();
    doNothing().when(gameController).updateTerritoryColors();
    doNothing().when(gameController).set3ActionPanesInvisible();
    doNothing().when(gameController).set3ButtonsUnselected();
    doNothing().when(gameController).updateClouds();

    gameController.displayGame();
  }
  
  @Test
  public void test_reconnect() throws ClassNotFoundException, IOException {
    mockPrepare();
    doNothing().when(gameController).disableButtonsButLogout();
    doNothing().when(gameController).activateButtons();
    doNothing().when(gameController).setAvailableTerritories(any(), any());
    doNothing().when(gameController).setHint();
    doNothing().when(gameController).updateTerritoryColors();
    doNothing().when(gameController).set3ActionPanesInvisible();
    doNothing().when(gameController).set3ButtonsUnselected();
    doNothing().when(gameController).isLostOrWin();
    doNothing().when(gameController).updateClouds();
    doNothing().when(gameController).nextTurn();
    doNothing().when(gameController).goToNextTurn();
    gameController.reconnect();
  }
  
  @Test
  public void test_idlostorwin() throws IOException, ClassNotFoundException, InterruptedException {
    mockPrepare();
    doNothing().when(gameController).updateHint(any());
    doNothing().when(gameController).disableButtonsButLogout();

    // game continue
    when(guiPlayer.isLost()).thenReturn(false);
    when(guiPlayer.isGameOver()).thenReturn(false);
    gameController.isLostOrWin();

    // win
    when(guiPlayer.isGameOver()).thenReturn(true);
    gameController.isLostOrWin();

    // lost
    when(guiPlayer.isLost()).thenReturn(true);
    gameController.isLostOrWin();

    // lost and watch
    //when(guiPlayer.isGameOver()).thenReturn(false);
    //gameController.isLostOrWin();
    //wait(20);
    //when(guiPlayer.isGameOver()).thenReturn(true);
  }

  @Test
  public void test_initialize() {
    mockPrepare();
    doReturn(new Pane()).when(scene).lookup(any());
    gameController.initialize(null, null);
  }

  @Test
  public void test_setAvilableTerritories() {
    mockPrepare();
    Button btn = new Button();
    btn.setId("21Territory");
    when(scene.lookupAll("Button")).thenReturn(Set.of(btn));
    gameController.setAvailableTerritories(btn, Set.of("22"));
  }

  /*@Test
  public void test_music() throws URISyntaxException {
    Media media = new Media(getClass().getResource("/maintitle.mp3").toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();
  }*/
}
