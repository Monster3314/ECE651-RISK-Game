package ece651.riskgame.shared;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackTest {

    @Disabled
    @Test
    void test_apply() {
        Board b = new Board();
        Territory t1 = new BasicTerritory("HK");
        Territory t2 = new BasicTerritory("Taiwan");
        Territory t3 = new BasicTerritory("Anhui");
        Territory t4 = new BasicTerritory("Henan");
        t1.addUnit(new BasicUnit(15));
        t2.addUnit(new BasicUnit(1));
        t3.addUnit(new BasicUnit(5));
        t4.addUnit(new BasicUnit(20));
        b.addTerritory(t1);
        b.addTerritory(t2);
        b.addTerritory(t3);
        b.addTerritory(t4);
        Clan c1 = new Clan();
        c1.addTerritory(t1);
        c1.addTerritory(t3);
        c1.getResource().addResource(new Resource(new int[]{100, 100}));
        Clan c2 = new Clan();
        c2.addTerritory(t2);
        c2.addTerritory(t4);
        c2.getResource().addResource(new Resource(new int[]{100, 100}));
        Map<String, Clan> clans = new HashMap<>();
        clans.put("Red", c1);
        clans.put("Blue", c2);

        Actable fakeWorld = mock(Actable.class);
        when(fakeWorld.getBoard()).thenReturn(b);
        when(fakeWorld.getClans()).thenReturn(clans);

        assertEquals(15, fakeWorld.getBoard().getTerritory("HK").getUnits().get(0).getNum());
        assertEquals(1, fakeWorld.getBoard().getTerritory("Taiwan").getUnits().get(0).getNum());

        Unit units = new BasicUnit(10);
        Attack attack1 = new Attack(List.of(units), "HK", "Taiwan", "Red");
        Attack attack2 = new Attack(List.of(new BasicUnit()), "HK", "Anhui", "Red");
        Attack attack3 = new Attack(List.of(new BasicUnit(5)), "Anhui", "Henan", "Red");
        attack1.onTheWay(fakeWorld);
        attack1.apply(fakeWorld);
        attack2.onTheWay(fakeWorld);
        attack2.apply(fakeWorld);
        attack3.clientApply(fakeWorld);
        attack3.apply(fakeWorld);

        assertEquals(1, fakeWorld.getBoard().getTerritory("Anhui").getUnits().get(0).getNum());
        assertEquals(4, fakeWorld.getBoard().getTerritory("HK").getUnits().get(0).getNum());
        System.out.println(t2.getUnits().get(0).getNum());
        System.out.println(c1.occupyTerritory("Taiwan"));
        System.out.println(c2.occupyTerritory("Henan"));
    }

}