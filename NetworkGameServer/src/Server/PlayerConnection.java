package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Server.Server.Type;

public class PlayerConnection implements Runnable {
	private Client client;
	private Socket socket;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	
	public PlayerConnection(Client client, Socket socket) throws IOException {
		this.client = client;
		this.socket = socket;
		dataInput = new DataInputStream(socket.getInputStream());
		dataOutput = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		while (!client.isTimedout()) {
			try {
				readInput();
			} catch (IOException e) {}
		}
	}
	
	private void readInput() throws IOException {
		Type type = Type.fromInt(dataInput.readByte());
		if (type == null)
			return;
		
		switch (type) {
			case CLIENT_UPDATE_POS:
				float x = dataInput.readFloat();
				float y = dataInput.readFloat();
				client.getServer().sendDataToAllClients(Server.Type.SERVER_CLIENT_UPDATE_POS, client.getID(), x, y);
				break;
			case CLIENT_PING:
				client.resetTimeoutTime();
				break;
			default:
				break;
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public DataInputStream getDataInput() {
		return dataInput;
	}
	
	public DataOutputStream getDataOutput() {
		return dataOutput;
	}
}
