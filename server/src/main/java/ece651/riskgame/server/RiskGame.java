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
   * A map of sockets and their ids
   */
  private Map<Socket, Integer> sockets;
  private List<String> colors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Pink"));

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
    sockets = new HashMap<Socket, Integer>();
    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    board = new Board();
    playerNumber = playerNum;
    players = new HashMap<>();
    
    mapGenerator.apply(board, playerNum);
  }

  private void initClan(int id) {
    String color = colors.get(id-1);
    List<Territory> occupies = new ArrayList<>();
    occupies.add(board.getTerritoriesList().get(id*3 - 3));
    occupies.add(board.getTerritoriesList().get(id*3 - 2));
    occupies.add(board.getTerritoriesList().get(id*3 - 1));
    Clan c = new Clan(occupies);
    players.put(color, c);
  }

  /**
   * Wait for players to login to start the game
   */
  private void waitForPlayers(ServerSocket ss, int playerNum) throws IOException{
    for (int i = 1;i <= playerNum; i++) { // what if player exits while waiting for other players
      Socket socket = ss.accept();
      sockets.put(socket, i);
      initClan(i);
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
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      oos.writeObject(gi);
    }
  }

  public void run() throws IOException{    
    ServerSocket ss = new ServerSocket(1651);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    sendGameInfo(getCurrentGameInfo());    
    
    ss.close();
  }    
}
