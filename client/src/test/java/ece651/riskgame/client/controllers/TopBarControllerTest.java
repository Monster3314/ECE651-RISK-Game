package ece651.riskgame.client.controllers;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.shared.Action;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class TopBarControllerTest {


  public Label playerName;
  public Label playerFood;
  public Label playerGold;
  public Label playerLevel;
  public Button levelUpBtn;
  public GameController gameController;
  public GUIPlayer guiPlayer;

  public TopBarController topBarController;

  public void prepare() {
    playerName = mock(Label.class);
    playerFood = mock(Label.class);
    playerGold = mock(Label.class);
    playerLevel = mock(Label.class);
    levelUpBtn = mock(Button.class);
    guiPlayer = mock(GUIPlayer.class);
    gameController = mock(GameController.class);
    topBarController = spy(new TopBarController());
    topBarController.playerLevel = playerLevel;
    topBarController.playerGold = playerGold;
    topBarController.gameController = gameController;
    topBarController.guiPlayer = guiPlayer;
    topBarController.playerFood = playerFood;
    topBarController.playerName = playerName;
    topBarController.levelUp = levelUpBtn;
  }

  @Test
  void test_levelUp() {
    prepare();

    when(guiPlayer.tryApplyAction(any())).thenReturn("good");
    topBarController.levelUp(mock(MouseEvent.class));
    verify(gameController).updateHint(any());
  }

  @Test
  void test_disableLevelUpButton() {
    //prepare();

//    topBarController.inactivateLevelUpButton();
  }

  @Test
  void tset_activateLevelUpButton() {
    prepare();
    topBarController.activateLevelUpButton();
  }

  @Test
  void test_updateTopBar() {
    prepare();
    topBarController.updateTopBar();
  }

  @Test
  void test_setUsername() {
    prepare();
    topBarController.setUsername("james");
  }
}