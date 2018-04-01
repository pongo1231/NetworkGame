package com.pongo1231.networkgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidLauncher extends Activity {
	private static final int PORT = 41000;

	private EditText ipView;
	private EditText portView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverchooser);

		ipView = findViewById(R.id.serverchooser_ip);
		portView = findViewById(R.id.serverchooser_port);
	}

	public void onConnectClick(View v) {
		String ipText = ipView.getText().toString();
		if (isEmpty(ipText))
			Toast.makeText(this, "Please enter an IP", Toast.LENGTH_SHORT).show();
		else {
			int port = PORT;
			String portText = portView.getText().toString();
			if (!isEmpty(portText))
				port = Integer.valueOf(portView.getText().toString());

			tryConnect(ipText, port);
		}
	}

	private void tryConnect(String ip, int port) {
		Handshaking handshaking = new Handshaking(this, ip, port);
		handshaking.execute();
	}

	private boolean isEmpty(String text) {
		return text.trim().length() == 0;
	}
}
