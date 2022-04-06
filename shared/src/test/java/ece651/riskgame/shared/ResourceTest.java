package ece651.riskgame.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    @Test
    void addResource() {
        Resource r1 = new Resource(new int[]{1, 2});
        HashMap<String, Integer> map = new HashMap<>();
        map.put(Resource.FOOD, 5);
        map.put(Resource.GOLD, 6);
        Resource r2 = new Resource(map);
        assertEquals(r1.getResourceNum(Resource.FOOD), 1);
        r1.addResource(r2);
        assertEquals(r1.getResourceNum(Resource.GOLD), 8);
        assertEquals(r2.getResources().size(), 2);
    }
}