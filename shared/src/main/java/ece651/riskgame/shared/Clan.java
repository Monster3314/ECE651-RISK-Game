package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Clan implements Serializable {
    private boolean active;
    private final List<Territory> occupies;

    public Clan() {
        this.occupies = new ArrayList<>();
        this.active = true;
    }

    public Clan(List<Territory> occupies) {
        this.occupies = occupies;
        this.active = true;
    }

    public void addTerritory(Territory t) {
        occupies.add(t);
    }

    public boolean isActive() {
        return active;
    }

    public List<Territory> getOccupies() {
        return occupies;
    }

    public void hasDied() {
        active = false;
    }

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

}
