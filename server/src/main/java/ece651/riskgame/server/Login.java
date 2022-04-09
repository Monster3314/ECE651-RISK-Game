package ece651.riskgame.server;


import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.UserInit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Login implements Runnable{

    private Map<String, String> userTable;

    private BufferedWriter wr;

    private ServerSocket loginSocket;

    private Socket player;

    private Map<Integer, serverRoom> gameRooms;

    public Login(String tableTxt) throws IOException {
        userTable = new ConcurrentHashMap<>();
        readFile(tableTxt);
        wr = new BufferedWriter(new FileWriter(tableTxt));
    }

    public void setGameRooms(Map<Integer, serverRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }

    private void readFile(String tableTxt) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(tableTxt))) {
            String line = br.readLine();
            while(line != null) {
                String[] entry = line.split(",");
                userTable.put(entry[0], entry[1]);
                line = br.readLine();
            }
        }
    }

    public void startSever(int port) throws IOException {
        loginSocket = new ServerSocket(port);
        while (true) {
            player = loginSocket.accept();
            Thread t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        Socket ownplayer = player;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ownplayer.getOutputStream());
            //String s = "test";
            //oos.writeObject(s);
            ObjectInputStream ois = new ObjectInputStream(ownplayer.getInputStream());
            UserInit userinfo = (UserInit) ois.readObject();
            System.out.println(userinfo.getUsername());
            if(userinfo.isIs_login()) {
                if(userTable.containsKey(userinfo.getUsername())) {
                    if(userTable.get(userinfo.getUsername()).equals(userinfo.getPassword())) {
                        oos.writeObject("yes");

                        List<Integer> roomNums = new ArrayList<>();
                        List<GameInfo> gameInfos = new ArrayList<>();
                        List<String> colorInfo = new ArrayList<>();

                        for(Map.Entry<Integer, serverRoom> entry : gameRooms.entrySet()) {
                            serverRoom temp = entry.getValue();
                            if(temp.close_status) {
                                gameRooms.remove(entry.getKey());
                            }
                            for(Map.Entry<Socket, String> ss : temp.socketUsernameMap.entrySet()) {
                                if(ss.getValue().equals(userinfo.getUsername())) {
                                     roomNums.add(entry.getKey());
                                     gameInfos.add(temp.game.getCurrentGameInfo());
                                     colorInfo.add(temp.nameColorMap.get(userinfo.getUsername()));
                                }
                            }
                        }

                        oos.flush();
                        oos.reset();
                        oos.writeObject(roomNums);
                        oos.flush();
                        oos.reset();
                        oos.writeObject(gameInfos);
                        oos.flush();
                        oos.reset();
                        oos.writeObject(colorInfo);
                    } else {
                        oos.writeObject("no");
                    }
                } else {
                    oos.writeObject("no");
                }
            } else {  //user register
                userTable.put(userinfo.getUsername(), userinfo.getPassword());
                wr.write(userinfo.getUsername() + "," + userinfo.getPassword() + "\n");
                oos.writeObject("no");
            }
            oos.flush();
            oos.reset();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
