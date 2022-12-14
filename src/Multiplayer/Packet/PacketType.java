package Multiplayer.Packet;

public enum PacketType {
	INVALID(-1), LOGIN(00), DISCONNECT(01), MESSAGE(02), KEYPRESS(03), INIT(04), NEW_SPAWN(05), MOVE_OBJECTS(06);
	
	private int id;
	
	PacketType(int id)
	{
		this.id = id;
	}
	
	public int getId() { return id; }
}
