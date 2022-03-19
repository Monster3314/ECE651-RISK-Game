package ece651.riskgame.shared;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void test_PutAndGetEntry() {
        Board map = new Board();
        Territory t1 = new BasicTerritory("A");
        LinkedList<Territory> neig = new LinkedList<>();
        neig.add(new BasicTerritory("B"));
        neig.add(new BasicTerritory("C"));
        map.putEntry(t1, neig);
        assertEquals(neig, map.getNeighbors(t1));
    }

    @Test
    void test_AddandGetTerritory() {
        Board b = new Board();
        b.addTerritory(new BasicTerritory("A"));
        b.addTerritory(new BasicTerritory("B"));
        Set<Territory> expect = new HashSet<>();
        expect.add(new BasicTerritory("A"));
        expect.add(new BasicTerritory("B"));
        List<Territory> expect_list = new ArrayList<>(expect);
        assertEquals(expect, b.getTerritoriesSet());
        assertEquals(expect_list, b.getTerritoriesList());
    }

    @Test
    void test_equals() {
      Board b1 = new Board();
      Board b2 = new Board();
      Board b3 = new Board();
      
      assertTrue(b1.equals(b1));
      assertNotEquals(b1, null);
      assertEquals(b1, b2);
      
      b1.addTerritory(new BasicTerritory("A"));
      b3.addTerritory(new BasicTerritory("shit"));
      assertNotEquals(b1, b3);
      b2.addTerritory(new BasicTerritory("A"));
      b2.addTerritory(new BasicTerritory("B"));
      assertNotEquals(b1,b2);
      b1.addTerritory(new BasicTerritory("B"));
      assertEquals(b1, b2);

      Territory t1 = new BasicTerritory("A");
      LinkedList<Territory> neig = new LinkedList<>();
      neig.add(new BasicTerritory("B"));
      neig.add(new BasicTerritory("C"));
      b1.putEntry(t1, neig);
      assertNotEquals(b1, b2);
      LinkedList<Territory> neig2 = new LinkedList<>();
      neig2.add(new BasicTerritory("B"));
      b2.putEntry(t1, neig2);
      assertNotEquals(b1, b2);
      neig2.add(new BasicTerritory("C"));
      assertEquals(b1, b2);
      neig2.remove(1);
      neig2.add(new BasicTerritory("D"));
      assertNotEquals(b1, b2);
      b2.putEntry(new BasicTerritory("holy"), neig2);
      assertNotEquals(b1, b2);      
    }

  @Test
  public void test_getTerritory() {
    Board b = new Board();
    b.addTerritory(new BasicTerritory("A"));
    b.addTerritory(new BasicTerritory("B"));    
    assertEquals(new BasicTerritory("A"), b.getTerritory("A"));
    assertThrows(IllegalArgumentException.class, ()->{b.getTerritory("C");});
  }
}
