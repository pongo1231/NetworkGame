package com.pongo1231.networkgame;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidLauncher extends Activity {
	private static final int PORT = 41000;

	private EditText ipView;
	private EditText portView;
	private TextView versionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverchooser);

		ipView = findViewById(R.id.serverchooser_ip);
		portView = findViewById(R.id.serverchooser_port);

		versionView = findViewById(R.id.serverchooser_version);
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionView.setText("Version " + packageInfo.versionName + " (" +  packageInfo.versionCode + ")");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
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
