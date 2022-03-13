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
  private List<String> colors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Pink"));
  private Map<Socket, ObjectOutputStream> oosMap;
  private Map<Socket, ObjectInputStream> oisMap;

  /**
   * The board(map) of game, storing all territories and their adjacencies
   */
  private Board board;
  private MapGenerator mapGenerator;
  private int playerNumber;
  private Map<String, Clan> players;
  
  /**
   * Constructor with specifying player number
   */
  public RiskGame(int playerNum) throws IOException {
    sockets = new HashMap<Socket, String>();
    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    board = new Board();
    playerNumber = playerNum;
    players = new HashMap<>();
    oosMap = new HashMap<Socket, ObjectOutputStream>();
    
    mapGenerator.apply(board, playerNum);
  }

  /**
   * Allocate color and territories for each clan
   */
  private void initClan(String color) {
    int id = colors.indexOf(color);
    List<Territory> occupies = new ArrayList<>();
    occupies.add(board.getTerritoriesList().get(id*3));
    occupies.add(board.getTerritoriesList().get(id*3+1));
    occupies.add(board.getTerritoriesList().get(id*3+2));
    Clan c = new Clan(occupies);
    players.put(color, c);
  }

  /**
   * Initialize each player, send their clan color to them and 
   * record their output stream
   */
  private void initPlayers() throws IOException{
    for (Socket socket: sockets.keySet()) {
      String color = sockets.get(socket);
      initClan(color);
      ObjectOutputStream oos = oosMap.get(socket);
      oos.writeObject(color);
      
    }
  }
  
  /**
   * Wait for players to login to start the game
   */
  private void waitForPlayers(ServerSocket ss, int playerNum) throws IOException{
    for (int i = 0;i < playerNum; i++) { // what if player exits while waiting for other players
      Socket socket = ss.accept();
      sockets.put(socket, colors.get(i));
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
    GameInfo gi = new GameInfo(board, players);
    return gi;
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

  public void run(int port) throws IOException, ClassNotFoundException {
    ServerSocket ss = new ServerSocket(port);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    initPlayers();  // assign color and territories for each player
    sendGameInfo(getCurrentGameInfo());    // send a initial board without unit number to client
    assignUnits(10);
    ss.close();
  }    
}
