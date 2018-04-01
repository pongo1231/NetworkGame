package com.pongo1231.networkgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pongo1231.networkgame.NetworkGame;

public class AndroidLauncher extends AndroidApplication {
	private EditText ipView;
	private AndroidApplicationConfiguration config;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverchooser);

		ipView = findViewById(R.id.serverchooser_ip);
		config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useWakelock = true;
	}

	public void onConnectClick(Button button) {
		if (ipView.getText().equals(""))
			Toast.makeText(this, "Please enter an IP", Toast.LENGTH_SHORT).show();
		else
			initialize(new NetworkGame(), config);
	}

	private void tryConnect(String ip) {
		// TODO
	}
}
