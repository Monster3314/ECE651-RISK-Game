package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RoomManager implements Runnable{

    private int port;
    private serverRoom latestRoom;

    public RoomManager(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        try {
            ServerSocket roomManager = new ServerSocket(port);
            while(true) {
                Socket player = roomManager.accept();
                ObjectInputStream ois = new ObjectInputStream(player.getInputStream());
                int roomnumber = (int) ois.readObject();
                if(roomnumber < 0) { //start a new room
                    serverRoom roominfo = new serverRoom();
                    //roominfo.addNewPlayer();
                    RiskGame game = new RiskGame(5, "new_territories.csv", "new_adj_list.csv", roominfo);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
