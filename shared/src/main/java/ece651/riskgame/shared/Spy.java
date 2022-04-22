package ece651.riskgame.shared;

import java.io.Serializable;

public class Spy implements Serializable {
    private String territory;
    private boolean canMove;

    public Spy(String territory) {
        this.territory = territory;
        this.canMove = true;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void moveTo(String territory) {
        this.territory = territory;
    }

    public String getTerritory() {
        return this.territory;
    }

    public boolean isCanMove() {
        return canMove;
    }
}
