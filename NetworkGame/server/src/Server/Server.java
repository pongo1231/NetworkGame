package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private static final int PORT = 41000;
	
	private ServerSocket server;
	private List<Client> clients;
	
	public Server(int port) throws IOException {
		server = new ServerSocket(port);
		clients = new ArrayList<>();
	}
	
	private void startListen() throws IOException {
		int id = 0;
		
		while (true) {
			Socket socket = server.accept();
			if (socket.isConnected()) {
				id++;
				clients.add(new Client(socket, id));
				sendDataToAllClients(ServerType.SERVER_CLIENT_JOINED, null);
				log(String.format("New client %d with IP %s", id, socket.getInetAddress().getHostAddress()));
			}
			
			for (Client client : clients) {
				try {
					readInput(client);
				} catch (IOException e) {
					removeClient(client);
					log(String.format("Client %d disconnected.", client.getID()));
					break;
				}
			}
		}
	}
	
	private void readInput(Client client) throws IOException {
		byte type = client.getDataInput().readByte();
		
		switch (type) {
			case 0:
				// TODO
				break;
		}
	}
	
	private void sendDataToAllClients(ServerType type, Object data) {
		for (Client client : clients) {
			try {
				client.getDataOutput().write(type.getValue());
			} catch (IOException e) {}
		}
	}
	
	private void removeClient(Client client) {
		clients.remove(client);
	}
	
	private static void log(String message) {
		System.out.println(message);
	}
	
	public static void main(String[] args) {
		try {
			log("Starting server on port " + PORT);
			Server server = new Server(PORT);
			log("Listening for clients...");
			server.startListen();
		} catch (IOException e) {
			log("Error while starting!");
			e.printStackTrace();
		}
	}
	
    public enum ClientType {
        CLIENT_UPDATE_POS(0);

        private final int value;
        private ClientType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ServerType {
        SERVER_CLIENT_JOINED(1000);

        private final int value;
        private ServerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
