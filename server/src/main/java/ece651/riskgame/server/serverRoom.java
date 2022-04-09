package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class serverRoom {
    Map<Socket, String> sockets;
    Map<Socket, ObjectOutputStream> oosMap;
    Map<Socket, ObjectInputStream> oisMap;
    Map<String, String> nameColorMap;
    Map<String, Boolean> online;
    Map<Socket, String> socketUsernameMap;
    private int number = 0;

    public serverRoom() {
        this.sockets = new ConcurrentHashMap<>();
        this.oosMap = new ConcurrentHashMap<>();
        this.oisMap = new ConcurrentHashMap<>();
        this.nameColorMap = new ConcurrentHashMap<>();
        this.online = new ConcurrentHashMap<>();
        this.socketUsernameMap = new ConcurrentHashMap<>();
    }

    public void addNewPlayer(Socket player, String username) throws IOException {
        sockets.put(player, "");
        //oosMap.put(player, new ObjectOutputStream(player.getOutputStream()));
        //oisMap.put(player, new ObjectInputStream(player.getInputStream()));
        socketUsernameMap.put(player, username);
        number ++;
    }

    public void addoldPlayer(Socket player, String username) throws IOException {
        String color = nameColorMap.get(username);
        sockets.put(player, color);
        oosMap.put(player, new ObjectOutputStream(player.getOutputStream()));
        oisMap.put(player, new ObjectInputStream(player.getInputStream()));
        online.put(color, true);
    }

    public int getNumber() {
        return number;
    }
}
