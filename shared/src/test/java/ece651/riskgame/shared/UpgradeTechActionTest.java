package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class UpgradeTechActionTest {
  @Test
  public void test_all() {
    UpgradeTechAction uta = new UpgradeTechAction("green");
    assertEquals("green", uta.getColor());
    Actable act = mock(Actable.class);
    Clan clan = mock(Clan.class);
    Resource resource = mock(Resource.class);
    doReturn(resource).when(clan).getResource();
    doNothing().when(resource).costGold(anyInt());

    Map<String, Clan> clans = mock(Map.class);
    doReturn(clans).when(act).getClans();
    doReturn(clan).when(clans).get(any());
    
    uta.apply(act);
    uta.clientApply(act);
    
  }

}
