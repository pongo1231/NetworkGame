package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientTimeouts implements Runnable {
	private Server server;
	private List<Client> clients;
	
	public ClientTimeouts(Server server) {
		this.server = server;
		clients = new ArrayList<>();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				
				List<Client> toBeKicked = new ArrayList<>();
				for (Client client : clients) {
					client.updateTimeoutTime();
					if (client.getTimeoutTime() <= 0)
						toBeKicked.add(client);
				}
				
				for (Client client : toBeKicked)
					server.kickClient("Timed out", client);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addClient(Client client) {
		clients.add(client);
	}
	
	public void removeClient(Client client) {
		clients.remove(client);
	}
}
