package Multiplayer.Packet;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public class Packet02Message extends Packet {
	
	private String message;
	
	public Packet02Message(String data) {
		super(02);
		message = readData(data);
	}

	
	public void writeData(GameClient client) {
		

	}

	
	public void writeData(GameServer server) {
		

	}

	
	public String getData() {
		return "02" + message;
	}
	
	
	public String getMessage() { return message; }
}
