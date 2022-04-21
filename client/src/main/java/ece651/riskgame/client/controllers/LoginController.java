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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController {

    final String ip = "0.0.0.0";

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    private UserInit userInit;
    private RoomPaneController roomPaneController;
    private Parent loginPane;
    //private Socket serverSocket;
    //private ObjectOutputStream oos;
    //private ObjectInputStream ois;

    public LoginController(UserInit userInit) {
        this.userInit = userInit;
    }

    public void setLoginPane(Parent loginPane) {
        this.loginPane = loginPane;
    }

    /*
    public void setSocket(Socket serverSocket) throws IOException, ClassNotFoundException {
        this.serverSocket = serverSocket;
        this.oos = new ObjectOutputStream(serverSocket.getOutputStream());
        this.ois = new ObjectInputStream(serverSocket.getInputStream());
        String s = (String) ois.readObject();
    }
    */

    @FXML
    void login(ActionEvent event) throws IOException, ClassNotFoundException {
        int port = 1651;
        // connect to server
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ip);
            System.exit(1);
        } catch (ConnectException e) {
            System.err.println("Can not connect to server. Please contact 984-377-9836.");
            System.exit(1);
        }
        System.out.println("Connection Estabilished");

        userInit.setIs_login(true);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());

        ObjectOutputStream oos = new ObjectOutputStream(serverSocket.getOutputStream());

        oos.writeObject(userInit);
        oos.flush();
        oos.reset();

        ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
        String result = (String) ois.readObject();
        if(result.equals("yes")) {

            @SuppressWarnings("unchecked")
            List<Integer> roomNums = (ArrayList<Integer>) ois.readObject();
            @SuppressWarnings("unchecked")
            List<GameInfo> gameInfos = (ArrayList<GameInfo>) ois.readObject();
            @SuppressWarnings("unchecked")
            List<String> colorInfo = (ArrayList<String>) ois.readObject();

            serverSocket.close();
            Map<Integer, Room> rooms = new HashMap<>();

            for(int i = 0; i < roomNums.size(); i++) {
                GUIPlayer guiPlayer = new GUIPlayer(colorInfo.get(i), gameInfos.get(i));
                GameController gameController = new GameController(guiPlayer);

                URL xmlResource = getClass().getResource("/ui/main.fxml");

                FXMLLoader loader = new FXMLLoader(xmlResource);
                loadControllers(loader, gameController);

                Parent gp = loader.load();

                gameController.displayGame();

                Room room = new Room(guiPlayer, gameController, gp);

                rooms.put(roomNums.get(i), room);
            }

            roomPaneController = new RoomPaneController(loginPane, rooms, username.getText());
            URL roomXML = getClass().getResource("/ui/roomPane.fxml");
            FXMLLoader roomLoader = new FXMLLoader(roomXML);
            loadControllers(roomLoader);
            Parent roomPane = roomLoader.load();

            roomPaneController.setRoomPane(roomPane);
            roomPaneController.initializeRoomPane();
            loginPane.getScene().setRoot(roomPane);
        }

    }

    @FXML
    void register(ActionEvent event) throws IOException, ClassNotFoundException {
        int port = 1651;
        // connect to server
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ip);
            System.exit(1);
        } catch (ConnectException e) {
            System.err.println("Can not connect to server. Please contact 984-377-9836.");
            System.exit(1);
        }
        System.out.println("Connection Estabilished");


        userInit.setIs_login(false);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());

        ObjectOutputStream oos = new ObjectOutputStream(serverSocket.getOutputStream());
        oos.flush();
        oos.reset();
        oos.writeObject(userInit);

        ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
        String result = (String) ois.readObject();
        System.out.println(result);
    }

    private void loadControllers(FXMLLoader loader) {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(RoomPaneController.class, roomPaneController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
    }

    private void loadControllers(FXMLLoader loader, GameController gameController) {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(GameController.class, gameController);
        controllers.put(PlacementPaneController.class, new PlacementPaneController());
        controllers.put(ActionPaneController.class, new ActionPaneController());
        controllers.put(UpgradePaneController.class, new UpgradePaneController());
        controllers.put(TopBarController.class, new TopBarController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
    }
}
