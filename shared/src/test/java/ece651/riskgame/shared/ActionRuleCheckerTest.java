package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ActionRuleCheckerTest {

  public class DemoActionRuleChecker extends ActionRuleChecker {
    public DemoActionRuleChecker(ActionRuleChecker arc) {
      super(arc);
    }

    @Override
    protected String checkMyRule(Actable act, Action action) {
      return null;
    }
  }
  
  @Test
  public void test_checkAction() {    
    ActionRuleChecker arc1 = Mockito.spy(new DemoActionRuleChecker(null));
    ActionRuleChecker arc2 = Mockito.spy(new DemoActionRuleChecker(arc1));

    assertEquals(null, arc2.checkAction(null, null));
    
    when(arc1.checkMyRule(null, null)).thenReturn("Wrong");
    assertEquals("Wrong", arc2.checkAction(null, null));
    
  } // test the chain feature 

}
