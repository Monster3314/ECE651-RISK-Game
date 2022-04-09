package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class RoomManager implements Runnable{

    private int port;
    private serverRoom latestRoom;
    private int latestRoomNum;
    private Map<Integer, serverRoom> gameRooms;
    private int playerNum = 2;

    public RoomManager(int port) throws IOException {
        this.port = port;
        latestRoom = new serverRoom();
        latestRoomNum = 0;
        RiskGame game = new RiskGame(playerNum, "new_territories.csv", "new_adj_list.csv", latestRoom);
        latestRoom.setRiskgame(game);
        Thread t = new Thread(game);
        t.start();
    }

    public void setGameRooms(Map<Integer, serverRoom> gameRooms) {
        this.gameRooms = gameRooms;
        gameRooms.put(latestRoomNum, latestRoom);
        latestRoomNum++;
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
                    if(latestRoom.getNumber() != playerNum) {
                        latestRoom.addNewPlayer(player, username);
                        latestRoom.oisMap.put(player, ois);
                    } else {
                        latestRoom = new serverRoom();
                        latestRoom.addNewPlayer(player, username);
                        latestRoom.oisMap.put(player, ois);
                        gameRooms.put(latestRoomNum, latestRoom);
                        RiskGame game = new RiskGame(playerNum, "new_territories.csv", "new_adj_list.csv", latestRoom);
                        latestRoom.setRiskgame(game);
                        Thread t = new Thread(game);
                        t.start();
                    }

                } else {
                    if(gameRooms.containsKey(roomnumber) && !gameRooms.get(roomnumber).close_status) {
                        serverRoom temp = gameRooms.get(roomnumber);
                        String tempcolor = temp.nameColorMap.get(username);
                        temp.sockets.put(player, tempcolor);
                        temp.oisMap.put(player, ois);
                        ObjectOutputStream oos = new ObjectOutputStream(player.getOutputStream());
                        temp.oosMap.put(player, oos);
                        temp.online.put(tempcolor, true);
                        String result = "succ";
                        oos.writeObject(result);
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(player.getOutputStream());
                        String result = "fail";
                        oos.writeObject(result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
