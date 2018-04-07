package Server;

import java.io.IOException;
import java.net.Socket;

public class Client {
	private Server server;
	private int id;

	private PlayerConnection connection;
	
	public Client(Server server, Socket socket, int id) throws IOException {
		this.server = server;
		this.id = id;
		
		connection = new PlayerConnection(this, server, socket);
		Thread connectionThread = new Thread(connection);
		connectionThread.start();
	}
	
	public int getID() {
		return id;
	}
	
	public PlayerConnection getConnection() {
		return connection;
	}
	
	public Server getServer() {
		return server;
	}
}
