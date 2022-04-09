package ece651.riskgame.client;

import ece651.riskgame.client.controllers.GameController;
import javafx.scene.Parent;

import java.util.Objects;

public class Room {

    private GUIPlayer guiPlayer;
    private GameController gameController;
    private Parent gamePane;

    public Room() {

    }

    public Room(GUIPlayer guiPlayer, GameController gameController, Parent gamePane) {
        this.guiPlayer = guiPlayer;
        this.gameController = gameController;
        this.gamePane = gamePane;
    }

    public GUIPlayer getGuiPlayer() {
        return guiPlayer;
    }

    public void setGuiPlayer(GUIPlayer guiPlayer) {
        this.guiPlayer = guiPlayer;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public Parent getGamePane() {
        return gamePane;
    }

    public void setGamePane(Parent gamePane) {
        this.gamePane = gamePane;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (!Objects.equals(guiPlayer, room.guiPlayer)) return false;
        if (!Objects.equals(gameController, room.gameController))
            return false;
        return Objects.equals(gamePane, room.gamePane);
    }

    @Override
    public int hashCode() {
        int result = guiPlayer != null ? guiPlayer.hashCode() : 0;
        result = 31 * result + (gameController != null ? gameController.hashCode() : 0);
        result = 31 * result + (gamePane != null ? gamePane.hashCode() : 0);
        return result;
    }
}
