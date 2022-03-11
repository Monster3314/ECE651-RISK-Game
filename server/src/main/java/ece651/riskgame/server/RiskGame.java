package ece651.riskgame.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class RiskGame {
  /**
   * A map of sockets and their colors
   */
  private Map<Socket, String> sockets;
  private List<String> colors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Pink"));
  private Map<Socket, ObjectOutputStream> oosMap;

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

  public void run(int port) throws IOException{    
    ServerSocket ss = new ServerSocket(port);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    initPlayers();  // assign color and territories for each player
    sendGameInfo(getCurrentGameInfo());    // send a initial board without unit number to client
    
    ss.close();
  }    
}
