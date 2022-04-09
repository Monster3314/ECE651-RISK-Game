package ece651.riskgame.client.controllers;

import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.client.Room;
import ece651.riskgame.shared.GameInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class RoomPaneController {

    @FXML
    private Button logout;

    @FXML
    private Button room;

    private Parent roomPane;
    private Parent loginPane;
    private Map<Integer, Room> rooms;
    private String username;
    private GameController gameController;

    public RoomPaneController(Parent loginPane, Map<Integer, Room> rooms, String username) {
        this.loginPane = loginPane;
        this.rooms = rooms;
        this.username = username;
    }

    public void setRoomPane(Parent roomPane) {
        this.roomPane = roomPane;
    }

    @FXML
    void game(ActionEvent event) throws IOException, ClassNotFoundException {
        String ip = "0.0.0.0";
        int port = 1653;
        // connect to server
        Socket roomSocket = null;
        try {
            roomSocket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Can not connect to server. Please contact 984-377-9836.");
            System.exit(1);
        }
        System.out.println("Connection Estabilished");

        ObjectOutputStream oos = new ObjectOutputStream(roomSocket.getOutputStream());

        int roomnumber = -1;
        oos.writeObject(roomnumber);
        oos.flush();
        oos.reset();

        oos.writeObject(username);
        oos.flush();
        oos.reset();

        ObjectInputStream ois = new ObjectInputStream(roomSocket.getInputStream());
        GameIO gameIO = new GameIO(ois, oos);
        String color = gameIO.recvColor();
        GameInfo gi = gameIO.recvGame();
        GUIPlayer guiPlayer = new GUIPlayer(color, gi);

        gameController = new GameController(guiPlayer, gameIO);

        URL xmlResource = getClass().getResource("/ui/main.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);
        loadControllers(loader);

        Parent gp = loader.load();

        initialize();

        roomPane.getScene().setRoot(gp);
    }

    @FXML
    void logout(ActionEvent event) {
        roomPane.getScene().setRoot(loginPane);
    }

    private void initialize() throws IOException, ClassNotFoundException {
        gameController.initializeGame();
    }

    private void loadControllers(FXMLLoader loader) {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(GameController.class, gameController);
        controllers.put(PlacementPaneController.class, new PlacementPaneController());
        controllers.put(ActionPaneController.class, new ActionPaneController());
        controllers.put(UpgradePaneController.class, new UpgradePaneController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
    }
}
