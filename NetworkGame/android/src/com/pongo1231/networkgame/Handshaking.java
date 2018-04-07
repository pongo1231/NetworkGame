package com.pongo1231.networkgame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pongo1231.networkgame.Game.Net.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.InterruptedByTimeoutException;

public class Handshaking extends AsyncTask<Void, Void, Boolean> {
    private Activity activity;
    private String ip;
    private int port;
    private ProgressDialog loadingDialog;
    private static Socket socket;
    private boolean hasServerHandshaked = false;

    public Handshaking(Activity activity, String ip, int port) {
        this.activity = activity;
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = new ProgressDialog(activity);
        loadingDialog.setMessage("Connecting...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void[] ok) {
        try {
            socket = new Socket(ip, port);
            if (!socket.isConnected())
                return false;

            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.writeByte(Type.CLIENT_INITIAL_HANDSHAKE.getValue());

            Thread handshakeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!hasServerHandshaked) {
                            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                            if (dataInput.readByte() == Type.SERVER_INITIAL_HANDSHAKE.getValue())
                                hasServerHandshaked = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            handshakeThread.run();

            int waitTime = 5;
            while (!hasServerHandshaked) {
                Thread.sleep(1000);
                waitTime--;
                if (waitTime == 0) {
                    handshakeThread.interrupt();
                    return false;
                }
            }

            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        loadingDialog.dismiss();
        if (!result.booleanValue())
            Toast.makeText(activity, "Connection failed", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(activity, GameLauncher.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    /**
     * Needed to give the other activity access to it
     */
    public static Socket getSocket() {
        return socket;
    }

    public enum Type {
        CLIENT_INITIAL_HANDSHAKE(99),
        SERVER_INITIAL_HANDSHAKE(98);

        private final int value;
        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Networking.Type fromInt(int i) {
            for (Networking.Type b : Networking.Type.values())
                if (b.getValue() == i)
                    return b;

            return null;
        }
    }
}
