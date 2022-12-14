package Multiplayer.Packet;

import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public class Packet06MoveObjects extends Packet {
	
	private double[] playerData;
	private double[] weaponData;
	private double[] itemData;
	private int numPlayers, numWeapons, numItems;
	
	public Packet06MoveObjects(String data) {
		super(06);
		try
		{
		
		String[] strings = readData(data).split(" ");
		List<String> list = Arrays.asList(strings);
		Stack<String> tokens = new Stack<String>();
		Collections.reverse(list);
		tokens.addAll(list);
		tokens.pop(); //?????
		numPlayers = Integer.parseInt(tokens.pop());
		playerData = new double[numPlayers * 4];
		for(int i = 0; i < numPlayers * 4; i++)
			playerData[i] = Double.parseDouble(tokens.pop());
		
		numWeapons = Integer.parseInt(tokens.pop());
		weaponData = new double[numWeapons * 4];
		for(int i = 0; i < numWeapons * 4; i++)
			weaponData[i] = Double.parseDouble(tokens.pop());
		
		numItems = Integer.parseInt(tokens.pop());
		itemData = new double[numItems * 4];
		for(int i = 0; i < numItems * 4; i++)
			itemData[i] = Double.parseDouble(tokens.pop());
		}catch(EmptyStackException e) { System.out.println("empty stack"); }
	}
	
	public Packet06MoveObjects(double[] playerData, double[] weaponData, double[] itemData)
	{
		super(06);
		this.playerData = playerData;
		this.numPlayers = playerData.length / 4;
		this.weaponData = weaponData;
		this.numWeapons = weaponData.length / 4;
		this.itemData = itemData;
		this.numItems = itemData.length / 4;
	}
	
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	
	public void writeData(GameServer server) {
		server.sendDataToAll(getData());

	}

	public double[] getPlayerData() { return this.playerData; }
	public double[] getWeaponData() { return this.weaponData; }
	public double[] getItemData() { return this.itemData; }
	public int getPlayerCount() { return this.numPlayers; }
	public int getWeaponCount() { return this.numWeapons; }
	public int getItemCount() { return this.numItems; }
	
	public String getData() {
		StringBuilder builder = new StringBuilder();
		builder.append("06");
		builder.append(" ");
		
		builder.append(numPlayers);
		builder.append(" ");
		for(int i = 0; i < playerData.length; i++)
		{
			builder.append(playerData[i]);
			builder.append(" ");
		}
		
		builder.append(numWeapons);
		builder.append(" ");
		for(int i = 0; i < weaponData.length; i++)
		{
			builder.append(weaponData[i]);
			builder.append(" ");
		}
		
		builder.append(numItems);
		builder.append(" ");
		for(int i = 0; i < itemData.length; i++)
		{
			builder.append(itemData[i]);
			builder.append(" ");
		}
		
		return builder.toString();
	}

}
