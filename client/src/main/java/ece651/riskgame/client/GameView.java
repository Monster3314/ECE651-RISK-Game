package ece651.riskgame.client;

import java.util.List;

import ece651.riskgame.shared.Unit;

public interface GameView {
  public String displayGame();

  public String displayUnits(List<Unit> units);

}
