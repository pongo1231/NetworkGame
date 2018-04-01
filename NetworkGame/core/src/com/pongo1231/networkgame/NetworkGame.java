package com.pongo1231.networkgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.pongo1231.networkgame.Game.Net.Networking;

import java.io.IOException;
import java.net.Socket;

public class NetworkGame extends Game {

	@Override
	public void create() {
		try {
			com.pongo1231.networkgame.Game.Game game = new com.pongo1231.networkgame.Game.Game();
			Networking networking = new Networking("192.168.178.134", 41000, game);
			if (!networking.getSocket().isConnected()) {
				Gdx.app.setLogLevel(Application.LOG_ERROR);
				Gdx.app.log("NetworkGame", "Could not connect.");
			} else
				this.setScreen(game);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		super.render();
	}
}
