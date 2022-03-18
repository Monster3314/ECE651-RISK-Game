package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ece651.riskgame.shared.*;

public class RiskGame {

  private Map<Socket, String> sockets; // A map of sockets and their colors
  private Map<Socket, ObjectOutputStream> oosMap;
  private Map<Socket, ObjectInputStream> oisMap;
  private Map<String, Boolean> online;

  /**
   * The board(map) of game, storing all territories and their adjacencies
   */
  private World world;
  private int playerNumber;
  private ActionRuleChecker MoveActionChecker = new MovePathChecker(new UnitsRuleChecker(null));
  private ActionRuleChecker AttackActionChecker = new UnitsRuleChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(null)));

  private Logger logger = Logger.getInstance();

  /**
   * Constructor with specifying player number
   */
  public RiskGame(int playerNum) throws IOException {
    playerNumber = playerNum;
    world = new World(playerNum);
    sockets = new HashMap<>();
    oosMap = new HashMap<>();
    oisMap = new HashMap<>();
    online = new HashMap<>();
  }

  /**
   * Initialize each connected player, send color to client
   */
  private void initPlayers() throws IOException{
    logger.writeLog("Begin to initialize players information.");
    for (Socket socket: sockets.keySet()) {
      String color = world.addClan();
      sockets.put(socket, color);
      online.put(color, true);
      ObjectOutputStream oos = oosMap.get(socket);
      oos.writeObject(color);
    }
    logger.flushBuffer();
  }

  /**
   * Wait for players to connect
   */
  private void waitForPlayers(ServerSocket ss, int playerNum) throws IOException {
    logger.writeLog("Begin to wait for " + playerNum + " players.");
    for (int i = 0; i < playerNum; i++) { // what if player exits while waiting for other players
      Socket socket = ss.accept();
      logger.writeLog("Player " + i + " connected successfully!");
      sockets.put(socket, null);
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      oosMap.put(socket, oos);
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      oisMap.put(socket, ois);
    }
    logger.flushBuffer();
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
  private void sendGameInfo(GameInfo gi) {
    for (Map.Entry<Socket, String> player: sockets.entrySet()) {
      if(!isOnline(player.getValue())) continue;
      ObjectOutputStream oos = oosMap.get(player.getKey());
      try {
        oos.flush();
        oos.reset();
        oos.writeObject(gi);
      } catch (IOException e) {
        online.put(player.getValue(), false);
      }

    }
  }

  @SuppressWarnings("unchecked")
  private void assignUnits(int unitNumber) throws IOException, ClassNotFoundException {
    for (Map.Entry<Socket, String> player : sockets.entrySet()) {
      List<Unit> needToAssign = new ArrayList<>();
      needToAssign.add(new BasicUnit(unitNumber));
      ObjectOutputStream oos = oosMap.get(player.getKey());
      oos.writeObject(needToAssign); // send List of Unit to be allocated to client
    }
    for (Map.Entry<Socket, String> player : sockets.entrySet()) {
      Clan clan = world.getClans().get(player.getValue());
      ObjectInputStream ois = oisMap.get(player.getKey());
      Map<String, List<Unit>> assignResult = (Map<String, List<Unit>>) ois.readObject();

      for (Territory t : clan.getOccupies()) {
        if (assignResult.containsKey(t.getName())) {
          List<Unit> units = assignResult.get(t.getName());
          logger.writeLog(player.getValue() + " player added " + units + " to " + t.getName() + ".");
          t.addUnitList(units);
        }
      }
    }
    logger.flushBuffer();
  }

  @SuppressWarnings("unchecked")
  private List<Action> readActions() {
    List<Action> Actions = new ArrayList<>();
    for(Map.Entry<Socket, String> player : sockets.entrySet()) {
      if(!isAlive(player.getValue()) || !isOnline(player.getValue())) continue;
      ObjectInputStream ois = oisMap.get(player.getKey());
      try {
        Actions.addAll((List<Action>) ois.readObject());
      } catch (Exception ignored) {
        online.put(player.getValue(), false);
      }
    }
    return Actions;
  }


  private void doAction() {
    List<Action> Actions = readActions();
    if(Actions.size() == 0) return;

    List<Move> moves = new ArrayList<>();
    List<Attack> attacks = new ArrayList<>();

    for (Action a : Actions) {
      if (a.getClass() == Move.class) {
        moves.add((Move) a);
        continue;
      }
      //if (a.getClass() == Attack.class) {
      else { // for coverage
        attacks.add((Attack) a);
        continue;
      }
    }

    doMoveAction(moves);
    doAttackAction(attacks);

    // TODO: Send to players before flushing
    logger.flushBuffer();
  }

  private void doMoveAction(List<Move> moveActions) {
    for (Move a : moveActions) {
      if (MoveActionChecker.checkAction(world, a) != null)
        continue;
      world.acceptAction(a);
      logger.writeLog(a.getColor() + " player moves " + a.getUnit() + " from " + a.getFromTerritory() + " to " + a.getToTerritory() + ".");
    }
  }

  private void doAttackAction(List<Attack> attackActions) {
    List<Attack> validAttacks = new ArrayList<Attack>();

    for (Attack attack : attackActions) {
      String checkResult = AttackActionChecker.checkAction(world, attack);
      if (checkResult != null) {
        continue;
      }
      attack.onTheWay(world);
      validAttacks.add(attack);
      logger.writeLog(attack.getColor() + " player attacks " + attack.getToTerritory() + " from " + attack.getFromTerritory() + " by " + attack.getUnit() + ".");
    }

    for (Attack attack : validAttacks)
      world.acceptAction(attack);
  }

  private void afterTurn() {
    for (Territory t : world.getBoard().getTerritoriesList()) {
      t.addUnit(new BasicUnit(1));
    }
  }

  private boolean isAlive(String player) {
    return world.getClans().get(player).isActive();
  }

  private boolean isOnline(String player) {
    return online.get(player);
  }

  private boolean stillHaveAlivePlayers() {
    boolean haveAlivePlayer = false;
    for(Map.Entry<String, Clan> players: world.getClans().entrySet()) {
      haveAlivePlayer |= (isAlive(players.getKey()) && isOnline(players.getKey()));
    }

    return haveAlivePlayer;
  }

  private void closeSockets() throws IOException {
    for(Socket s: sockets.keySet()) s.close();
  }

  public void run(int port) throws IOException, ClassNotFoundException, IllegalAccessException {
    ServerSocket ss = new ServerSocket(port);
    // only one player is allowed now
    waitForPlayers(ss, playerNumber);
    initPlayers(); // assign color and territories for each player
    sendGameInfo(getCurrentGameInfo()); // send a initial board without unit number to client
    assignUnits(30);
    sendGameInfo(getCurrentGameInfo());
    GameInfo gi = getCurrentGameInfo();
    while(stillHaveAlivePlayers() && (gi.getWinner() == null)) {
      doAction();
      afterTurn();
      gi = getCurrentGameInfo();
      sendGameInfo(gi);
    }
    ss.close();
    closeSockets();
  }    
}
