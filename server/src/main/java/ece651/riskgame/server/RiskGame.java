package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.GameInfo;

public class RiskGame {
  private ServerSocket serverSocket;
  private Map<Socket, Integer> sockets;

  private Board board;

  /**
   * Initialize territories based on given player number
   */
  private void initBoard(int playerNum) {
    board = new Board();
    MapGenerator.apply(board, 1);
  }

  /**
   * Wait for players to login to start the game
   */
  private void waitForPlayers(ServerSocket ss, int playerNum) throws IOException{
    for (int i = 1;i <= playerNum; i++) { // what if player exits while waiting for other players
      Socket socket = ss.accept();
      sockets.put(socket, i);
    }
  }

  /**
   * Get the GameInfo of current round/initialization.
   */
  private GameInfo getCurrentGameInfo() {
    GameInfo gi = new GameInfo(board);
    return gi;
  }

  /**
   * send gameInfo to all players(not exited)
   */
  private void sendGameInfo() throws IOException {
    for (Socket socket: sockets.keySet()) {
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      GameInfo gi = getCurrentGameInfo();
      oos.writeObject(gi);
    }
  }

  public void run() throws IOException{
    initBoard(1);
    
    ServerSocket ss = new ServerSocket(1651);
    // only one player is allowed now
    waitForPlayers(ss, 1);

    sendGameInfo();
    
    
    ss.close();
  }    
}
