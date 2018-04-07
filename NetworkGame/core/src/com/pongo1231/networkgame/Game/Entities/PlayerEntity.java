package com.pongo1231.networkgame.Game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class PlayerEntity extends Entity {
    private static Texture cachedPlayerTexture;
    private int id = -1;

    public PlayerEntity(int x, int y) {
        // Thanks java
        super(cachedPlayerTexture == null ? cachedPlayerTexture = new Texture(Gdx.files.internal("player.png")) : cachedPlayerTexture, x, y);
    }

    public PlayerEntity(int x, int y, int id) {
        super(cachedPlayerTexture == null ? cachedPlayerTexture = new Texture(Gdx.files.internal("player.png")) : cachedPlayerTexture, x, y);
        this.id = id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
