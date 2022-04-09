package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RoomManager implements Runnable{

    private int port;
    private serverRoom latestRoom;

    public RoomManager(int port) throws IOException {
        this.port = port;
        latestRoom = new serverRoom();
        RiskGame game = new RiskGame(2, "new_territories.csv", "new_adj_list.csv", latestRoom);
        Thread t = new Thread(game);
        t.start();
    }


    @Override
    public void run() {
        try {
            ServerSocket roomManager = new ServerSocket(port);
            while(true) {
                Socket player = roomManager.accept();
                ObjectInputStream ois = new ObjectInputStream(player.getInputStream());
                int roomnumber = (int) ois.readObject();
                String username = (String) ois.readObject();
                if(roomnumber < 0) { //start a new room
                    if(latestRoom.getNumber() != 2) {
                        latestRoom.addNewPlayer(player, username);
                        latestRoom.oisMap.put(player, ois);
                    } else {
                        latestRoom = new serverRoom();
                        latestRoom.addNewPlayer(player, username);
                        latestRoom.oisMap.put(player, ois);
                        RiskGame game = new RiskGame(2, "new_territories.csv", "new_adj_list.csv", latestRoom);
                        Thread t = new Thread(game);
                        t.start();
                    }

                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
