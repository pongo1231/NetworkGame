package com.pongo1231.networkgame.Game.Entities;

public class LocalPlayer extends PlayerEntity {
    private int id = -1;

    public LocalPlayer(int x, int y) {
        super(x, y);
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
