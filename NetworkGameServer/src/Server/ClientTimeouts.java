package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientTimeouts implements Runnable {
	private Server server;
	
	public ClientTimeouts(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				
				List<Client> toBeKicked = new ArrayList<>();
				for (Client client : server.getClients()) {
					client.getConnection().updateTimeoutTime();
					if (client.getConnection().getTimeoutTime() <= 0)
						toBeKicked.add(client);
				}
				
				for (Client client : toBeKicked)
					server.kickClient("Timed out", client);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
