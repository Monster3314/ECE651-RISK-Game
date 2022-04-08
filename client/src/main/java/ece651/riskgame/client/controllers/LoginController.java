package ece651.riskgame.client.controllers;

import com.sun.javafx.stage.EmbeddedWindow;
import ece651.riskgame.client.GUIPlayer;
import ece651.riskgame.client.GameIO;
import ece651.riskgame.client.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.UserInit;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    private UserInit userInit;
    private GameController gameController;
    private RoomPaneController roomPaneController;
    private Parent loginPane;

    public LoginController(UserInit userInit) {
        this.userInit = userInit;
    }

    public void setLoginPane(Parent loginPane) {
        this.loginPane = loginPane;
    }

    @FXML
    void login(ActionEvent event) throws IOException, ClassNotFoundException {
        userInit.setIs_login(true);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());

        String ip = "0.0.0.0";
        //String ip = "vcm-25372.vm.duke.edu";
        int port = 1651;
        // connect to server
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Can not connect to server. Please contact 984-377-9836.");
            System.exit(1);
        }
        System.out.println("Connection Estabilished");

        GameIO gameIO = new GameIO(serverSocket);
        String color = gameIO.recvColor();
        GameInfo gi = gameIO.recvGame();
        GUIPlayer guiPlayer = new GUIPlayer(color, gi);

        gameController = new GameController(guiPlayer, gameIO);

        URL xmlResource = getClass().getResource("/ui/main.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);
        loadControllers(loader);

        Parent gp = loader.load();

        initialize();

        Room fisrt = new Room(guiPlayer, gameController, gp);
        Map<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, fisrt);

        roomPaneController = new RoomPaneController(loginPane, rooms);
        URL roomXML = getClass().getResource("/ui/roomPane.fxml");
        FXMLLoader roomLoader = new FXMLLoader(roomXML);
        loadControllers(roomLoader);
        Parent roomPane = roomLoader.load();
        roomPaneController.setRoomPane(roomPane);

        loginPane.getScene().setRoot(roomPane);
    }

    @FXML
    void register(ActionEvent event) {
        userInit.setIs_login(false);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());
    }

    private void initialize() throws IOException, ClassNotFoundException {
        gameController.initializeGame();
    }


    private void loadControllers(FXMLLoader loader) {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(GameController.class, gameController);
        controllers.put(PlacementPaneController.class, new PlacementPaneController());
        controllers.put(ActionPaneController.class, new ActionPaneController());
        controllers.put(RoomPaneController.class, roomPaneController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
    }

}
