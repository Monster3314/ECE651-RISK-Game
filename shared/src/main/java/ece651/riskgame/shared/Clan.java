package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Clan implements Serializable {
    private boolean active;
    private List<Territory> occupies;

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

}
