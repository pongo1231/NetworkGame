package com.pongo1231.networkgame.Game.Net;

import com.pongo1231.networkgame.Game.Entities.PlayerEntity;
import com.pongo1231.networkgame.Game.Game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Networking {
    private Socket socket;
    private DataOutputStream dataOut;
    private Game game;
    private int id = -1;

    public Networking(String ip, int port, Game game) throws IOException {
        socket = new Socket(ip, port);
        socket.setKeepAlive(true);
        dataOut = new DataOutputStream(socket.getOutputStream());
        this.game = game;

        Thread listener = new Thread(new NetworkingListener(this));
        listener.start();

        Thread pinging = new Thread(new NetworkingPinging(this));
        pinging.start();

        Thread position = new Thread(new NetworkingPosition(this));
        position.start();
    }

    public void sendData(Type type, int... data) throws IOException {
        dataOut.writeByte(type.getValue());
        for (int d : data)
            dataOut.writeInt(d);
    }

    public void sendData(Type type, float... data) throws IOException {
        dataOut.writeByte(type.getValue());
        for (float d : data)
            dataOut.writeFloat(d);
    }

    public PlayerEntity getPlayerEntityByID(int id) {
        for (PlayerEntity playerEntity : game.getPlayerEntities())
            if (playerEntity.getID() == id)
                return playerEntity;

        return null;
    }

    public Socket getSocket() {
        return socket;
    }

    public Game getGame() {
        return game;
    }

    public enum Type {
        SERVER_CLIENT_JOINED(0),
        SERVER_CLIENT_UPDATE_POS(1),
        CLIENT_UPDATE_POS(100),
        CLIENT_PING(101);

        private final int value;
        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Type fromInt(int i) {
            for (Type b : Type.values())
                if (b.getValue() == i)
                    return b;

            return null;
        }
    }
}