package Multiplayer.Network;

import java.net.InetAddress;

public class ClientInstance{
	public final InetAddress ip;
	public final int port;
	public String username;
	public ClientInstance(InetAddress ip, int port){
		this.ip=ip;
		this.port=port;
	}
	@Override public String toString(){ return ip.toString()+":"+port; }
}
