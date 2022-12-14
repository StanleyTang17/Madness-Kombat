package Multiplayer.Server;

import java.awt.Point;

import GameObject.Item.Item;
import GameObject.Player.Player;
import GameObject.Player.Team;
import GameObject.SpecialBlock.MysteryBlock;
import GameObject.Weapon.Weapon;
import Multiplayer.Packet.*;

public class ServerLevelHandler {

	private ServerLevel level;
	private GameServer server;
	
	public ServerLevelHandler(ServerLevel level, GameServer server) {
		this.level = level;
		this.server = server;
		level.setLevelHandler(this);
	}
	
	public String getPlayerData() {
		StringBuilder builder = new StringBuilder();
		builder.append(Player.PlayerList.size());
		builder.append(" ");
		for(Player player : Player.PlayerList)
		{
			builder.append(player.getX());
			builder.append(" ");
			builder.append(player.getY());
			builder.append(" ");
			builder.append(player.getXVel());
			builder.append(" ");
			builder.append(player.getYVel());
			builder.append(" ");
		}
		return builder.toString();
	}
	
	public void sendSpawnPacket(String type, int id, Point point)
	{
		Packet05NewSpawn spawnPacket = new Packet05NewSpawn(type, id, point);
		spawnPacket.writeData(server);
	}
	
	public void sendObjectMovementPacket()
	{
		int numP = level.players.size();
		double[] pData = new double[numP * 4];
		for(int i = 0; i < numP; i++)
		{
			Player obj = level.players.get(i);
			pData[i * 4] = obj.getX();
			pData[i * 4 + 1] = obj.getY();
			pData[i * 4 + 2] = obj.getXVel();
			pData[i * 4 + 3] = obj.getYVel();
		}
		
		int numW = level.weapons.size();
		double[] wData = new double[numW * 4];
		for(int i = 0; i < numW; i++)
		{
			Weapon obj = level.weapons.get(i);
			wData[i * 4] = obj.getX();
			wData[i * 4 + 1] = obj.getY();
			wData[i * 4 + 2] = obj.getXVel();
			wData[i * 4 + 3] = obj.getYVel();
		}
		
		int numI = level.items.size();
		double[] iData = new double[numI * 4];
		for(int i = 0; i < numI; i++)
		{
			Item obj = level.items.get(i);
			iData[i * 4] = obj.getX();
			iData[i * 4 + 1] = obj.getY();
			iData[i * 4 + 2] = obj.getXVel();
			iData[i * 4 + 3] = obj.getYVel();
		}
		
		Packet06MoveObjects packet = new Packet06MoveObjects(pData, wData, iData);
		packet.writeData(server);
	}
	
	public void processKeypress(String name, int[] key) {
		Player p = null;
		try {
			for(Player player : Player.PlayerList)
				if(player.getName().equals(name))
				{
					p = player;
					break;
				}
		} catch(Exception e) {e.printStackTrace(); }
		
		if(p == null) return;
		
		int k = key[0];
		int j = key[1];
		switch(k)
		{
		case 0:
			if(j == 1) p.upReleased();
			if(j == -1) p.upPressed();
			break;
		case 1:
			if(j == 1) p.downReleased();;
			if(j == -1) p.downPressed();
			break;
		case 2:
			if(j == 1) p.leftReleased();
			if(j == -1) p.leftPressed();
			break;
		case 3:
			if(j == 1) p.rightReleased();
			if(j == -1) p.rightPressed();
			break;
		case 4:
			if(j == 1) p.shootReleased();
			if(j == -1) p.shootPressed();
			break;
		}
	}

	public Packet04Init getInitPacket() {
		int n = level.players.size();
		String[] playerNames = new String[n];
		Team[] teamSet = new Team[n];
		String[] playerImageFileNames = new String[n];
		int w = level.weapons.size();
		int[] weaponIds = new int[w];
		int[] mysteryWeaponIds = new int[level.tileMap.getMysteryBlockCoords().size()];
		
		for(int i = 0; i < n; i++)
		{
			Player p = level.players.get(i);
			playerNames[i] = p.getName();
			teamSet[i] = p.getTeam();
			playerImageFileNames[i] = "/playerMailman.png";
		}
		
		for(int i = 0; i < w; i++)
		{
			weaponIds[i] = level.library.getWeaponId(level.weapons.get(i));
		}
		
		for(int i = 0; i < level.tileMap.getMysteryBlockCoords().size(); i++)
		{
			if(level.specialBlocks.get(i).getClass().equals(MysteryBlock.class))
			{
				MysteryBlock m = (MysteryBlock) level.specialBlocks.get(i);
				mysteryWeaponIds[i] = m.getWeaponId();
			}
		}
		
		Packet04Init initPacket = new Packet04Init(server.mapFileName , playerNames, teamSet,
												   playerImageFileNames, weaponIds, mysteryWeaponIds);
		return initPacket;
	}

}
