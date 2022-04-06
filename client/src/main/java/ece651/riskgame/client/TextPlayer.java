package ece651.riskgame.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.ActionRuleChecker;
import ece651.riskgame.shared.AdjacentTerritoryChecker;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.EnemyTerritoryChecker;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.MovePathChecker;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
import ece651.riskgame.shared.UnitsRuleChecker;
/**
 * Textplayer is a class on client side which is responsible for reading initial placements, reading actions, applying actions locally, and spectating.
 */
public class TextPlayer {
  private final String color;
  private GameInfo theGame;
  private GameView view;
  final BufferedReader inputReader;
  final PrintStream out;

  final ArrayList<String> actionChoices;
  final HashMap<String, Supplier<Action>> actionReadingFns;
  final HashMap<Class, ActionRuleChecker> actionCheckers;

  // DEMO CODE
  private RiscApplication riscApplication;
  // END DEMO CODE

  /**
   * Construct the TextPlayer using Standard I/O
   * @param color is the color of the player, "Red", "Blue", etc
   * @param g is the GameInfo achieved from server  
   */
  public TextPlayer(String color, GameInfo g) {
    this(color, g, new BufferedReader(new InputStreamReader(System.in)), System.out);

    //DEMO CODE
    riscApplication = new RiscApplication();
    // END
  }
  
  /**
   * Construct the TextPlayer using specified I/O
   * @param color is the color of the player, "Red", "Blue", etc
   * @param g is the GameInfo achieved from server
   * @param input is hte reader which is used to fetch instructions from player
   * @param out is to print view and prompt to  
   */
  public TextPlayer(String color, GameInfo g, BufferedReader input, PrintStream out) {
    this.color = color;
    this.theGame = g;
    this.view = new GameTextView(theGame);
    this.inputReader = input;
    this.out = out;

    this.actionChoices = new ArrayList<String>();
    this.actionReadingFns = new HashMap<String, Supplier<Action>>();
    this.actionCheckers = new HashMap<Class, ActionRuleChecker>();
    setupActionList();
    setupActionReadingMap();
    setupActionCheckers();
  }

  /**
   * Check if the player is lost
   * @return true if player is lost, false if game is not over or player wins 
   */  
  public boolean isLost() {
    return !theGame.getPlayers().get(color).isActive();
  }
  /**
   * Check if the game is over
   * @return true if game is over
   */
  public boolean isGameOver() {
    return !(theGame.getWinner() == null); 
  }
  /**
   * Update current Game according to the GameInfo recieved
   * @param latestGame is the latest GameInfo recieved from server
   */
  public void update(GameInfo latestGame) {
    theGame = latestGame;
    view = new GameTextView(theGame);
  }

  /**
   * choose the action type and read it from input, then apply locally
   * new action candidate should be added to actionCheckers, actionChoices, actionReadingFns
   * @return a list of valid actions
   */
  public List<Action> readActionsPhase() throws IOException {
    List<Action> actions = new ArrayList<Action>();
    while (true) {
      Action currentAction = readOneAction();
      if (currentAction != null) {
        String msg = tryAct(currentAction, actionCheckers.get(currentAction.getClass()));
        if (msg == null) {
          actions.add(currentAction);
        }
        else {
          printErrorMsg(msg);
        }
      }
      //"(D)one" entered
      else {
        break;
      }
    }
    return actions;
  }

  /**
   * select and read a unchecked action from input
   * @throws IOException when nothing fetched from input(Standard input or BufferedReader)
   * @return a action specified by the user, validity unchecked  
   */  
  public Action readOneAction() throws IOException {
    printInfo(view.displayGame());
    StringBuilder prompt = new StringBuilder();
    while(true) {
      prompt.append("You are the " + color + " player, what would you like to do?");
      for (String actionType: actionChoices) {
        prompt.append("\n" + actionType);
      }
      printPromptMsg(prompt.toString());
      String inputAction;
      inputAction = inputReader.readLine();
      try {
        return actionReadingFns.get(inputAction).get();
      } catch (NullPointerException e) {
        StringBuilder line = new StringBuilder();
        line.append("Your action should be within");
        for (String choice:actionChoices) {
          line.append(" " + choice);
        }
        line.append(".");
        printErrorMsg(line.toString());
      }
    }
  }
    
