package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class TextPlayerTest extends PlayerTest{
  ByteArrayOutputStream bytes;
  String input;
  public TextPlayerTest(){
    bytes = new ByteArrayOutputStream();
    input = "";
  }


  @Test
  public void test_doOneSpectation() {
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
    player.updateGame(newGame);    
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
    //valid spectate
    input = "S\n";
    TextPlayer spy = Mockito.spy(createTextPlayer(input, bytes));
    when(spy.isGameOver()).thenReturn(false);
    when(spy.isLost()).thenReturn(true);
    assertEquals("S", spy.getPostDeathChoice());
    //valid quit
    input = "Q\n";
    spy.setInputReader(new BufferedReader(new StringReader(input)));
    assertEquals("Q", spy.getPostDeathChoice());
    //invalid testcase
    input = "E\nS\n";
    spy.setInputReader(new BufferedReader(new StringReader(input)));
    assertEquals("S", spy.getPostDeathChoice());
    //eof testcase
    input = "";
    spy.setInputReader(new BufferedReader(new StringReader(input)));
    assertThrows(EOFException.class, () -> spy.getPostDeathChoice());
    //game over
    input = "S\n";
    final TextPlayer winner = createLoser(input, bytes);
    assertThrows(IllegalStateException.class, () -> winner.getPostDeathChoice());
    //user not dead
    input = "S\n";
    final TextPlayer p2 = createTextPlayer(input, bytes);
    assertThrows(IllegalStateException.class, () -> p2.getPostDeathChoice());    
  }


  @Disabled
  @Test
  public void test_readPlacementPhase() throws IOException, ClassNotFoundException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "Durham\n4\nCary\n1\n";
    TextPlayer player = createTextPlayer(input, bytes);
    player.readPlacements(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));
    //valid testcases
    input = "Durham\n2\nCary\n1\nDurham\n2\n";
    player = createTextPlayer(input, bytes);
    player.readPlacements(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));

    //valid testcases
    input = "Durham\n5\n";
    player = createTextPlayer(input, bytes);
    player.readPlacements(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));

    //invalid testcases
    input = "Durham\n6\n5\n";
    player = createTextPlayer(input, bytes);
    player.readPlacements(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));

    //invalid testcases
    input = "Raleigh\n5\nDurham\n5";
    player = createTextPlayer(input, bytes);
    player.readPlacements(new ArrayList<Unit>(Arrays.asList(new BasicUnit(5))));
  }

  @Test
  public void test_readActionsPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "M\nDurham\nCary\n2\nD\n";
    TextPlayer player = createTextPlayer(input, bytes);
    assertEquals(1, player.readActions().size());
    //valid testcases
    input = "A\nDurham\nRaleigh\n2\nM\nDurham\nCary\n2\nD\n";
    player = createTextPlayer(input, bytes);
    assertEquals(2, player.readActions().size());
    //invalid testcases
    input = "A\nDurham\nCary\n2\nM\nDurham\nCary\n2\nD\n";
    player = createTextPlayer(input, bytes);
    assertEquals(1, player.readActions().size());
  }
  @Test
  public void test_readOneAction() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "M\nDurham\nCary\n2\n";
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
    input = "P\nM\nDurham\nCary\n2\n";
    player = createTextPlayer(input, bytes);
    player.readOneAction();
  }
  @Test
  public void test_readAttack() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    Attack toAdd;
    //valid testcase
    input = "Durham\nCary\n0\n";
    TextPlayer player = createTextPlayer(input, bytes);
    toAdd = player.readAttackAction();
    assertEquals(0, toAdd.getUnit().size());
    assertEquals("Durham", toAdd.getFromTerritory());
    assertEquals("Cary", toAdd.getToTerritory());
    assertEquals("Red", toAdd.getColor());
    //invalid testcase
    input = "Durham\nCary\n5\n4\n";
    player = createTextPlayer(input, bytes);
    toAdd = player.readAttackAction();
    assertEquals(1, toAdd.getUnit().size());
    assertEquals("Durham", toAdd.getFromTerritory());
    assertEquals("Cary", toAdd.getToTerritory());
    assertEquals("Red", toAdd.getColor());

    //EOF testcase
    input = "Durham\nCary\n";
    player = createTextPlayer(input, bytes);
    assertNull(player.readAttackAction());

    

  }

  @Test
  public void test_readMoveAction() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;

    Move toAdd;
    //valid testcase
    input = "Durham\nCary\n0\n";
    TextPlayer player = createTextPlayer(input, bytes);
    toAdd = player.readMoveAction();
    assertEquals(0, toAdd.getUnit().size());
    assertEquals("Durham", toAdd.getFromTerritory());
    assertEquals("Cary", toAdd.getToTerritory());
    assertEquals("Red", toAdd.getColor());

    //invalid testcase
    input = "Durham\nCary\n5\n4\n";
    player = createTextPlayer(input, bytes);
    toAdd = player.readMoveAction();
    assertEquals(1, toAdd.getUnit().size());
    assertEquals("Durham", toAdd.getFromTerritory());
    assertEquals("Cary", toAdd.getToTerritory());
    assertEquals("Red", toAdd.getColor());

    //invalid testcase
    input = "Durham\nCary\nsdhoih\n4\n";
    player = createTextPlayer(input, bytes);
    toAdd = player.readMoveAction();
    assertEquals(1, toAdd.getUnit().size());
    assertEquals("Durham", toAdd.getFromTerritory());
    assertEquals("Cary", toAdd.getToTerritory());
    assertEquals("Red", toAdd.getColor());
    //invalid testcase
    input = "Durham\nCary\n-1\n4\n";
    player = createTextPlayer(input, bytes);
    toAdd = player.readMoveAction();
    assertEquals(1, toAdd.getUnit().size());
    assertEquals("Durham", toAdd.getFromTerritory());
    assertEquals("Cary", toAdd.getToTerritory());
    assertEquals("Red", toAdd.getColor());

    //EOF testcase
    input = "Durham\nCary\n";
    player = createTextPlayer(input, bytes);
    toAdd = player.readMoveAction();
    assertNull(toAdd);

    /*
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
    String input = "";
    final TextPlayer p = createTextPlayer(input, bytes);
    assertThrows(IOException.class, () -> p.readTerritory(""));
    
    input = "Durham\nRaleigh\nCary\n";
    //valid testcases
    TextPlayer player = createTextPlayer(input, bytes);

    Territory[] expectedTerritories = new Territory[3];
    expectedTerritories[0] = new BasicTerritory("Durham");
    expectedTerritories[1] = new BasicTerritory("Raleigh");
    expectedTerritories[2] = new BasicTerritory("Cary");

    for (int i = 0; i < expectedTerritories.length; i++) {
      Territory t = player.readTerritory("");
      assertEquals(t, expectedTerritories[i]);

      bytes.reset();
    }
    //invalid testcases
    input = "Greensboro\nApex\nDurham\n";
    player = createTextPlayer(input, bytes);
    Territory expected = new BasicTerritory("Durham");
    assertEquals(player.readTerritory(""), expected);
    bytes.reset();

  }
  @Test
  public void test_readOneUnit() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    input = "\n";
    final TextPlayer p = createTextPlayer(input, bytes);
    assertThrows(IOException.class, () -> p.readOneUnit(new BasicUnit(5)));

    input = "5\n";
    TextPlayer player = createTextPlayer(input, bytes);
    assertEquals(5, player.readOneUnit(new BasicUnit(5)).getNum());

    input = "-1\n6\ns511d\n4\n";
    player = createTextPlayer(input, bytes);
    assertEquals(4, player.readOneUnit(new BasicUnit(5)).getNum());

    input = "0\n";
    player = createTextPlayer(input, bytes);
    assertNull(player.readOneUnit(new BasicUnit(5)));
  }
  @Test
  public void test_readUnits() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    List<Unit> toRead = new ArrayList<Unit>(Arrays.asList(new BasicUnit(5, 0),
                                                               new BasicUnit(5, 1),
                                                               new BasicUnit(5, 2)));
    List<Unit> toSend = null;
    input = "";
    final TextPlayer p = createTextPlayer(input, bytes);
    assertThrows(IOException.class, () -> p.readUnits(toRead, ""));

    input = "5\n5\n5\n";
    TextPlayer player = createTextPlayer(input, bytes);
    toSend = player.readUnits(toRead, "");
    assertEquals(3, toSend.size());
    assertEquals(0, toSend.get(0).getLevel());
    assertEquals(1, toSend.get(1).getLevel());
    assertEquals(2, toSend.get(2).getLevel());
    
    
    input = "5\n0\n5\n";
    player = createTextPlayer(input, bytes);
    toSend = player.readUnits(toRead, "");
    assertEquals(2, toSend.size());
    assertEquals(0, toSend.get(0).getLevel());
    assertEquals(2, toSend.get(1).getLevel());

    input = "0\n0\n0\n";
    player = createTextPlayer(input, bytes);
    assertEquals(0, player.readUnits(toRead, "").size());
  }
  @Test
  public void test_getGameInfo() {
    TextPlayer player = createTextPlayer(input, bytes);
    player.getGameInfo();
  }



  protected TextPlayer createTextPlayer(String inputData, OutputStream bytes) {
    GameInfo g = getDefaultGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Red", g, input, output);
  }
  protected TextPlayer createWinner(String inputData, OutputStream bytes) {
    GameInfo g = getEmptyGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Blue", g, input, output);
  }
  protected TextPlayer createLoser(String inputData, OutputStream bytes) {
    GameInfo g = getEmptyGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Red", g, input, output);
  }
    
    
}
