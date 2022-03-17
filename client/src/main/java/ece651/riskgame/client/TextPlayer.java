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
import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class TextPlayer {
  final String color;
  private GameInfo theGame;
  private GameView view;
  final BufferedReader inputReader;
  final PrintStream out;

  final ArrayList<String> actionChoices;
  final HashMap<String, Supplier<Action>> actionReadingFns;

  /**
   * Construct the TextPlayer using Standard I/O
   * @param color is the color of the player, "Red", "Blue", etc
   * @param g is the GameInfo achieved from server  
   */
  public TextPlayer(String color, GameInfo g) {
    this(color, g, new BufferedReader(new InputStreamReader(System.in)), System.out);
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
    setupActionList();
    setupActionReadingMap();
  }
  public List<Territory> getOccupies() {
    return theGame.getPlayers().get(color).getOccupies();
  }

  /**
   * Display the Game, including territory information(name, ownership, and neighbourhood) and  unit deployment
   */  
  public void display() {
    out.print(view.displayGame());
  }
  
  /**
   * Update current Game according to the GameInfo recieved
   * @param latestGame is the latest GameInfo recieved from server
   */
  public void update(GameInfo latestGame) {
    theGame = latestGame;
    view = new GameTextView(theGame);
  }
  public List<Action> readActionsPhase() throws IOException {
    List<Action> actions = new ArrayList<Action>();
    while (true) {
      Action currentAction = readOneAction();
      if (currentAction != null) {
        actions.add(currentAction);
      }
      else {
        break;
      }
    }
    return actions;
  }

  public Action readOneAction() throws IOException {
    out.println("You are the " + color + " player, what would you like to do?");
    for (String actionType: actionChoices) {
      out.println(actionType);
    }
    String inputAction;
    inputAction = inputReader.readLine();
    //TODO:inputAction invalid;
    return actionReadingFns.get(inputAction).get();
  }
    
  /**
   * read a Move action from terminal
   * @throws IOException when nothing fetched from input(Standard input or BufferedReader)
   *   
   */
  public Move readMove() {
    while (true){
      try {
        Territory src = readTerritory("Which territory do you want to move unit from?");
        Unit toMove = readUnit(src, "How many units do you want to move?");
        Territory dst = readTerritory("Which territory do you want to move unit to?");
        return new Move(toMove, src.getName(), dst.getName(), this.color);
      } catch (IOException e) {
      }
    }
  }
  
  /**
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
  */  
  
  /**
   * read Unit to move/attack from terminal
   * @param src is the source territory to move unit from
   * @throws IOException when nothing fetched from input (STD input or BufferedReader)
   * @throws IllegalArgumentException when entered number is less than the unit inside the territory    
  */
  public Unit readUnit(Territory src, String prompt) throws IOException, IllegalArgumentException{
    List<Unit> units = src.getUnits();
    if (units.size() == 0) {
      throw new IllegalArgumentException("This Territory has no unit.");      
    }
    String s;
    int moveNumber;
    while (true) {
      //TODO:Support multiple units
      Unit u = units.get(0);
      out.println("You have " + Integer.toString(u.getNum()) + " units. How many of the m do you want to move?");
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      moveNumber = Integer.parseInt(s);
      try {
        u.decSoldiers(moveNumber);
        return new BasicUnit(moveNumber);
      } catch (IllegalArgumentException e) {
        out.println("Not enough units in this territory.");
      }
    }
    
  }

  /**
   * read Territory from the terminal
   * @param prompt is the prompt message when reading territory  
   * @throws IOException when nothing fetched from terminal  
   */
  public Territory readTerritory(String prompt) throws IOException {
    String s;
    Board b = theGame.getBoard();
    while (true) {
      out.println(prompt);
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      try {
        return b.getTerritory(s);
      } catch (IllegalArgumentException e) {
        out.println(s + " is not one of the existing territories");
      }
    }
  }
  public Move readPlacement() {
    Territory src = theGame.getBoard().getTerritory("unassigned");
    out.println("You have " + view.displayUnits(src.getUnits()) + "to place.");
    while (true) {
      try {
        Territory dst = readTerritory("Which territory do you want to place?");
        Unit placed = readUnit(src, "How many units do you place?");
        return new Move(placed, "unassigned", dst.getName(), this.color);
      } catch (Exception e) {
        //out.println("You don't have enough units to place.");
      }
    }
  }
  public List<Move> readPlacementPhase(List<Unit> toPlace) throws IOException {
    Territory unassigned = new BasicTerritory("unassigned");
    Clan myClan = theGame.getPlayers().get(color);
    theGame.getBoard().addTerritory(unassigned);
    theGame.getBoard().putEntry(unassigned, myClan.getOccupies());

    List<Move> placements = new ArrayList<Move>();

    while (!unassigned.isEmpty()) {
      placements.add(readPlacement());
    }
    return placements;
  }
  protected void setupActionList() {
    actionChoices.add("(M)ove");
    actionChoices.add("(A)ttack");
    actionChoices.add("(D)one)");
  }

  protected void setupActionReadingMap() {
    actionReadingFns.put("M", () -> readMove());
    //actionReadingFns.put("A", () -> readAttack());
    actionReadingFns.put("D", () -> {
      return null;
    });
  }
}
