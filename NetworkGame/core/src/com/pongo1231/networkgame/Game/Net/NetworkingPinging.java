package com.pongo1231.networkgame.Game.Net;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

public class NetworkingPinging implements Runnable {
    private Networking networking;

    public NetworkingPinging(Networking networking) throws IOException {
        this.networking = networking;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                networking.sendData(Networking.Type.CLIENT_PING);
            } catch (IOException e) {
                e.printStackTrace();

                // TODO: Better server disconnect system
                System.exit(-1);

                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