  /**
   * read a Move action from terminal, validity unchecked
   * @throws IOException is caught because lambda doesn't support
   * @return a valid move action specified by the user
   */
  public Move readMove() {
    while (true)  {
      try {
        Territory src = readTerritory("Which territory do you want to move unit from?");
        Unit toMove = readUnit(src, "How many units do you want to move?");
        Territory dst = readTerritory("Which territory do you want to move unit to?");
        return new Move(toMove, src.getName(), dst.getName(), this.color);

      } catch (IOException e) {
        //out.println();
      }
    }
  }
  /**
   * try to apply a action on client side
   * @param toAct is the action you want to apply
   * @param checker is the rulechecker used to check the rule of specified action
   * @return error message if action is invalid, otherwise null
   */
  public String tryAct(Action toAct, ActionRuleChecker checker) {
    String msg = checker.checkAction(theGame, toAct);
    if (msg == null) {
      toAct.clientApply(theGame);
    }
    return msg;
  }
  
  /**
   * read a valid Attack action from terminal validity unchecked
   * @throws IOException caught because lambda doesn't support
   * @return a valid attack action specified by the user
   */
  public Attack readAttack() {
    while (true){
      try {
        Territory src = readTerritory("Which territory do you want to attack from?");
        Territory dst = readTerritory("Which territory do you want to attack?");
        Unit toAttack = readUnit(src, "How many units do you want to dispatch?");
        return new Attack(toAttack, src.getName(), dst.getName(), color);
      } catch (IOException e) {
      }
    }
  }  
  
  /**
   * read Unit to move/attack from terminal
   * @param src is the source territory to move unit from
   * @throws IOException when nothing fetched from input (STD input or BufferedReader)
  */
  public Unit readUnit(Territory src, String prompt) throws IOException{
    List<Unit> units = src.getUnits();
    String s;
    int readNumber;
    while (true) {
      //TODO:Support multiple units
      Unit u = units.get(0);
      StringBuilder promptMsg = new StringBuilder();
      promptMsg.append("You have " + Integer.toString(u.getNum()) + " units.\n");
      promptMsg.append(prompt + "\n");
      printPromptMsg(prompt.toString());

      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      try {
        readNumber = Integer.parseInt(s);
        if (readNumber <= u.getNum()) {
          return new BasicUnit(readNumber);
        }
        else {
          printErrorMsg("You don't have enough units.");
        }
      } catch (NumberFormatException e) {
        printErrorMsg("You should type a valid positive number.");
      } catch (IllegalArgumentException e) {
        printErrorMsg("You should not type a negative number.");
      }
    }
    
  }

  /**
   * read Territory from the input
   * @param prompt is the prompt message when reading territory  
   * @throws IOException when nothing fetched from terminal  
   */
  public Territory readTerritory(String prompt) throws IOException {
    String s;
    Board b = theGame.getBoard();
    while (true) {
      printPromptMsg(prompt);
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      try {
        return b.getTerritory(s);
      } catch (IllegalArgumentException e) {
        printErrorMsg(s + " is not one of the existing territories");
      }
    }
  }

  /**
   * read a initial placement from the input, validity unchecked
   * @return a move action which move units from "unassigned" to target territory 
   */
  public Move readPlacement() throws IOException{
    Territory src = theGame.getBoard().getTerritory("unassigned");
    printInfo("You have " + view.displayUnits(src.getUnits()) + "to place.");
    Territory dst = readTerritory("Which territory do you want to place?");
    Unit placed = readUnit(src, "How many units do you place?");
    return new Move(placed, "unassigned", dst.getName(), this.color);
  }

