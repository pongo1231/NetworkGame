package com.pongo1231.networkgame.Game.Net;

import com.badlogic.gdx.Gdx;
import com.pongo1231.networkgame.Game.*;
import com.pongo1231.networkgame.Game.Entities.PlayerEntity;

import java.io.DataInputStream;
import java.io.IOException;

public class NetworkingListener implements Runnable {
    private Networking networking;
    private Game game;
    private DataInputStream dataInput;
    private boolean fullyJoined = false;

    public NetworkingListener(Networking networking) throws IOException {
        this.networking = networking;
        game = networking.getGame();
        dataInput = new DataInputStream(networking.getSocket().getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Networking.Type data = Networking.Type.fromInt(dataInput.readByte());
                if (data == null)
                    continue;
                int id = dataInput.readInt();
                switch (data) {
                    case SERVER_CLIENT_JOINED:
                        if (!fullyJoined) {
                            networking.getGame().getPlayer().setID(id);
                            fullyJoined = true;
                        } else
                            game.addPlayerEntity(new PlayerEntity(500, 500, id));
                        break;
                    case SERVER_CLIENT_UPDATE_POS:
                        float x = dataInput.readFloat();
                        float y = dataInput.readFloat();
                        PlayerEntity playerEntity = networking.getPlayerEntityByID(id);
                        if (playerEntity != null)
                            game.updatePlayerEntityPosition(playerEntity, x, y);
                    default:
                        break;
                }
            } catch (IOException e) {}
        }
    }
}
