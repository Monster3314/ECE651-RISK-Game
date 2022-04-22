package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a player.
 * Keep a list of territory occupied by this Clan
 */
public class Clan implements Serializable {
    private final List<Territory> occupies;
    private int techLevel;

    private boolean cloakAbility = false;
    private Resource resource;
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
    }

    public Clan(int techLevel, Resource resource) {
        this.occupies = new ArrayList<>();
        this.techLevel = techLevel;
        this.resource = resource;
    }
    public Clan(List<Territory> occupies, int techLevel, Resource resource) {
        this.occupies = occupies;
        this.techLevel = techLevel;
        this.resource = resource;
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

    public void upgradeLevel() throws IllegalStateException {
      if (techLevel < MAX_TECH_LEVEL) {
        resource.costGold(COST[techLevel]);
        techLevel ++;
      }
      else {
        throw new IllegalStateException("Max Technology Level can not exceed " + Integer.toString(MAX_TECH_LEVEL));
      }
    }

    public void getTerritoryProduction() {
        for (Territory t : occupies) {
            resource.addResource(t.getProduction());
        }
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
