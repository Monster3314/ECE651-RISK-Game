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
import javafx.scene.control.Label;

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

    @FXML
    Label hint;

    private Parent roomPane;
    private Parent loginPane;
    private Map<Integer, Room> rooms;
    private String username;
    private Map<Integer, Integer> imageToRoom;
    //private GameController gameController;

    public RoomPaneController(Parent loginPane, Map<Integer, Room> rooms, String username) {
        this.loginPane = loginPane;
        this.rooms = rooms;
        this.username = username;
        imageToRoom = new HashMap<>();
    }

    public void setRoomPane(Parent roomPane) {
        this.roomPane = roomPane;
    }

    public void initialize() {
        int num = 1;
        for(Integer i : rooms.keySet()) {
            if(num > 4) break;
            imageToRoom.put(num, i);
            roomPane.lookup("#image" + num).setVisible(true);
            num++;
        }
    }

    @FXML
    void seeSnap(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();
        int roomNumber = imageToRoom.get(Integer.parseInt(text.charAt(4) + ""));
        Room r = rooms.get(roomNumber);
        r.getGameController().disableButtonsButLogout();
        roomPane.getScene().setRoot(r.getGamePane());
    }

    @FXML
    void connect(ActionEvent event) throws IOException, ClassNotFoundException {
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

        int roomnum = imageToRoom.get(Integer.parseInt(((Button) event.getSource()).getText()));


        ObjectOutputStream oos = new ObjectOutputStream(roomSocket.getOutputStream());
        oos.writeObject(roomnum);
        oos.flush();
        oos.reset();
        oos.writeObject(username);

        ObjectInputStream ois = new ObjectInputStream(roomSocket.getInputStream());
        String result = (String) ois.readObject();

        if(result.equals("succ")) {
            GameIO io = new GameIO(roomSocket);
            GameController cc = rooms.get(roomnum).getGameController();
            cc.setGameIO(io);
            cc.setRoomPane(roomPane);
            roomPane.getScene().setRoot(rooms.get(roomnum).getGamePane());
            cc.reconnect();
        }
        if(result.equals("fail")) {
            hint.setText("Cannot open the Room");
        }

    }

    @FXML
    void newgame(ActionEvent event) throws IOException, ClassNotFoundException {
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

        int roomnum = (int) ois.readObject();

        GameIO gameIO = new GameIO(ois, oos);
        String color = gameIO.recvColor();
        GameInfo gi = gameIO.recvGame();
        GUIPlayer guiPlayer = new GUIPlayer(color, gi);

        GameController gameController = new GameController(guiPlayer, gameIO);

        URL xmlResource = getClass().getResource("/ui/main.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);
        loadControllers(loader, gameController);

        Parent gp = loader.load();

        gameController.initializeGame();

        gameController.setRoomPane(roomPane);

        Room temproom = new Room(guiPlayer, gameController, gp);

        rooms.put(roomnum, temproom);

        if(imageToRoom.size() == 4) {
            imageToRoom.put(1,roomnum);
        } else {
            imageToRoom.put(imageToRoom.size() + 1, roomnum);
        }

        roomPane.getScene().setRoot(gp);
    }

    @FXML
    void logout(ActionEvent event) {
        roomPane.getScene().setRoot(loginPane);
    }

    private void loadControllers(FXMLLoader loader, GameController gameController) {
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
