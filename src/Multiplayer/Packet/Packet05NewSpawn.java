package Multiplayer.Packet;

import java.awt.Point;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public class Packet05NewSpawn extends Packet {
	
	private String type;
	private int id;
	private Point spawnPoint;
	
	public Packet05NewSpawn(String data) {
		super(05);
		String[] tokens = readData(data).split(" ");
		type = tokens[0];
		id = Integer.parseInt(tokens[1]);
		spawnPoint = new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
	}

	public Packet05NewSpawn(String type, int id, Point spawnPoint) {
		super(05);
		this.type = type;
		this.id = id;
		this.spawnPoint = spawnPoint;
	}
	
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	
	public void writeData(GameServer server) {
		server.sendDataToAll(getData());

	}
	
	public String getType() { return type; }
	public int getId() { return id; }
	public Point getSpawnPoint() { return spawnPoint; }
	
	public String getData() {
		return "05" + type + " " + Integer.toString(id) + " " + Integer.toString(spawnPoint.x) + " " + Integer.toString(spawnPoint.y);
	}

}
