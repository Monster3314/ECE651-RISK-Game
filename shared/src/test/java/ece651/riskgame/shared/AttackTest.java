package ece651.riskgame.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackTest {

    @Test
    void test_apply() {
        Board b = new Board();
        Territory t1 = new BasicTerritory("HK");
        Territory t2 = new BasicTerritory("Taiwan");
        t1.addUnit(new BasicUnit(15));
        t2.addUnit(new BasicUnit(5));
        b.addTerritory(t1);
        b.addTerritory(t2);
        Clan c1 = new Clan();
        c1.addTerritory(t1);
        Clan c2 = new Clan();
        c2.addTerritory(t2);
        Map<String, Clan> clans = new HashMap<>();
        clans.put("Red", c1);
        clans.put("Blue", c2);

        Actable fakeWorld = mock(Actable.class);
        when(fakeWorld.getBoard()).thenReturn(b);
        when(fakeWorld.getClans()).thenReturn(clans);

        assertEquals(15, fakeWorld.getBoard().getTerritory("HK").getUnits().get(0).getNum());
        assertEquals(5, fakeWorld.getBoard().getTerritory("Taiwan").getUnits().get(0).getNum());

        Unit units = new BasicUnit(10);
        Attack attack = new Attack(units, "HK", "Taiwan", "Red");
        attack.onTheWay(fakeWorld);
        attack.apply(fakeWorld);

        assertEquals(5, fakeWorld.getBoard().getTerritory("HK").getUnits().get(0).getNum());
        System.out.println(t2.getUnits().get(0).getNum());
        System.out.println(c1.occupyTerritory("Taiwan"));
    }

}