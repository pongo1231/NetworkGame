package com.pongo1231.networkgame.Game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pongo1231.networkgame.Game.Entities.Entity;
import com.pongo1231.networkgame.NetworkGame;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private SpriteBatch batch;
	private TiledMap map;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;

	private List<Entity> entities;
	private Entity cameraTarget;

	public Map() {
		map = new TmxMapLoader().load("map.tmx");

		camera = new OrthographicCamera(200, 120);
		camera.setToOrtho(false, 200, 120);
		camera.update();

		renderer = new OrthogonalTiledMapRenderer(map, 1/32f);
		batch = new SpriteBatch();
		entities = new ArrayList<Entity>();
	}

	public void render() {
		cameraChaseTarget();
		drawMap();
		drawEntities();
	}

	private void cameraChaseTarget() {
		if (cameraTarget != null) {
			Vector2 position = cameraTarget.getPosition();
			camera.position.x = position.x;
			camera.position.y = position.y;
			camera.update();
		}
	}

	private void drawMap() {
		renderer.setView(camera);
		renderer.render();
	}

	private void drawEntities() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Entity entity : new ArrayList<Entity>(entities))
			entity.draw(batch);
		batch.end();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public boolean entityExists(Entity entity) {
		return entities.contains(entity);
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public void setCameraTarget(Entity entity) {
		if (entities.contains(entity))
			cameraTarget = entity;
	}

	public void freeCameraTarget() {
		cameraTarget = null;
	}

	public int[][] getMapBoundries() {
		return new int[][] {{100, 900}, {100, 900}};
	}
}
