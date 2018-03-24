package com.pongo1231.networkgame.Game.Net;

import com.badlogic.gdx.math.Vector2;

import java.io.IOException;

public class NetworkingPosition implements Runnable {
    private Networking networking;

    public NetworkingPosition(Networking networking) throws IOException {
        this.networking = networking;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(16);
                Vector2 playerPosition = networking.getGame().getPlayer().getPosition();
                networking.sendData(Networking.Type.CLIENT_UPDATE_POS, playerPosition.x, playerPosition.y);
            } catch (IOException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
