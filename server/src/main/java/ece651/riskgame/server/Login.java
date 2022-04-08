package ece651.riskgame.server;


import ece651.riskgame.shared.UserInit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Login implements Runnable{

    private Map<String, String> userTable;

    private BufferedWriter wr;

    private ServerSocket loginSocket;

    private Socket player;

    public Login(String tableTxt) throws IOException {
        userTable = new ConcurrentHashMap<>();
        readFile(tableTxt);
        wr = new BufferedWriter(new FileWriter(tableTxt));
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
            ObjectInputStream ois = new ObjectInputStream(ownplayer.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(ownplayer.getOutputStream());
            while(true) {
                UserInit userinfo = (UserInit) ois.readObject();
                if(userinfo.isIs_login()) {
                    if(userTable.containsKey(userinfo.getUsername())) {
                        if(userTable.get(userinfo.getUsername()).equals(userinfo.getPassword())) {
                            oos.writeObject("yes");
                            //TODO: send each room info
                            break;
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
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
