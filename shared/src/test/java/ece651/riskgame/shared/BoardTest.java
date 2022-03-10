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

}
