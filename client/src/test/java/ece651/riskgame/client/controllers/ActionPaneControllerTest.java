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
    pane.getChildren().addAll(from, to);
    for (int i = 1;i <=7; i++) {
      TextField f = new TextField(""+i);
      f.setId("field"+i);
      pane.getChildren().add(f);
    }
    actionPaneController.moveMode();
    actionPaneController.submitAction();
    
    actionPaneController.attackMode();
    actionPaneController.submitAction();

    // not pass rule checker
    when(gameController.guiPlayer.tryApplyAction(any())).thenReturn("wrong");
    actionPaneController.submitAction();

    // nothing to commit
    pane.getChildren().clear();
    MenuButton from1 = new MenuButton("US");
    from1.setId("from");
    MenuButton to1 = new MenuButton("China");
    to1.setId("to");
    pane.getChildren().addAll(from1, to1);
    for (int i = 1;i <=7; i++) {
      TextField f = new TextField("");
      f.setId("field"+i);
      pane.getChildren().add(f);
    }    
    actionPaneController.submitAction();

    // negative number
    pane.getChildren().clear();
    MenuButton from2 = new MenuButton("US");
    from2.setId("from");
    MenuButton to2 = new MenuButton("China");
    to2.setId("to");
    pane.getChildren().addAll(from2, to2);
    for (int i = 1;i <=7; i++) {
      TextField f = new TextField("-"+i);
      f.setId("field"+i);
      pane.getChildren().add(f);
    }
    actionPaneController.submitAction();
  }

}
