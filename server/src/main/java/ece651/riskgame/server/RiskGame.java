package ece651.riskgame.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ece651.riskgame.shared.*;

public class RiskGame {

  private Map<Socket, String> sockets;  //A map of sockets and their colors
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
    sockets = new HashMap<>();
    oosMap = new HashMap<>();
    oisMap = new HashMap<>();
  }

  /**
   * Initialize each connected player, send color to client
   */
  private void initPlayers() throws IOException, IllegalAccessException {
    for (Socket socket: sockets.keySet()) {
      String color = world.addClan();
      sockets.put(socket, color);
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
      oos.flush();
      oos.reset();
      oos.writeObject(gi);
    }
  }

  @SuppressWarnings("unchecked")
  private void assignUnits(int unitNumber) throws IOException, ClassNotFoundException{
    for(Map.Entry<Socket, String> player : sockets.entrySet()) {
      List<Unit> needToAssign = new ArrayList<>();
      needToAssign.add(new BasicUnit(unitNumber));
      ObjectOutputStream oos = oosMap.get(player.getKey());
      oos.writeObject(needToAssign); // send List of Unit to be allocated to client
    }
    for(Map.Entry<Socket, String> player : sockets.entrySet()) {
      Clan clan = world.getClans().get(player.getValue());
      ObjectInputStream ois = oisMap.get(player.getKey());
      Map<String, List<Unit>> assignResult = (Map<String, List<Unit>>) ois.readObject();

      for (Territory t : clan.getOccupies()) {
        if (assignResult.containsKey(t.getName())) {
          t.addUnitList(assignResult.get(t.getName()));
        }
        else {
          throw new IllegalArgumentException("player " + player.getValue() + " did not assign units to " + t.getName() +"!");
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void doMoveAction() throws IOException, ClassNotFoundException{
    List<Action> moveActions = new ArrayList<>();
    for(Map.Entry<Socket, String> player : sockets.entrySet()) {
      ObjectInputStream ois = oisMap.get(player.getKey());
      List<Action> temp = (List<Action>) ois.readObject();
      moveActions.addAll(temp);
    }

    for(Action a: moveActions) world.acceptAction(a);
  }

  public void run(int port) throws IOException, ClassNotFoundException, IllegalAccessException {
    ServerSocket ss = new ServerSocket(port);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    initPlayers();  // assign color and territories for each player
    sendGameInfo(getCurrentGameInfo());    // send a initial board without unit number to client
    assignUnits(30);
    sendGameInfo(getCurrentGameInfo());
    doMoveAction();
    sendGameInfo(getCurrentGameInfo());
    ss.close();
  }    
}
