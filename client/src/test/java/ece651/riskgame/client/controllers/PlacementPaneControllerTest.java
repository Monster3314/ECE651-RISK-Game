package ece651.riskgame.client.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Territory;
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
  private GameIO gameIO;

  @Start
  private void start(Stage stage) {
    pane = new Pane();
    guiPlayer = mock(GUIPlayer.class);
    gameIO = mock(GameIO.class);
    gameController = mock(GameController.class);
    gameController.guiPlayer = guiPlayer;
    gameController.gameIO = gameIO;
    placementPaneController = new PlacementPaneController();
    placementPaneController.pane = pane;
    placementPaneController.gameController = gameController;
  }

  
  @AfterAll
  public static void finish() {
    System.out.println("finish");
  }

  @Test
  public void testSetPlacementPaneLabels() throws ClassNotFoundException, IOException {
    Set<Territory> set = new HashSet<> ();
    set.add(new BasicTerritory("shitland"));
    when(guiPlayer.getOccupies()).thenReturn(set);
    Label label = new Label();
    label.setId("label1");
    Label title = new Label("titlee");
    title.setId("title");
    pane.getChildren().addAll(label, title);
    when(gameIO.recvUnitsToPlace()).thenReturn(new ArrayList<>(Arrays.asList(new BasicUnit(3))));
    placementPaneController.setPlacementPaneLabels();
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
    doThrow(IOException.class).when(gameIO).sendPlacements(any());;
    placementPaneController.submitPlacement(null);

    // make guiplayer.tryplace fail
    when(guiPlayer.tryPlace(any())).thenReturn("wrong");
    placementPaneController.submitPlacement(null);

  }

}
