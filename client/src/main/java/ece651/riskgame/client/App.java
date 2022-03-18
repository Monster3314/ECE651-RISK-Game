/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ece651.riskgame.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class App {
  private TextPlayer player;
  private ObjectInputStream socketIn;
  private ObjectOutputStream socketOut;

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws IOException {
    String ip = args[0];
    int port = -1;
    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Argument" + args[1] + " must be an integer.");
      System.exit(1);
    }
    //connect to server
    Socket serverSocket = null;
    ObjectInputStream socketIn = null;
    ObjectOutputStream socketOut = null;
    try {
      serverSocket = new Socket(ip, port);
      socketIn = new ObjectInputStream(serverSocket.getInputStream());
      socketOut = new ObjectOutputStream(serverSocket.getOutputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host: " + ip);
      System.exit(1);
    }
    System.out.println("Connection Estabilished");
    //recv allocated player color
    //recv GameInfo
    //recv Initial Units
    String color = null;
    GameInfo game = null;
    try {
      color = (String) socketIn.readObject();
      System.out.println("Color recved");
      game = (GameInfo) socketIn.readObject();
      System.out.println("Game recved");
    } catch (ClassNotFoundException e) {
      System.err.println("Class Not Found when reading Object through socket");
      System.exit(1);
    }
    TextPlayer p = new TextPlayer(color, game);
    App app = new App(p, socketIn, socketOut);

    app.doPlacementPhase();
    app.doActionPhase();

    socketIn.close();
    socketOut.close();
    serverSocket.close();
    System.exit(0);
  }
  
  public App(TextPlayer player, ObjectInputStream socketIn, ObjectOutputStream socketOut) {
    this.player = player;
    this.socketIn = socketIn;
    this.socketOut = socketOut;
  }
  private GameInfo recvGame() throws IOException{
    GameInfo game;
    try {
      game = (GameInfo) socketIn.readObject();
      return game;
    } catch (ClassNotFoundException e) {
      System.err.println("Class Not Found when reading Object through socket");
      System.exit(1);
      return null;
    }
  }
  @SuppressWarnings("unchecked")
  public void doPlacementPhase() throws IOException {
    List<Unit> toPlace = null;
    try {
      toPlace = (List<Unit>) socketIn.readObject();
      System.out.println("Units recved");
    } catch (ClassNotFoundException e) {
      System.err.println("Class Not Found when reading Object through socket");
      System.exit(1);
    }
    List<Move> placements = player.readPlacementPhase(toPlace);
    //adapting
    Map<String, List<Unit>> serverPlacements = new HashMap<>();
    List<Territory> occupies = player.getOccupies();
    for (Territory occupy : occupies) {
      serverPlacements.put(occupy.getName(), new ArrayList<Unit>());
    }
    for (Move placement: placements) {
      serverPlacements.get(placement.getDst()).add(placement.getUnit());
    }

    socketOut.writeObject(serverPlacements);
    socketOut.flush();
    socketOut.reset();
  }
  public void doActionPhase() throws IOException {
    while (true) {
      GameInfo game = recvGame();
      //TODO:player lost
      player.update(game);
      List<Action> actions = player.readActionsPhase();
      socketOut.writeObject(actions);
      System.out.println("Actions sent.");
      socketOut.flush();
      socketOut.reset();
    }
  }
} 
