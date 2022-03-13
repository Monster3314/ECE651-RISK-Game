package ece651.riskgame.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ece651.riskgame.shared.*;

public class RiskGame {
  /**
   * A map of sockets and their colors
   */
  private Map<Socket, String> sockets;
  private Map<Socket, ObjectOutputStream> oosMap;
  private Map<Socket, ObjectInputStream> oisMap;

  /**
   * The board(map) of game, storing all territories and their adjacencies
   */
  private World world;
  private int playerNumber;
  
  /**
   * Constructor with specifying player number
   */
  public RiskGame(int playerNum) throws IOException {
    playerNumber = playerNum;
    world = new World(playerNum);
    sockets = new HashMap<Socket, String>();
    oosMap = new HashMap<Socket, ObjectOutputStream>();
  }

  /**
   * Initialize each connected player, send color to client
   */
  private void initPlayers() throws IOException, IllegalAccessException {
    for (Socket socket: sockets.keySet()) {
      String color = world.addClan();
      ObjectOutputStream oos = oosMap.get(socket);
      oos.writeObject(color);
    }
  }
  
  /**
   * Wait for players to connect
   */
  private void waitForPlayers(ServerSocket ss, int playerNum) throws IOException{
    for (int i = 0;i < playerNum; i++) { // what if player exits while waiting for other players
      Socket socket = ss.accept();
      sockets.put(socket, null);
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      oosMap.put(socket, oos);
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      oisMap.put(socket, ois);
    }
  }

  /**
   * Get the GameInfo of current round/initialization.
   */
  private GameInfo getCurrentGameInfo() {
    return world.getGameInfo();
  }

  /**
   * send gameInfo to all players(not exited)
   */
  private void sendGameInfo(GameInfo gi) throws IOException {
    for (Socket socket: sockets.keySet()) {      
      ObjectOutputStream oos = oosMap.get(socket);
      oos.writeObject(gi);
    }
  }

  private void assignUnits(int unitNumber) throws IOException, ClassNotFoundException{
    for(Map.Entry<Socket, String> player : sockets.entrySet()) {
      Clan clan = players.get(player.getValue());
      List<Unit> needToAssign = new ArrayList<>();
      for(int i = 0; i < unitNumber; i++) {
        needToAssign.add(new BasicUnit());
      }
      ObjectOutputStream oos = oosMap.get(player.getKey());
      oos.writeObject(needToAssign);

      ObjectInputStream ois = oisMap.get(player.getKey());
      Map<Territory, List<Unit>> assignResult = (Map<Territory, List<Unit>>) ois.readObject();

      for (Territory t : clan.getOccupies()) {
        if (assignResult.containsKey(t)) {
          t.addUnitList(assignResult.get(t));
        } else throw new IllegalArgumentException("player " + player.getValue() + " did not assign units to " + t.getName() +"!");
      }
    }
  }

  public void run(int port) throws IOException, ClassNotFoundException, IllegalArgumentException {
    ServerSocket ss = new ServerSocket(port);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    initPlayers();  // assign color and territories for each player
    sendGameInfo(getCurrentGameInfo());    // send a initial board without unit number to client
    assignUnits(10);
    ss.close();
  }    
}
