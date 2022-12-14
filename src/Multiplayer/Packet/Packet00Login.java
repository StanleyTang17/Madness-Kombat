package Multiplayer.Packet;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public class Packet00Login extends Packet {
	
	private String username;
	
	public Packet00Login(String data) {
		super(00);
		this.username = this.readData(data);
	}
	
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	
	public void writeData(GameServer server) {
		server.sendDataToAll(getData());

	}

	
	public String getData() {
		return "00" + this.username;
	}
	
	public String getUsername() { return this.username; }
}
