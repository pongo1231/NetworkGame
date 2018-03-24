package com.pongo1231.networkgame.Game.Entities;

public class LocalPlayer extends PlayerEntity {
    private static LocalPlayer instance;

    private int id = -1;

    private LocalPlayer() {
        super(500, 500);
    }

    public static LocalPlayer getInstance() {
        if (instance == null)
            instance = new LocalPlayer();

        return instance;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
