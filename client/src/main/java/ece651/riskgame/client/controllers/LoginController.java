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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
    private RoomPaneController roomPaneController;
    private Parent loginPane;
    private Socket serverSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginController(UserInit userInit) {
        this.userInit = userInit;
    }

    public void setLoginPane(Parent loginPane) {
        this.loginPane = loginPane;
    }

    public void setSocket(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.oos = new ObjectOutputStream(serverSocket.getOutputStream());
        this.ois = new ObjectInputStream(serverSocket.getInputStream());
    }

    @FXML
    void login(ActionEvent event) throws IOException, ClassNotFoundException {
        userInit.setIs_login(true);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());

        oos.writeObject(userInit);
        oos.flush();
        oos.reset();

        String result = (String) ois.readObject();
        if(result.equals("yes")) {
            serverSocket.close();

            Room first = new Room();
            Map<Integer, Room> rooms = new HashMap<>();
            rooms.put(1, first);

            roomPaneController = new RoomPaneController(loginPane, rooms, username.getText());
            URL roomXML = getClass().getResource("/ui/roomPane.fxml");
            FXMLLoader roomLoader = new FXMLLoader(roomXML);
            loadControllers(roomLoader);
            Parent roomPane = roomLoader.load();
            roomPaneController.setRoomPane(roomPane);

            loginPane.getScene().setRoot(roomPane);
        }

        /*
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

        */

    }

    @FXML
    void register(ActionEvent event) throws IOException, ClassNotFoundException {
        userInit.setIs_login(false);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());

        oos.flush();
        oos.reset();
        oos.writeObject(userInit);

        String result = (String) ois.readObject();
    }

    private void loadControllers(FXMLLoader loader) {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(RoomPaneController.class, roomPaneController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
    }
}
