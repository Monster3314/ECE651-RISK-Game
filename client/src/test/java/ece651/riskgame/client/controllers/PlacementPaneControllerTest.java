package ece651.riskgame.client.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import ece651.riskgame.client.GUIPlayer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class PlacementPaneControllerTest {

  private GameController gameController;
  private Pane pane;
  private PlacementPaneController placementPaneController;
  private GUIPlayer guiPlayer;

  @Start
  private void start(Stage stage) {
    pane = new Pane();
    guiPlayer = mock(GUIPlayer.class);
    gameController = mock(GameController.class);
    gameController.guiPlayer = guiPlayer;
    placementPaneController = new PlacementPaneController();
    placementPaneController.pane = pane;
    placementPaneController.gameController = gameController;
  }

  @Test
  public void test_submitPlacement() throws IOException, ClassNotFoundException, InterruptedException {
    // successful situation
    pane.getChildren().clear();
    for (int i = 1; i <= 3; i++) {
      Label label = new Label("label" + i);
      label.setId("label" + i);
      TextField tf = new TextField();
      tf.setId("field" + i);
      tf.setText("");
      pane.getChildren().addAll(label, tf);
    }
    placementPaneController.submitPlacement(null);

    
    // successful situation
    pane.getChildren().clear();
    for (int i = 1; i <= 3; i++) {
      Label label = new Label("label" + i);
      label.setId("label" + i);
      TextField tf = new TextField();
      tf.setId("field" + i);
      tf.setText("" + i);
      pane.getChildren().addAll(label, tf);
    }
    placementPaneController.submitPlacement(null);

    // make GUIplayer throws IOException
    doThrow(IOException.class).when(guiPlayer).sendPlacements(any());
    placementPaneController.submitPlacement(null);

    // make guiplayer.tryplace fail
    when(guiPlayer.tryPlace(any())).thenReturn("wrong");
    placementPaneController.submitPlacement(null);

  }

}
