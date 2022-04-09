package ece651.riskgame.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClanTest {

    @Test
    void addTerritory() {
        Clan c = new Clan();
        c.addTerritory(new BasicTerritory("A"));
        assertTrue(c.isActive());
        assertEquals(new ArrayList<Territory>(List.of(new BasicTerritory("A"))), c.getOccupies());
    }

    @Test
    void isActive() {
        Clan c = new Clan();
        assertFalse(c.isActive());
        c.addTerritory(new BasicTerritory("A"));
        assertTrue(c.isActive());
    }

    @Test
    void test_Constructor() {
        List<Territory> l = new ArrayList<>(List.of(new BasicTerritory("A")));
        Clan c = new Clan(l);
        assertEquals(l, c.getOccupies());
    }

  @Test
  void test_occupyTerritory() {
    List<Territory> l = new ArrayList<>();
    l.add(new BasicTerritory("A"));
    Clan c = new Clan(l);
    assertTrue(c.occupyTerritory("A"));
    assertFalse(c.occupyTerritory("B"));
  }

  @Test
    void testGetProduction() {
        Clan clan = new Clan();
        Territory t1 = new BasicTerritory("A", 0, new Resource(new int[]{1, 2}));
        Territory t2 = new BasicTerritory("B", 0, new Resource(new int[]{3, 4}));
        clan.addTerritory(t1);
        clan.addTerritory(t2);
        assertEquals(new Resource(new int[]{40, 100}), clan.getResource());
        clan.getTerritoryProduction();
        assertEquals(new Resource(new int[]{44, 106}), clan.getResource());
  }

}
