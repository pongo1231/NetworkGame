package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Server.Server.Type;

public class PlayerConnection implements Runnable {
	private Client client;
	private Server server;
	private Socket socket;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	private boolean hasHandshaked = false;
	private int timeoutTime = 5;
	
	public PlayerConnection(Client client, Server server, Socket socket) throws IOException {
		this.client = client;
		this.server = server;
		this.socket = socket;
		dataInput = new DataInputStream(socket.getInputStream());
		dataOutput = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		handshake();
		if (!hasHandshaked)
			try {
				destroyConnection("No handshake");
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			while (!isTimedout()) {
				try {
					readInput();
				} catch (IOException e) {
					return;
				}
			}
	}
	
	private void handshake() {
		try {
			Thread handshakeThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (!hasHandshaked)
							if (dataInput.readByte() == Type.CLIENT_INITIAL_HANDSHAKE.getValue()) {
								dataOutput.writeByte(Type.SERVER_INITIAL_HANDSHAKE.getValue());
								hasHandshaked = true;
							}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			handshakeThread.run();
			
			int waitTime = 5;
			while (!hasHandshaked) {
				Thread.sleep(1000);
				waitTime--;
				if (waitTime == 0) {
					handshakeThread.interrupt();
					hasHandshaked = false;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
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
				server.sendDataToAllClients(Server.Type.SERVER_CLIENT_UPDATE_POS, client.getID(), x, y);
				break;
			case CLIENT_PING:
				resetTimeoutTime();
				break;
			case CLIENT_DISCONNECT:
				destroyConnection("Disconected");
				break;
			default:
				break;
		}
	}
	
	private void destroyConnection(String reason) throws IOException {
		server.kickClient(reason, client);
	}
	
	public void resetTimeoutTime() {
		timeoutTime = 5;
	}
	
	public void updateTimeoutTime() {
		timeoutTime--;
	}
	
	public int getTimeoutTime() {
		return timeoutTime;
	}
	
	public boolean isTimedout() {
		return !socket.isConnected() || timeoutTime <= 0;
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
