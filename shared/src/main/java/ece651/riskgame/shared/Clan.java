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
    private int maxTechLevel;
    private Resource resource;

    public Clan() {
        this.occupies = new ArrayList<>();
        this.maxTechLevel = 0;
        this.resource = new Resource(new int[] {0, 0});
    }

    public Clan(List<Territory> occupies) {
        this.occupies = occupies;
    }

    public Clan(int maxTechLevel, Resource resource) {
        this.occupies = new ArrayList<>();
        this.maxTechLevel = maxTechLevel;
        this.resource = resource;
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

//    boolean tryUpgradeLevel() {
//
//    }
}
