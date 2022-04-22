package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import ece651.riskgame.shared.*;

public class RiskGame implements Runnable{

  private Map<Socket, String> sockets; // A map of sockets and their colors
  private Map<Socket, ObjectOutputStream> oosMap;
  private Map<Socket, ObjectInputStream> oisMap;
  private Map<String, Boolean> online;
  private Map<String, String> nameColorMap;

  /**
   * The board(map) of game, storing all territories and their adjacencies
   */
  private World world;
  private int playerNumber;
  private ActionRuleChecker MoveActionChecker = new MovePathChecker(new UnitsRuleChecker(new SufficientResourceChecker(null)));
  private ActionRuleChecker AttackActionChecker = new UnitsRuleChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientResourceChecker(null))));
  private ActionRuleChecker upgradeUnitChecker = new SufficientUnitChecker(new SufficientResourceChecker(null));
  private ActionRuleChecker upgradeTechChecker = new SufficientResourceChecker(null);

  private ActionRuleChecker getColakChecker = new SufficientResourceChecker(new AbilityChecker(null));

  private ActionRuleChecker doColakChecker = new SufficientResourceChecker(new AbilityChecker(null));
  private serverRoom roominfo;
  private Logger logger = Logger.getInstance();

  /**
   * Constructor with specifying player number
   */
  public RiskGame(int playerNum, String territoryListFile, String adjacencyListFile, serverRoom roominfo) throws IOException {
    playerNumber = playerNum;
    world = new World(playerNum, territoryListFile, adjacencyListFile);
    sockets = roominfo.sockets;
    oosMap = roominfo.oosMap;
    oisMap = roominfo.oisMap;
    online = roominfo.online;
    nameColorMap = roominfo.nameColorMap;
    this.roominfo = roominfo;
  }

  
  /**
   * Constructor with specifying player number
   */
  @Deprecated
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
      nameColorMap.put(roominfo.socketUsernameMap.get(socket), color);
      //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      //oosMap.put(socket, oos);
      oosMap.get(socket).writeObject(color);
    }
    logger.flushBuffer();
  }

  /**
   * Wait for players to connect
   */
  private void waitForPlayers(int playerNum) throws IOException {
    logger.writeLog("Begin to wait for " + playerNum + " players.");
    while(sockets.size() != playerNum);  //TODO: selfspin
    logger.flushBuffer();
  }

  /**
   * Get the GameInfo of current round/initialization.
   */
  public GameInfo getCurrentGameInfo() {
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
        sockets.remove(player.getKey());
        oosMap.remove(player.getKey());
        oisMap.remove(player.getKey());
        online.put(player.getValue(), false);
        logger.writeLog("Player " + player.getValue() + " disconnect");
      }

    }
  }


  /**
   * ask players to assign their units to each territory
   * @param unitNumber the unit number that player can assign
   * @throws IOException
   * @throws ClassNotFoundException
   */
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

  /**
   * read the list of Actions from each player
   * @return the list of Actions
   */
  @SuppressWarnings("unchecked")
  private List<Action> readActions() {
    Set<String> nowUsers = new HashSet<>(sockets.values());
    List<Action> Actions = new ArrayList<>();
    for(Map.Entry<Socket, String> player : sockets.entrySet()) {
      if(!nowUsers.contains(player.getValue())|| !isAlive(player.getValue()) || !isOnline(player.getValue())) continue;
      ObjectInputStream ois = oisMap.get(player.getKey());
      try {
        Actions.addAll((List<Action>) ois.readObject());
      } catch (Exception e) {
        sockets.remove(player.getKey());
        oosMap.remove(player.getKey());
        oisMap.remove(player.getKey());
        online.put(player.getValue(), false);
        logger.writeLog("Player " + player.getValue() + " disconnect");
      }
    }
    return Actions;
  }


  /**
   * perform action including move and attack action
   */
  private void doAction() {
    List<Action> Actions = readActions();
    if(Actions.size() == 0) return;

    List<Action> movesAndUpgradeUnits = new ArrayList<>();
    List<Attack> attacks = new ArrayList<>();
    List<UpgradeTechAction> upgradeTechActions = new ArrayList<>();
    List<GetCloakAction> getCloakActions = new ArrayList<>();
    List<DoCloakAction> doCloakActions = new ArrayList<>();

    for (Action a : Actions) {
      if (a.getClass() == Attack.class) {
        attacks.add((Attack) a);
      }
      else if (a.getClass() == UpgradeTechAction.class) {
        upgradeTechActions.add((UpgradeTechAction) a);
      }
      else if (a.getClass() == GetCloakAction.class) {
        getCloakActions.add((GetCloakAction) a);
      }
      else if (a.getClass() == DoCloakAction.class) {
        doCloakActions.add((DoCloakAction) a);
      }
      else { // for coverage
        movesAndUpgradeUnits.add(a);
      }
    }

    doGetCloakAction(getCloakActions);
    doDoCloakAction(doCloakActions);
    doMoveAndUpgradeUnitAction(movesAndUpgradeUnits);
    doAttackAction(attacks);
    doUpgradeLevelAction(upgradeTechActions);


    // TODO: Send to players before flushing
    logger.flushBuffer();
  }

  private void doGetCloakAction(List<GetCloakAction> getCloakActions) {
    for(GetCloakAction action:getCloakActions) {
      String checkResult = getColakChecker.checkAction(world, action);
      if(checkResult != null) {
        return;
      }
      try {
        world.acceptAction(action);
      } catch (Exception e) {
        //logger.writeLog("");
      }
    }
  }

  private void doDoCloakAction(List<DoCloakAction> doCloakActions) {
    for(DoCloakAction action: doCloakActions) {
      String checkResult = doColakChecker.checkAction(world, action);
      if(checkResult != null) {
        //log
        return;
      }
      try {
        world.acceptAction(action);
      } catch (Exception e) {
        //TODO:log
      }
    }
  }

  private void doUpgradeLevelAction(List<UpgradeTechAction> upgradeTechActions) {
    for (UpgradeTechAction action : upgradeTechActions) {
      String checkResult = upgradeTechChecker.checkAction(world, action);
      if (checkResult != null) {
        // TODO: log
        return;
      }
      world.acceptAction(action);
    }
  }

  private void doMoveAndUpgradeUnitAction(List<Action> movesAndUpgradeUnits) {
    for (Action a : movesAndUpgradeUnits) {
      if (a.getClass() == Move.class) {
        doMoveAction((Move) a);
      }
      else {
        doUpgradeUnitAction((UpgradeUnitAction) a);
      }
    }
  }

  private void doUpgradeUnitAction(UpgradeUnitAction a) {
    String checkResult = upgradeUnitChecker.checkAction(world, a);
    if (checkResult != null) {
      // TODO: log
      return;
    }
    world.acceptAction(a);
  }

  /**
   * do move action
   * @param a the Move action
   */
  private void doMoveAction(Move a) {
    String checkResult = MoveActionChecker.checkAction(world, a);
    if (checkResult != null) {
      logger.writeLog("Discard " + a.getColor() + "'s move" + "(" + a.getFromTerritory() + ", " + a.getToTerritory()
              + ", " + a.getUnit() + ") for reason: " + checkResult);
      return;
    }
    world.acceptAction(a);
    logger.writeLog(a.getColor() + " player moves " + a.getUnit() + " from " + a.getFromTerritory() + " to " + a.getToTerritory() + ".");
  }

  /**
   * do attack actions
   * @param attackActions the list of attack actions
   */
  private void doAttackAction(List<Attack> attackActions) {
    List<Attack> validAttacks = new ArrayList<Attack>();

    for (Attack attack : attackActions) {
      String checkResult = AttackActionChecker.checkAction(world, attack);
      if (checkResult != null) {
        logger.writeLog("Discard " + attack.getColor() + "'s attack" + "(" + attack.getFromTerritory() + ", " + attack.getToTerritory()
                        + ", " + attack.getUnit() + ") for reason: " + checkResult);
        continue;
      }
      attack.onTheWay(world);
      validAttacks.add(attack);
      logger.writeLog(attack.getColor() + " player attacks " + attack.getToTerritory() + " from " + attack.getFromTerritory() + " by " + attack.getUnit() + ".");
    }

    for (Attack attack : validAttacks)
      world.acceptAction(attack);
  }

  /**
   * after each turn, each territory will get one unit
   */
  private void afterTurn() {
    for (Territory t : world.getBoard().getTerritoriesList()) {
      t.addUnit(new BasicUnit(1));
      t.decCloakNum();
    }
    for (Clan clan : world.getClans().values()) {
      if (clan.isActive()) {
        clan.afterTurn();
      }
    }
    randomDecResource();
    randomDecTroops();
  }

  private void randomDecResource() {
    Random random = new Random();
    for(Territory t : world.getBoard().getTerritoriesList()) {
      try {
        t.getProduction().costGold(random.nextInt(50));
        t.getProduction().costFood(random.nextInt(100));
      } catch (Exception e) {

      }
    }
  }

  private void randomDecTroops() {
    Random random = new Random();
    for(Territory t: world.getBoard().getTerritoriesList()) {
      try {
        int index = random.nextInt(t.getUnits().size());
        t.getUnits().get(index).decSoldiers(1);
      } catch (Exception e) {

      }
    }
  }

  /**
   * to test whether player is still alive, which means he has some territories
   * @param player the player that we want to test
   * @return the status of player
   */
  private boolean isAlive(String player) {
    return world.getClans().get(player).isActive();
  }

  /**
   * to test whether player is still online
   * @param player the player that we want to test
   * @return the online status of player
   */
  private boolean isOnline(String player) {
    return online.get(player);
  }

  /**
   * to test whether there is player in the game.
   * @return false if there is no player in the game, including not alive or not online
   */
  private boolean stillHaveAlivePlayers() {
    boolean haveAlivePlayer = false;
    for(Map.Entry<String, Clan> players: world.getClans().entrySet()) {
      haveAlivePlayer |= (isAlive(players.getKey()) && isOnline(players.getKey()));
    }

    return haveAlivePlayer;
  }

  /**
   * close the sockets that used to connect to each player
   * @throws IOException
   */
  private void closeSockets() throws IOException {
    for(Socket s: sockets.keySet()) s.close();
  }

  @Override
  public void run() {
    // only one player is allowed now
    try{
      waitForPlayers(playerNumber);
      initPlayers(); // assign color and territories for each player
      sendGameInfo(getCurrentGameInfo()); // send a initial board without unit number to client
//      printMoveCost();
      assignUnits(30);
      sendGameInfo(getCurrentGameInfo());
      GameInfo gi = getCurrentGameInfo();
      while(stillHaveAlivePlayers() && (gi.getWinner() == null)) {
        doAction();
        afterTurn();
        gi = getCurrentGameInfo();
        sendGameInfo(gi);
      }
      roominfo.close_status = true;
      closeSockets();
    } catch (Exception ignored) {
      roominfo.close_status = true;
      ignored.printStackTrace();
    }

  }

//  private void printMoveCost() {
//    for (Map.Entry<String, Clan> entry : world.getClans().entrySet()) {
//      for (Territory territory : entry.getValue().getOccupies()) {
//        System.out.println(territory.getName());
//        System.out.println(world.getUnitMoveCost(territory.getName(), entry.getKey()));
//      }
//    }
//  }
}
