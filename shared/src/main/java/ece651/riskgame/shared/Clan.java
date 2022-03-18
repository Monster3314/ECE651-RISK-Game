package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Clan implements Serializable {
    private final List<Territory> occupies;

    public Clan() {
        this.occupies = new ArrayList<>();
    }

    public Clan(List<Territory> occupies) {
        this.occupies = occupies;
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
