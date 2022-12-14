package Multiplayer.Packet;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

import java.util.*;

import GameObject.Player.Team;

public class Packet04Init extends Packet {
	
	private String mapFileName;
	private String[] playerNames;
	private Team[] teamSet;
	private String[] playerImageFileNames;
	private int[] weaponIds;
	private int[] mysteryWeaponIds;
	
	public Packet04Init(String mapFileName, String[] playerNames, Team[] teamSet,
						String[] playerImageFileNames, int[] weaponIds, int[] mysteryWeaponIds)
	{
		super(04);
		this.mapFileName = mapFileName;
		this.playerNames = playerNames;
		this.teamSet = teamSet;
		this.playerImageFileNames = playerImageFileNames;
		this.weaponIds = weaponIds;
		this.mysteryWeaponIds = mysteryWeaponIds;
	}
	
	public Packet04Init(String data) {
		super(04);
		String[] strings = readData(data).split(" ");
		List<String> list = Arrays.asList(strings);
		Stack<String> tokens = new Stack<String>();
		Collections.reverse(list);
		tokens.addAll(list);
		
		mapFileName = tokens.pop();
		
		int numPlayers = Integer.parseInt(tokens.pop());
		
		playerNames = new String[numPlayers];
		for(int i = 0; i < numPlayers; i++)
		{
			playerNames[i] = tokens.pop();
		}
		
		teamSet = new Team[numPlayers];
		for(int i = 0; i < numPlayers; i++)
		{
			teamSet[i] = Team.values()[Integer.parseInt(tokens.pop())];
		}
		
		playerImageFileNames = new String[numPlayers];
		for(int i = 0; i < numPlayers; i++)
		{
			playerImageFileNames[i] = tokens.pop();
		}
		
		int numWeapons = Integer.parseInt(tokens.pop());
		
		weaponIds = new int[numWeapons];
		for(int i = 0; i < numPlayers; i++)
		{
			weaponIds[i] = Integer.parseInt(tokens.pop());
		}
		
		int numMysteryWeapons = Integer.parseInt(tokens.pop());
		
		mysteryWeaponIds = new int[numMysteryWeapons];
		for(int i = 0; i < numMysteryWeapons; i++)
		{
			mysteryWeaponIds[i] = Integer.parseInt(tokens.pop());
		}
			
	}

	
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	
	public void writeData(GameServer server) {
		server.sendDataToAll(getData());

	}

	public String getMapFileName() { return this.mapFileName; }
	public String[] getPlayerNames() { return this.playerNames; }
	public Team[] getTeamSet() { return this.teamSet; }
	public String[] getPlayerImageFileNames() { return this.playerImageFileNames; }
	public int[] getWeaponIds() { return this.weaponIds; }
	public int[] getMysteryWeaponIds() { return this.mysteryWeaponIds; }
	
	public String getData() {
		StringBuilder builder = new StringBuilder();
		builder.append("04");
		builder.append(mapFileName + " ");
		
		int numPlayers = playerNames.length;
		
		builder.append(numPlayers);
		builder.append(" ");
		
		for(int i = 0; i < numPlayers; i++)
			builder.append(playerNames[i] + " ");
		
		for(int i = 0; i < numPlayers; i++)
		{
			builder.append(teamSet[i].ordinal());
			builder.append(" ");
		}
		
		for(int i = 0; i < numPlayers; i++)
			builder.append(playerImageFileNames[i] + " ");
		
		builder.append(weaponIds.length);
		builder.append(" ");
		
		for(int i = 0; i < weaponIds.length; i++)
		{
			builder.append(weaponIds[i]);
			builder.append(" ");
		}
		
		builder.append(mysteryWeaponIds.length);
		builder.append(" ");
		
		for(int i = 0; i < mysteryWeaponIds.length; i++)
		{
			builder.append(mysteryWeaponIds[i]);
			builder.append(" ");
		}
		return builder.toString();
	}

}
