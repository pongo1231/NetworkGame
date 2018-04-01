package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private static final int PORT = 41000;
	
	private ServerSocket server;
	private List<Client> clients;
	private ClientTimeouts clientTimeouts;
	
	public Server(int port) throws IOException {
		server = new ServerSocket(port);
		clients = new ArrayList<>();
		
		clientTimeouts = new ClientTimeouts(this);
		Thread timeouts = new Thread(clientTimeouts);
		timeouts.start();
	}
	
	private void listenNewConnections() throws IOException {
		int id = 0;
		
		while (true) {
			Socket socket = server.accept();
			socket.setKeepAlive(true);
			Client client = new Client(this, socket, ++id);
			addClient(client);
			sendAllClientIDsToClient(client);
			log(String.format("New Client ID %d with IP %s joined", id, socket.getInetAddress().getHostAddress()));
		}
	}
	
	private void sendDataToClient(Client client, Type type, int id, int... data) {
		try {
			DataOutputStream dataOut = client.getConnection().getDataOutput();
			
			dataOut.writeByte(type.getValue());
			dataOut.writeInt(id);
			for (int d : data)
				dataOut.writeInt(d);
		} catch (IOException e) {}
	}
	
	private void sendDataToClient(Client client, Type type, int id, float... data) {
		try {
			DataOutputStream dataOut = client.getConnection().getDataOutput();
			
			dataOut.writeByte(type.getValue());
			dataOut.writeInt(id);
			for (float d : data)
				dataOut.writeFloat(d);
		} catch (IOException e) {}
	}
	
	public void sendDataToAllClients(Type type, int id, int... data) {
		for (Client client : new ArrayList<Client>(clients)) {
			sendDataToClient(client, type, id, data);
		}
	}
	
	public void sendDataToAllClients(Type type, int id, float... data) {
		for (Client client : new ArrayList<Client>(clients)) {
			sendDataToClient(client, type, id, data);
		}
	}
	
	public void sendAllClientIDsToClient(Client client) {
		for (Client listClient : new ArrayList<Client>(clients))
			if (listClient != client)
				sendDataToClient(client, Type.SERVER_CLIENT_JOINED, listClient.getID());
	}
	
	private void addClient(Client client) {
		clients.add(client);
		sendDataToAllClients(Type.SERVER_CLIENT_JOINED, client.getID());
	}
	
	public synchronized void kickClient(String reason, Client... kickClients) throws IOException {
		for (Client client : kickClients) {
			client.getConnection().getSocket().close();
			clients.remove(client);
			sendDataToAllClients(Type.SERVER_REMOVE_CLIENT, client.getID());
			log(String.format("Client ID %d disconnected (" + reason + ")", client.getID()));
		}
	}
	
	public List<Client> getClients() {
		return clients;
	}
	
	private static void log(String message) {
		System.out.println(message);
	}
	
	public static void main(String[] args) {
		try {
			log("Starting server on port " + PORT);
			Server server = new Server(PORT);
			log("Server started successfully!");
			server.listenNewConnections();
		} catch (IOException e) {
			log("Error while starting!");
			e.printStackTrace();
		}
	}

    public enum Type {
        CLIENT_INITIAL_HANDSHAKE(99),
        SERVER_INITIAL_HANDSHAKE(98),
    	
        SERVER_CLIENT_JOINED(0),
        SERVER_CLIENT_UPDATE_POS(1),
        SERVER_REMOVE_CLIENT(2),
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
