package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class TextPlayerTest {
  @Test
  public void test_getOccupies() {
   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
   String input = "";
   TextPlayer player = createTextPlayer(input, bytes);
   player.getOccupies();
  }
  @Test
  public void test_doOneSpeculation() {
   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
   String input = "";
   TextPlayer player = createTextPlayer(input, bytes);
   player.doOneSpectation();
  }
  @Test
  public void test_update() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "";
    GameInfo oldGame = getDefaultGame();
    TextPlayer player = createTextPlayer(input, bytes);
    GameInfo newGame = getEmptyGame();
    player.update(newGame);    
  }
  @Test
  public void test_isLost() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "";
    TextPlayer player = createWinner(input, bytes);
    assertFalse(player.isLost());
    //valid testcases
    player = createLoser(input, bytes);
    assertTrue(player.isLost());
    //valid testcases
    player = createTextPlayer(input, bytes);
    assertFalse(player.isLost());
  }
  @Test
  public void test_doGameOverPhase() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "";
    TextPlayer player = createWinner(input, bytes);
    player.doGameOverPhase();
    //valid testcases
    player = createLoser(input, bytes);
    player.doGameOverPhase();
    //invalid testcases
    final TextPlayer p = createTextPlayer(input, bytes);
    assertThrows(IllegalStateException.class, () -> {
      p.doGameOverPhase();
    });
  }
  @Test
  public void test_getPostDeathChoice() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "S\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.getPostDeathChoice();
    //valid testcases
    input = "Q\n";
    player = createTextPlayer(input, bytes);
    player.getPostDeathChoice();
    //invalid testcases
    input = "E\nS\n";
    player = createTextPlayer(input, bytes);
    player.getPostDeathChoice();
  }
  @Test
  public void test_readActionsPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "M\nDurham\n2\nCary\nD\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.readActionsPhase();
    //valid testcases
    input = "A\nDurham\nRaleigh\n2\nM\nDurham\n2\nCary\nD\n";
    player = createTextPlayer(input, bytes);
    player.readActionsPhase();
    //invalid testcases
    input = "A\nDurham\nCary\n2\nM\nDurham\n2\nCary\nD\n";
    player = createTextPlayer(input, bytes);
    player.readActionsPhase();
  }
  @Test
  public void test_readOneAction() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "M\nDurham\n2\nCary\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.readOneAction();
    //valid testcases
    input = "A\nDurham\nRaleigh\n2\n";
    player = createTextPlayer(input, bytes);
    player.readOneAction();
    //valid testcases
    input = "D\n";
    player = createTextPlayer(input, bytes);
    player.readOneAction();

    //invalid testcases
    input = "P\nM\nDurham\n2\nCary\n";
    player = createTextPlayer(input, bytes);
    player.readOneAction();
  }
  @Test
  public void test_readPlacementPhase() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "Durham\n4\nCary\n1\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.readPlacementPhase(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));
    //valid testcases
    input = "Durham\n2\nCary\n1\nDurham\n2\n";
    player = createTextPlayer(input, bytes);
    player.readPlacementPhase(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));

    //valid testcases
    input = "Durham\n5\n";
    player = createTextPlayer(input, bytes);
    player.readPlacementPhase(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));

    //invalid testcases
    input = "Durham\n6\n5\n";
    player = createTextPlayer(input, bytes);
    player.readPlacementPhase(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));

    //invalid testcases
    input = "Raleigh\n5\nDurham\n5";
    player = createTextPlayer(input, bytes);
    player.readPlacementPhase(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));
  }
  @Test
  public void test_readAttack() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "Durham\nCary\n4\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.readAttack();

    //invalid testcases
    input = "Durham\nCary\n5\n4\n";
    player = createTextPlayer(input, bytes);
    player.readAttack();

  }
  @Test
  public void test_readMove() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "Durham\n0\nCary\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.readMove();

    //invalid testcases
    input = "Durham\n5\n4\nCary\n";
    player = createTextPlayer(input, bytes);
    player.readMove();

    //invalid testcases
    input = "Durham\nsdhoih\n4\nCary\n";
    player = createTextPlayer(input, bytes);
    player.readMove();

    //invalid testcases
    input = "Durham\n-1\n4\nCary\n";
    player = createTextPlayer(input, bytes);
    player.readMove();

    //invalid testcases
    //input = "";
    //player = createTextPlayer(input, bytes);
    //player.readMove();

    /**
    //invalid testcases
    input = "Durham\n1\nRaleigh\n";
    player = createTextPlayer(input, bytes);
    player.readMove();


    //invalid testcases
    input = "Raleigh\n1\nCary\n";
    player = createTextPlayer(input, bytes);
    player.readMove();

    //invalid testcases
    input = "Durham\n1\nDurham\n";
    player = createTextPlayer(input, bytes);
    player.readMove();
    */
    




  }

   

  @Test
  public void test_readTerritory() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "Durham\nRaleigh\nCary\n";
    //valid testcases
    TextPlayer player = createTextPlayer(input, bytes);
    String prompt = "Please enter a territory:";
    Territory[] expectedList = new Territory[3];

    for (int i = 0; i < expectedList.length; i++) {
      Territory t = player.readTerritory(prompt);
      bytes.reset();
    }
    //invalid testcases
    input = "Greensboro\nApex\nDurham\n";
    player = createTextPlayer(input, bytes);
    Territory expected = new BasicTerritory("Durham");
    assertEquals(player.readTerritory(prompt), expected);
    
    bytes.reset();

    input = "";
    final TextPlayer p = createTextPlayer(input, bytes);
    assertThrows(IOException.class, () -> p.readTerritory(""));
  }
  
  @Test
  public void test_readUnit() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    input = "";
    final TextPlayer p = createTextPlayer(input, bytes);
    //assertThrows(IOException.class, () -> p.readUnit(""));
  }

  private GameInfo getDefaultGame() {
    Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
    GameInfo g = new GameInfo(b, players);
    GameTextView view = new GameTextView(g);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1, t3)));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t2, t4)));
    players.put("Green", c1);
    players.put("Blue", c2);
    t1.addUnit(new BasicUnit(4));
    t2.addUnit(new BasicUnit(3));
    t3.addUnit(new BasicUnit(1));
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return g;
  }
  private GameInfo getEmptyGame() {
    Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
    GameInfo g = new GameInfo(b, players);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList()));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t1,t2,t3,t4)));
    players.put("Green", c1);
    players.put("Blue", c2);
    t1.addUnit(new BasicUnit(4));
    t2.addUnit(new BasicUnit(3));
    t3.addUnit(new BasicUnit(1));
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return g;
  }
  private TextPlayer createTextPlayer(String inputData, OutputStream bytes) {
    GameInfo g = getDefaultGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Green", g, input, output);
  }
  private TextPlayer createWinner(String inputData, OutputStream bytes) {
    GameInfo g = getEmptyGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Blue", g, input, output);
  }
  private TextPlayer createLoser(String inputData, OutputStream bytes) {
    GameInfo g = getEmptyGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Green", g, input, output);
  }
    
    
}
