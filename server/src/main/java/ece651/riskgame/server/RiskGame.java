package ece651.riskgame.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ece651.riskgame.shared.GameInfo;

public class RiskGame {
  /**
   * A map of sockets and their colors
   */
  private Map<Socket, String> sockets;
  private Map<Socket, ObjectOutputStream> oosMap;

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

  public void run(int port) throws IOException, IllegalAccessException{    
    ServerSocket ss = new ServerSocket(port);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    initPlayers();  // assign color and territories for each player
    sendGameInfo(getCurrentGameInfo());    // send a initial board without unit number to client
    
    ss.close();
  }    
}
