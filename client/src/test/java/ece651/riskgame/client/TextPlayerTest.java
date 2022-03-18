package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
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

public class TextPlayerTest {
  @Test
  public void test_readMove() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input;
    //valid testcases
    input = "Durham\nCary\n";
  }
  @Test
  public void test_readTerritory() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "Durham\nRaleigh\nCary\n";
    //valid testcases
    TextPlayer player = createTextPlayer(input, bytes);
    String prompt = "Please enter a territory:";
    Territory[] expectedList = new Territory[3];
    expectedList[0] = new BasicTerritory("Durham");
    expectedList[1] = new BasicTerritory("Raleigh");
    expectedList[2] = new BasicTerritory("Cary");

    for (int i = 0; i < expectedList.length; i++) {
      Territory t = player.readTerritory(prompt);
      assertEquals(t, expectedList[i]);
      assertEquals(prompt + "\n", bytes.toString());
      bytes.reset();
    }
    //invalid testcases
    input = "Greensboro\nApex\nDurham\n";
    player = createTextPlayer(input, bytes);
    Territory expected = new BasicTerritory("Durham");
    assertEquals(player.readTerritory(prompt), expected);
    String expectedMsg[] = new String[2];
    expectedMsg[0] = "Greensboro is not one of the existing territories";
    expectedMsg[1] =  "Apex is not one of the existing territories";
    String expectedOutput = new String();
    for (int i = 0; i < expectedMsg.length; i++) {
      expectedOutput = expectedOutput + prompt + "\n";
      expectedOutput = expectedOutput + expectedMsg[i] + "\n";
    }
    expectedOutput = expectedOutput + prompt + "\n";
    assertEquals(expectedOutput, bytes.toString());
    bytes.reset();
    //TODO: Test if EOFException is thrown when get full from inputRead
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
  private TextPlayer createTextPlayer(String inputData, OutputStream bytes) {
    GameInfo g = getDefaultGame();
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer("Green", g, input, output);
  }
    
    
}
