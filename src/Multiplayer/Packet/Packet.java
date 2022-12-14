package Multiplayer.Packet;

import Multiplayer.Client.GameClient;
import Multiplayer.Server.GameServer;

public abstract class Packet {
	
	public byte packetId;
	
	public Packet(int packetId) {
		this.packetId = (byte) packetId;
		
	}

	public abstract void writeData(GameClient client);
	public abstract void writeData(GameServer server);
	public abstract String getData();
	
	public String readData(String data)
	{
		String s = null;
		try
		{
			Integer.parseInt(data.substring(0, 2));
			s = data.substring(2);
		}catch(NumberFormatException e) { 
			s = data; 
		}catch(java.lang.StringIndexOutOfBoundsException e) {
			
		}
		return s;
	}
	
	public static PacketType lookupPacket(int id)
	{
		for(PacketType p : PacketType.values())
			if(p.getId() == id)
				return p;
		return PacketType.INVALID;
	}
}
