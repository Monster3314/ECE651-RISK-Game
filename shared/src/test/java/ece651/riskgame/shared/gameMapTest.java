package ece651.riskgame.shared;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class gameMapTest {

    @Test
    void test_PutAndGetEntry() {
        gameMap map = new gameMap();
        Territory t1 = new BasicTerritory("A");
        LinkedList<Territory> neig = new LinkedList<>();
        neig.add(new BasicTerritory("B"));
        neig.add(new BasicTerritory("C"));
        map.putEntry(t1, neig);
        assertEquals(neig, map.getNeighbors(t1));
    }

}