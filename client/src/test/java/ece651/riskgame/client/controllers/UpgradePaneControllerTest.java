package ece651.riskgame.client.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import ece651.riskgame.client.GUIPlayer;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class UpgradePaneControllerTest {

  private GameController gameController;
  private Pane pane;
  private UpgradePaneController upgradePaneController;
  private GUIPlayer guiPlayer;
  private TopBarController topBarController;

  @Start
  private void start(Stage stage) {
    pane = new Pane();
    guiPlayer = mock(GUIPlayer.class);
    gameController = mock(GameController.class);
    gameController.guiPlayer = guiPlayer;
    topBarController = mock(TopBarController.class);
    upgradePaneController = new UpgradePaneController();
    upgradePaneController.pane = pane;
    upgradePaneController.gameController = gameController;
    gameController.topBarController = topBarController;
  }

  @AfterAll
  public static void finish() {
    System.out.println("finish");
  }


  @Test
  public void test_updateUpgradePane() throws IOException, ClassNotFoundException {
    MenuButton mb = new MenuButton();
    mb.setId("territory");
    pane.getChildren().add(mb);
    upgradePaneController.updateUpgradePane();
    verify(gameController).updateMenuButton(any(), any());
  }

  @Test
  public void test_setUpgradePane() {
    pane.getChildren().clear();
    MenuButton from = new MenuButton("From");
    from.setId("from");
    MenuButton to = new MenuButton("To");
    to.setId("to");
    pane.getChildren().addAll(from, to);
    upgradePaneController.setUpgradePane();    
  }
  
  @Test
  public void test_submitAction() throws IOException, ClassNotFoundException {
    pane.getChildren().clear();
    MenuButton terr = new MenuButton("Territory");
    terr.setId("territory");
    MenuButton from = new MenuButton("From");
    from.setId("from");
    MenuButton to = new MenuButton("To");
    to.setId("to");
    TextField f1 = new TextField("1");
    f1.setId("number");
    pane.getChildren().addAll(terr, from,to,  f1);

    
    // level not match
    upgradePaneController.submitAction();
    verify(gameController).updateHint(any());
    
    // match level, success
    from.setText("North Army");
    to.setText("Golden Company");
    upgradePaneController.submitAction();

    verify(gameController, times(2)).updateHint(any());

    // negative number
    f1.setText("-1");
    upgradePaneController.submitAction();
    verify(gameController, times(3)).updateHint(any());
    f1.setText("1");
    
    // not pass rule checker
    doReturn("no").when(gameController.guiPlayer).tryApplyAction(any());
    upgradePaneController.submitAction();
    verify(gameController, times(4)).updateHint(any());
    
  }
}
