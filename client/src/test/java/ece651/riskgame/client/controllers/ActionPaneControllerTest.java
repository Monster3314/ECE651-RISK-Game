package ece651.riskgame.client.controllers;

import static org.mockito.ArgumentMatchers.any;
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
public class ActionPaneControllerTest {

  private GameController gameController;
  private Pane pane;
  private ActionPaneController actionPaneController;
  private GUIPlayer guiPlayer;

  @Start
  private void start(Stage stage) {
    pane = new Pane();
    guiPlayer = mock(GUIPlayer.class);
    gameController = mock(GameController.class);
    gameController.guiPlayer = guiPlayer;
    actionPaneController = new ActionPaneController();
    actionPaneController.pane = pane;
    actionPaneController.gameController = gameController;
  }

  @AfterAll
  public static void finish() {
    System.out.println("finish");
  }

  @Test
  public void test_updateActionPane() {
    MenuButton mb = new MenuButton();
    mb.setId("from");
    pane.getChildren().add(mb);
    MenuButton mb2 = new MenuButton();
    mb2.setId("from");
    pane.getChildren().add(mb2);
    actionPaneController.updateActionPane();
    verify(gameController, times(2)).updateMenuButton(any(), any());

    actionPaneController.attackMode();
    actionPaneController.updateActionPane();
    verify(gameController, times(4)).updateMenuButton(any(), any());
  }
  
  @Test
  public void test_submitAction() throws IOException {
    MenuButton from = new MenuButton("US");
    from.setId("from");
    MenuButton to = new MenuButton("China");
    to.setId("to");
    TextField f1 = new TextField("");
    f1.setId("field1");
    pane.getChildren().addAll(from, to, f1);
    actionPaneController.submitAction();
    
    f1.setText("3");
    actionPaneController.moveMode();
    actionPaneController.submitAction();

    actionPaneController.attackMode();
    actionPaneController.submitAction();
    
    when(gameController.guiPlayer.tryApplyAction(any())).thenReturn("wrong");
    actionPaneController.submitAction();

    // find no unit
    f1.setId("1");
    actionPaneController.submitAction();
  }

}
