package ece651.riskgame.client.controllers;

import ece651.riskgame.client.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.util.Map;


public class RoomPaneController {

    @FXML
    private Button logout;

    @FXML
    private Button room;

    private Parent roomPane;
    private Parent loginPane;
    private Map<Integer, Room> rooms;

    public RoomPaneController(Parent loginPane, Map<Integer, Room> rooms) {
        this.loginPane = loginPane;
        this.rooms = rooms;
    }

    public void setRoomPane(Parent roomPane) {
        this.roomPane = roomPane;
    }

    @FXML
    void game(ActionEvent event) {
        Room r = rooms.get(1);
        roomPane.getScene().setRoot(r.getGamePane());
    }

    @FXML
    void logout(ActionEvent event) {
        roomPane.getScene().setRoot(loginPane);
    }

}
