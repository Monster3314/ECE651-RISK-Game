package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserInitTest {
  @Test
  public void test_all() {
    UserInit ui1 = new UserInit();
    UserInit ui2 = new UserInit(true, "one", "two");
    ui1.setIs_login(false);
    ui1.setPassword("good");
    ui1.setUsername("bad");
    ui1.getPassword();
    ui1.getUsername();
    ui1.isIs_login();
  }

}
