package Server;

import java.io.IOException;
import java.net.Socket;

public class Client {
	private Server server;
	private int id;
	private int timeoutTime = 10;

	private PlayerConnection connection;
	
	public Client(Server server, Socket socket, int id) throws IOException {
		this.server = server;
		this.id = id;
		
		connection = new PlayerConnection(this, socket);
		Thread connectionThread = new Thread(connection);
		connectionThread.start();
	}
	
	public int getID() {
		return id;
	}
	
	public void resetTimeoutTime() {
		timeoutTime = 10;
	}
	
	public void updateTimeoutTime() {
		timeoutTime--;
	}
	
	public int getTimeoutTime() {
		return timeoutTime;
	}
	
	public boolean isTimedout() {
		return timeoutTime <= 0;
	}
	
	public PlayerConnection getConnection() {
		return connection;
	}
	
	public Server getServer() {
		return server;
	}
}
