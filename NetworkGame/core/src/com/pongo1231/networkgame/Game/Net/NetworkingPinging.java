package com.pongo1231.networkgame.Game.Net;

import com.badlogic.gdx.Gdx;

import java.io.IOException;

public class NetworkingPinging implements Runnable {
    private Networking networking;
    private int timeoutTime = 5;

    public NetworkingPinging(Networking networking) throws IOException {
        this.networking = networking;
    }

    @Override
    public void run() {
        while (networking.getSocket().isConnected()) {
            try {
                Thread.sleep(1000);
                networking.sendData(Networking.Type.CLIENT_PING);
                timeoutTime = 5;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }

            timeoutTime--;
            if (timeoutTime == 0)
                break;
        }

        try {
            networking.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
