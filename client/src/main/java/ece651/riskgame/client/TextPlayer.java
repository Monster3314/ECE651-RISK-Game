package ece651.riskgame.client;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.PlaceAction;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
/**
 * Textplayer is a class on client side which is responsible for reading initial placements, reading actions, applying actions locally, and spectating.
 */
public class TextPlayer extends Player{
  private GameView view;
  final BufferedReader inputReader;
  final PrintStream out;

  final ArrayList<String> actionChoices;
  final HashMap<String, Supplier<Action>> actionReadingFns;


  /**
   * Construct the TextPlayer using Standard I/O
   * @param server is the socket connection with game server
   */
  public TextPlayer(Socket server) throws IOException{
    this(server, new BufferedReader(new InputStreamReader(System.in)), System.out);


  }
  
  /**
   * Construct the TextPlayer using specified I/O
   * @param server is the socket connection with server
   * @param input is the reader which is used to fetch instructions from player
   * @param out is to print view and prompt to  
   */
  public TextPlayer(Socket server, BufferedReader input, PrintStream out) throws IOException{
    super(server);
    this.view = null;
    this.inputReader = input;
    this.out = out;

    this.actionChoices = new ArrayList<String>();
    this.actionReadingFns = new HashMap<String, Supplier<Action>>();
    setupActionList();
    setupActionReadingMap();
  }

  @Override
  public void initializeGame() throws ClassNotFoundException, IOException {
    this.color = (String) input.readObject();
    this.theGame = (GameInfo) input.readObject();
    this.view = new GameTextView(this.theGame);
  }
  /**
   * recieving the latest game from server, then update current Game and view 
   * @throws IOException when latest game is not recieved 
   * @throws ClassNotFoundException when casting failed during reading object  
   */
  @Override
  public void updateGame() throws IOException, ClassNotFoundException {
    theGame = recvLatestGame();
    view = new GameTextView(theGame);
  }

