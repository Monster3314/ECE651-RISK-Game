package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a player.
 * Keep a list of territory occupied by this Clan
 */
public class Clan implements Serializable {
  private List<Territory> occupies;
  private int techLevel;
  
  private Resource resource;
  private List<Spy> spies;
  private boolean cloakAbility = false;

  static public int[] COST = new int[] {0, 50, 75, 125, 200, 300};
  static public int MAX_TECH_LEVEL = 6;
  static public int INITIAL_FOOD = 40;
  static public int INITIAL_GOLD = 100;
  
  public Clan() {
    this(new ArrayList<>());
  }
  
  public Clan(List<Territory> occupies) {
    this.occupies = occupies;
    this.techLevel = 1;
    this.resource = new Resource(new int[] { INITIAL_FOOD, INITIAL_GOLD });
    this.spies = new ArrayList<>();
  }
  
  public Clan(int techLevel, Resource resource) {
    this.occupies = new ArrayList<>();
    this.techLevel = techLevel;
    this.resource = resource;
    this.spies = new ArrayList<>();
  }
  public Clan(List<Territory> occupies, int techLevel, Resource resource) {
    this.occupies = occupies;
    this.techLevel = techLevel;
    this.resource = resource;
    this.spies = new ArrayList<>();
  }
  
  public int getTechLevel() {
    return techLevel;
  }
  
  public Resource getResource() {
    return this.resource;
  }
  
  public void addTerritory(Territory t) {
    occupies.add(t);
  }
  
  public boolean isActive() {
    return occupies.size() != 0;
  }
  
  public List<Territory> getOccupies() {
    return occupies;
  }
  
  /**
   * @param name of territory
   * @return true if succeed
   */
  public boolean occupyTerritory(String name) {
    for (Territory territory : occupies) {
      if (territory.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }
  
  public void removeTerritory(Territory t) {
    occupies.remove(t);
  }
  public Territory removeTerritory(String territoryName) {
    for (Territory occupy: occupies) {
      if (occupy.getName().equals(territoryName)) {
        occupies.remove(occupy);
        return occupy;
      }
    }
    return null;
  }
  
  public void upgradeLevel() throws IllegalStateException {
    if (techLevel < MAX_TECH_LEVEL) {
      resource.costGold(COST[techLevel]);
      techLevel ++;
    }
    else {
      throw new IllegalStateException("Max Technology Level can not exceed " + MAX_TECH_LEVEL);
    }
  }
  
  public void afterTurn() {
    getTerritoryProduction();
    refreshSpies();
    }
  
  public void getTerritoryProduction() {
    for (Territory t : occupies) {
      resource.addResource(t.getProduction());
    }
  }
  
  public void refreshSpies() {
    for (Spy spy : spies) {
      spy.setCanMove(true);
    }
  }
  
  public void addSpy(Spy spy) {
    spies.add(spy);
  }
  
  public Spy getSpy(String territory, boolean canMove) {
    for (Spy spy : spies) {
      if (spy.getTerritory().equals(territory) && (!canMove || spy.isCanMove())) {
        return spy;
      }
    }
    return null;
  }

  public List<Spy> getSpies() {
    return spies;
  }
  public int getSpyNumOnTerritory(String territoryName) {
    int count = 0;
    for (Spy s: spies) {
      if (s.getTerritory().equals(territoryName)) {
        count += 1;
      }
    }
    return count;
  }
  public void updateAsset(Clan latestClan) {
    techLevel = latestClan.getTechLevel();
    resource = latestClan.getResource();
    spies = latestClan.getSpies();
    
  }
  
  
  public void getCloakAbility() throws IllegalArgumentException{
    if(hasCloakAbility()) throw new IllegalArgumentException("Already has cloakability");
    if(techLevel < 3) throw new IllegalArgumentException("Techlevel is smaller than 3");
    cloakAbility = true;
    resource.costGold(100);
  }
  
  public boolean hasCloakAbility() {
    return cloakAbility;
  }
}
