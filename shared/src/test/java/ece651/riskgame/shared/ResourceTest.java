package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ResourceTest {

  HashMap<String, Integer> resources;
  Resource res;
  
  public void prepare() {
    resources = mock(HashMap.class);
    doReturn(Integer.parseInt("10")).when(resources).get(any());
    res = new Resource();
    res = new Resource(resources);
  }

  @Test
  public void test_costFood() {
    prepare();
    res.costFood(1);
    assertThrows(IllegalArgumentException.class, () -> res.costFood(100));    
    res.costGold(1);
    assertThrows(IllegalArgumentException.class, () -> res.costGold(100));
  }

  @Test
  public void test_equals() {    
    Resource r1 = new Resource(new int[] {1,2});
    Resource r2 = new Resource(new int[] {2,2});
    assertFalse(r1.equals(null));
    assertFalse(r1.equals(r2));
  }
  
  
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
