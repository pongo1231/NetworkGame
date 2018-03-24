package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
	private Socket socket;
	private int id;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	
	public Client(Socket socket, int id) throws IOException {
		this.socket = socket;
		this.id = id;
		dataInput = new DataInputStream(socket.getInputStream());
		dataOutput = new DataOutputStream(socket.getOutputStream());
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public int getID() {
		return id;
	}
	
	public DataInputStream getDataInput() {
		return dataInput;
	}
	
	public DataOutputStream getDataOutput() {
		return dataOutput;
	}
}
