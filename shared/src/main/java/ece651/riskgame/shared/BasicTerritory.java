package ece651.riskgame.shared;

public class BasicTerritory extends Territory {
  public BasicTerritory(String name) {
    super(name);
  }

  public BasicTerritory(String name, int size, Resource resource) {
    super(name, size, resource);
  }
}
