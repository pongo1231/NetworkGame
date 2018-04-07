package com.pongo1231.networkgame.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    private Sprite sprite;

    public Entity(Texture texture, int x, int y) {
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
