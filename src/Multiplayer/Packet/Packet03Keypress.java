package Multiplayer.Packet;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public class Packet03Keypress extends Packet {
	
	private String username;
	private int[] keyData = new int[2];
	
	public Packet03Keypress(String data) {
		super(03);
		String[] tokens = readData(data).split(" ");
		username = tokens[0];
		keyData[0] = Integer.parseInt(tokens[1]);
		keyData[1] = Integer.parseInt(tokens[2]);
	}
	
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	
	public void writeData(GameServer server) {
		server.sendDataToAll(getData());

	}

	public int[] getKeyData() { return keyData; }
	public String getUsername() { return username; }
	
	public String getData() {
		String s = "03" + username + " " + Integer.toString(keyData[0]) + " " + Integer.toString(keyData[1]);
		return s;
	}

}
