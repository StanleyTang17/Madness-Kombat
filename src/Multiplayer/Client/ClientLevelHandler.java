package Multiplayer.Client;

import GameObject.Item.Item;
import GameObject.Player.Player;
import GameObject.Weapon.Weapon;

public class ClientLevelHandler {
	
	public ClientLevel level;
	public GameClient client;
	
	public ClientLevelHandler(ClientLevel level, GameClient client) {
		this.level = level;
		this.client = client;
		level.setHandler(this);
		
	}
	
	public void processMoveObjectPacket(int p, double[] pData, int w, double[] wData, int i, double[] iData)
	{
		for(Player player : level.players) player.setVulnerable();
		for(int a = 0; a < p; a++)
		{
			Player obj = level.players.get(a);
			obj.setX(pData[a * 4]);
			obj.setY(pData[a * 4 + 1]);
			obj.setXVel(pData[a * 4 + 2]);
			obj.setYVel(pData[a * 4 + 3]);
		}
		
		for(int a = 0; a < w; a++)
		{
			Weapon obj = level.weapons.get(a);
			obj.setX(wData[a * 4]);
			obj.setY(wData[a * 4 + 1]);
			obj.setXVel(wData[a * 4 + 2]);
			obj.setYVel(wData[a * 4 + 3]);
		}
		
		for(int a = 0; a < i; a++)
		{
			Item obj = level.items.get(a);
			obj.setX(iData[a * 4]);
			obj.setY(iData[a * 4 + 1]);
			obj.setXVel(iData[a * 4 + 2]);
			obj.setYVel(iData[a * 4 + 3]);
		}
	}
}
