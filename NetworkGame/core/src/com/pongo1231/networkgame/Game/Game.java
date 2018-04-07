package com.pongo1231.networkgame.Game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.pongo1231.networkgame.Game.Entities.LocalPlayer;
import com.pongo1231.networkgame.Game.Entities.PlayerEntity;
import com.pongo1231.networkgame.Game.Net.Networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game implements Screen, InputProcessor {
    private Map map;
    private LocalPlayer player;
    private List<PlayerEntity> playerEntities;

    private boolean moving = false;
    private Vector2 moveDir;

    public Game() {
        player = new LocalPlayer(500, 500);
        map = new Map();
        map.addEntity(player);
        map.setCameraTarget(player);

        playerEntities = new ArrayList<PlayerEntity>();
        moveDir = new Vector2();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        map.render();

        if (moving) {
            Vector2 currentPosition = player.getPosition();
            player.setPosition(currentPosition.x + moveDir.x, currentPosition.y + moveDir.y);
            checkMapBoundries();
        }
    }

    private void checkMapBoundries() {
        int[][] boundries = map.getMapBoundries();
        Vector2 currentPosition = player.getPosition();

        if (currentPosition.x < boundries[0][0])
            player.setPosition(boundries[0][0], currentPosition.y);
        else if (currentPosition.x > boundries[0][1])
            player.setPosition(boundries[0][1], currentPosition.y);

        if (currentPosition.y < boundries[1][0])
            player.setPosition(currentPosition.y, boundries[1][0]);
        else if(currentPosition.y > boundries[1][1])
            player.setPosition(currentPosition.y, boundries[1][1]);
    }

    public synchronized void addPlayerEntity(PlayerEntity playerEntity) {
        map.addEntity(playerEntity);
        playerEntities.add(playerEntity);
    }

    public synchronized void removePlayerEntity(PlayerEntity playerEntity) {
        map.removeEntity(playerEntity);
        playerEntities.remove(playerEntity);
    }

    public synchronized LocalPlayer getPlayer() {
        return player;
    }

    public synchronized List<PlayerEntity> getPlayerEntities() {
        return playerEntities;
    }

    public synchronized void updatePlayerEntityPosition(PlayerEntity playerEntity, float x, float y) {
        playerEntity.setPosition(x, y);
    }

    public void exit() {
        // TODO: Better exiting
        System.exit(-1);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer > 0)
            return false;

        moving = true;
        Vector2 currentPosition = player.getPosition();
        if (screenX > currentPosition.x)
            moveDir.x = (currentPosition.x - screenX) * 0.01f;
        else
            moveDir.x = (screenX - currentPosition.x) * 0.01f;

        if (screenY > currentPosition.y)
            moveDir.y = (currentPosition.y - screenY) * 0.01f;
        else
            moveDir.y = (screenY - currentPosition.y) * 0.01f;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer > 0)
            return false;

        Vector2 currentPosition = player.getPosition();
        if (screenX > currentPosition.x)
            moveDir.x = (currentPosition.x - screenX) * 0.01f;
        else
            moveDir.x = (screenX - currentPosition.x) * 0.01f;

        if (screenY > currentPosition.y)
            moveDir.y = (currentPosition.y - screenY) * 0.01f;
        else
            moveDir.y = (screenY - currentPosition.y) * 0.01f;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer > 0)
            return false;

        moving = false;
        return true;
    }

    @Override
    public void resize(int p1, int p2) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