  /**
   * choose the action type and read it from input, then apply locally
   * do the above operations repetitively until "D" is entered, then send all actions to server  
   * new action candidate should be added to actionCheckers, actionChoices, actionReadingFns  
   * @return a list of valid actions
   */
  public List<Action> readActions() throws IOException {
    List<Action> actions = new ArrayList<>();
    while (true) {
      Action currentAction = readOneAction();
      if (currentAction != null) {
        String msg = tryApplyAction(currentAction);
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
    while(true) {
      printPromptMsg(getActionPrompt());
      String inputAction;
      inputAction = inputReader.readLine();
      try {
        return actionReadingFns.get(inputAction).get();
      } catch (NullPointerException e) {
        printErrorMsg(getActionError());
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
        List<Unit> unitsToMove = readUnits(src, "How many units do you want to move?");
        Territory dst = readTerritory("Which territory do you want to move unit to?");
        return new Move(unitsToMove, src.getName(), dst.getName(), this.color);

      } catch (IOException e) {
        //out.println();
      }
    }
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
        List<Unit> unitsToAttack = readUnits(src, "How many units do you want to dispatch?");
        return new Attack(unitsToAttack, src.getName(), dst.getName(), color);
      } catch (IOException e) {
      }
    }
  }  

  
  public Unit readOneUnit(Unit toAllocate) throws IOException{
    printPromptMsg(getUnitPrompt(toAllocate));
    int readNumber;
    String inputString;
    while (true) {
      try {
        inputString = inputReader.readLine();
        if (inputString == null) {
          throw new EOFException("EOF");
        }
        readNumber = Integer.parseInt(inputString);
        if (readNumber == 0) {
          return null;
        }
        else if (readNumber <= toAllocate.getNum()) {
          //TODO:Use unit instead of basic unit
          return new BasicUnit(readNumber);
        }
        else {
          //TODO: Unit name
          printErrorMsg("You don't have enough units.");
        }
      } catch (NumberFormatException e) {
        printErrorMsg("You should type a valid non negative number.");
      } catch (IllegalArgumentException e) {
        printErrorMsg("You should not type a negative number.");
      }
    }
  }
  /**
   * read Units to move/attack from terminal
   * @param src is the source territory to move unit from
   * @throws IOException when nothing fetched from input (STD input or BufferedReader)
  */
  public List<Unit> readUnits(Territory src, String prompt) throws IOException{
    List<Unit> toAllocate = src.getUnits();
    List<Unit> unitsToMove = new ArrayList<>();
    for (Unit toRead : toAllocate) {
      Unit unitToMove = readOneUnit(toRead);
      if (unitToMove != null) {
        unitsToMove.add(unitToMove);
      }
    }
    return unitsToMove;
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
   * @return a placeAction which place a unit to target territory 
   */
  public PlaceAction readOnePlacement(Unit toAllocate) throws IOException {
    //TODO:unit name
    printInfo("You have " + Integer.toString(toAllocate.getNum()) + " to palce.");

    Territory dst = readTerritory("Which territory do you want to place?");
    Unit unitToPlace = readOneUnit(toAllocate);
    return new PlaceAction(unitToPlace, dst.getName());
  }
  
  /**
   * read mulitple placement in placement phase
   * @return a list of valid move
   * @throws IOException when nothing fetched from input
   * @throws ClassNotFoundException when casting failed during recieving object  
   */
  public List<PlaceAction> readPlacements(List<Unit> unitsToPlace) throws IOException, ClassNotFoundException {
    printInfo(getGameInfo());
    //TODO:Support multiple units placement
    Unit unitToPlace = unitsToPlace.get(0);
    List<PlaceAction> placements = new ArrayList<>();
    while (unitToPlace.getNum() > 0) {
      PlaceAction placement = readOnePlacement(unitToPlace);
      //TODO: check validity
      String errorMsg = tryApplyAction(placement, actionCheckers.get(placement.getClass()));
      if (errorMsg == null) {
        placement.apply(theGame);
        placements.add(placement);
        unitToPlace.decSoldiers(placement.getUnit().getNum());
      }
    }

  }
  /**
   * doSpectationPhase is used when player choose to speculate the game
   * @return IOException when the latest game is not recieved
   */
  public void doSpectationPhase() throws IOException{
    while(!isGameOver()) {
      doOneSpectation();
      printWaitingMsg();
    }
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
      input.close();
      output.close();
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

  protected String getDeathChoicesPrompt() {
    StringBuilder promptMsg = new StringBuilder();
    promptMsg.append("You are dead. What would you like to do?\n");
    promptMsg.append("(S)pectate\n");
    promptMsg.append("(Q)uit");
    return promptMsg.toString();
  }
  
  /**
   * fetch post peath choice from input 
   * @return Q or S based, which represents Quit or Spectate
   * @throws IOException when nothing fetched from input
   */
  //TODO:multiple post death choices
  public String getPostDeathChoice() throws IOException{
    while (true) {
      printPromptMsg(getDeathChoicesPrompt());
      String choice = inputReader.readLine();
      if (choice.equals("S") || choice.equals("Q")) {
        return choice;
      }
      else {
        printErrorMsg(getDeathChoicesError());
      }
    }
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
   * print error message with two lines of symbol around  
   */
  protected void printErrorMsg(String msg) {
    out.println("****************************************************");
    out.println(msg);
    out.println("****************************************************");
  }

  /**
   * print prompt message with two lines of symbol around 
   */
  protected void printPromptMsg(String prompt) {
    out.println("==========================");
    out.println(prompt);
    out.println("==========================");
  }
  /**
   * print game state message with two lines of symbol around  
   */
  protected void printInfo(String info) {
    out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    out.println(info);
    out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
  }
  //TODO:Time elapse
  protected void printWaitingMsg() {
    System.out.println("Waiting for other players...");
  }
  protected String getActionPrompt() {
    StringBuilder promptMsg = new StringBuilder();
    promptMsg.append("You are the " + color + " player, what would you like to do?");
    for (String actionType: actionChoices) {
      promptMsg.append("\n" + actionType);
    }
    return promptMsg.toString();
  }
  protected String getActionError() {
    StringBuilder errorMsg = new StringBuilder();
    errorMsg.append("Your action should be within");
    for (String actionType: actionChoices) {
      errorMsg.append(" " + actionType);
    }
    errorMsg.append(".");
    return errorMsg.toString();
  }
  protected String getDeathChoicesError() {
    return "Please choose from (S)pectate (Q)uit";
  }
  protected String getGameInfo() {
    StringBuilder infoMsg = new StringBuilder();
    infoMsg.append(view.displayGame());
    infoMsg.append("You are the " + color + " player.");
    return infoMsg.toString();
  }
  protected String getUnitsInfo(List<Unit> units) {
    StringBuilder infoMsg = new StringBuilder();
    infoMsg.append("You have the following units:\n");
    for (Unit u: units) {
      //TODO:Add unit name
      infoMsg.append(Integer.toString(u.getNum()) + "\n");
    }
    return infoMsg.toString();
  }
  protected String getUnitPrompt(Unit toMove) {
    StringBuilder promptMsg = new StringBuilder();
    promptMsg.append("You have ");
    promptMsg.append(Integer.toString(toMove.getNum()));
    //TODO:Add unit name
    promptMsg.append(" units.\n");
    promptMsg.append("How many units do you want to place?");
    return promptMsg.toString();
  }

}
