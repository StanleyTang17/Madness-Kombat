package Multiplayer.Packet;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public class Packet01Disconnect extends Packet{
	
	private String username;
	
	public Packet01Disconnect(String data) {
		super(01);
		this.username = readData(data);
	}
	
	public void writeData(GameClient client) {
		client.sendData(getData());
		
	}

	
	public void writeData(GameServer server) {
		server.sendDataToAll(getData());
		
	}

	
	public String getData() {
		return "01" + this.username;
	}
	
	public String getUsername()
	{
		return username;
	}

}