  /**
   * read mulitple placement in placement phase
   * @return a list of valid move
   * @throws IOException when nothing fetched from input
   */
  public List<Move> readPlacementPhase(List<Unit> toPlace) throws IOException {
    printInfo(view.displayGame() + "You are the " + color + " player.");
    
    Territory unassigned = new BasicTerritory("unassigned");
    unassigned.addUnitList(toPlace);
    Clan myClan = theGame.getPlayers().get(color);
    theGame.getBoard().addTerritory(unassigned);
    theGame.getBoard().putEntry(unassigned, myClan.getOccupies());
    myClan.addTerritory(unassigned);
    
    List<Move> placements = new ArrayList<Move>();    
    
    while (!unassigned.isEmpty()) {
      Move placement = readPlacement();
      String msg = tryAct(placement, actionCheckers.get(placement.getClass()));
      if (msg == null) {
        placements.add(placement);
      }
      else {
        printErrorMsg(msg);
      }
    }
    return placements;
  }

  /**
   * print game result and close I/O after game over
   * @throws IOException when failed to close I/O  
   * @throws IllegalStateException when game is not over  
   */
  public void doGameOverPhase() throws IOException, IllegalStateException{
    if (isGameOver()) {
      printInfo(view.displayWinner());
      inputReader.close();
      out.close();
    }
    else {
      throw new IllegalStateException("Game is not over yet.");
    }
  }

  /**
   * player spectate for one round
   */
  public void doOneSpectation() {
    printInfo(view.displayGame());
  }

  /**
   * fetch post peath choice from input 
   * @return Q or S based, which represents Quit or Spectate
   * @throws IOException when nothing fetched from input
   */
  //TODO:multiple post death choices
  public String getPostDeathChoice() throws IOException{
    while (true) {
      StringBuilder prompt = new StringBuilder();
      prompt.append("You are dead. What would you like to do?\n");
      prompt.append("(S)pectate\n");
      prompt.append("(Q)uit");
      printPromptMsg(prompt.toString());
      String choice = inputReader.readLine();
      if (choice.equals("S") || choice.equals("Q")) {
        return choice;
      }
      else {
        printErrorMsg("Please choose from (S)pectate (Q)uit");
      }
    }
  }

  /**
   * helper function used to get the player's occupies outside this class
   * this function is used to adapt a list of placements(move) to the map which maps territory name to a list of units  
   * @return a list of territory which is this player's occupies  
   */  
  public List<Territory> getOccupies() {
    return theGame.getClans().get(color).getOccupies();
  }
  /**
   * setup the list of action candidates when doing action phase
   */
  protected void setupActionList() {
    actionChoices.add("(M)ove");
    actionChoices.add("(A)ttack");
    actionChoices.add("(D)one");
  }
  /**
   * setup the map which maps the action selected to the action reading function
   */
  //TODO:Lambda can't throw any Exception, so we catch all possible exceptions
  protected void setupActionReadingMap() {
    actionReadingFns.put("M", () -> readMove());
    actionReadingFns.put("A", () -> readAttack());
    actionReadingFns.put("D", () -> {
      return null;
    });
  }
  
  /**
   * setup the map which maps the action class to the rulechecker
   */
  //TODO:add tryapply to action interface
  protected void setupActionCheckers() {
    actionCheckers.put(Attack.class, new UnitsRuleChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(null))));
    actionCheckers.put(Move.class, new MovePathChecker(new UnitsRuleChecker(null)));
  }

  /**
   * print error message with two lines of symbol around  
   */
  public void printErrorMsg(String msg) {
    out.println("****************************************************");
    out.println(msg);
    out.println("****************************************************");
  }

  /**
   * print prompt message with two lines of symbol around 
   */
  public void printPromptMsg(String prompt) {
    out.println("==========================");
    out.println(prompt);
    out.println("==========================");
  }
  /**
   * print game state message with two lines of symbol around  
   */
  public void printInfo(String info) {
    out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    out.println(info);
    out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
  }

}
