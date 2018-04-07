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

    private Thread listener;
    private Thread pinging;
    private Thread position;

    public Networking(Socket socket, Game game) throws IOException {
        socket.setKeepAlive(true);
        this.socket = socket;

        dataOut = new DataOutputStream(socket.getOutputStream());
        this.game = game;

        listener = new Thread(new NetworkingListener(this));
        listener.start();

        pinging = new Thread(new NetworkingPinging(this));
        pinging.start();

        position = new Thread(new NetworkingPosition(this));
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

    public void destroy() throws IOException {
        sendData(Type.CLIENT_DISCONNECT);
        listener.interrupt();
        pinging.interrupt();
        position.interrupt();
        socket.close();
    }

    public void disconnect() throws IOException {
        destroy();
        game.exit();
    }

    public enum Type {
        SERVER_CLIENT_JOINED(0),
        SERVER_CLIENT_UPDATE_POS(1),
        SERVER_REMOVE_CLIENT(2),
        CLIENT_UPDATE_POS(100),
        CLIENT_PING(101),
        CLIENT_DISCONNECT(102);

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