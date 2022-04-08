package ece651.riskgame.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.Socket;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.server.RiskGame;
import ece651.riskgame.shared.GameInfo;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
  
  // @BeforeEach
  private void prepare(int port) throws IOException, InterruptedException, ClassNotFoundException{
    serverT = new Thread() {
        @Override
        public void run() {
          try {
            RiskGame rg = new RiskGame(1, "new_territories.csv", "new_adj_list.csv");
            rg.run(port);
          }
          catch (Exception e) {
            System.out.println(e.getMessage());
          }
        }
      };

    serverT.start();
    Thread.sleep(150);

    Socket socket = new Socket("0.0.0.0", port);
    gameIO = new GameIO(socket);
    String color = gameIO.recvColor();
    GameInfo gi = gameIO.recvGame();
    infoView = new VBox();
    guiPlayer = new GUIPlayer(color, gi);
    gameController = new GameController(guiPlayer, gameIO);
    username = "Jon Snow";    
    hint = new Label();

    gameController.hint = hint;
  }

  //@AfterEach
  private void afterone() {
    System.out.println("1 complete");
  }
  
  //@AfterAll
  public static void finish() {
    System.out.println("finish");
  }
  
  @Start
  private void start(Stage stage) throws IOException {
    scene = new Region();
  }
  
  @Test
  public void test_SetScene() throws IOException, ClassNotFoundException, InterruptedException {
    prepare(8888);
    gameController.setScene(scene);
    assertEquals(gameController.scene.getClass(), scene.getClass());
  }

  @Test
  public void test_setHint() throws IOException, ClassNotFoundException, InterruptedException{
    prepare(8889);
    assertThrows(NullPointerException.class, () -> gameController.setHint());
  }

  @Test
  public void test_updateHint() throws IOException, ClassNotFoundException, InterruptedException {
    prepare(8890);
    gameController.updateHint("hintt");
    assertEquals("hintt", hint.getText());
  }

  private void clickDorne() {
    Platform.runLater(() -> {      
        Button b = new Button("Dorne");
        gameController.showTerritoryInfo(new MouseEvent(b, null, null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, false, false, null));      
    });
    WaitForAsyncUtils.waitForFxEvents();
  }
  
  @Test
  public void test_showTerritoryInfoInPlacement(FxRobot robot) throws IOException, ClassNotFoundException, InterruptedException {
    prepare(9900);
    gameController.infoView = infoView;
    clickDorne();    
  }

  

}
