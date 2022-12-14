package GameObject.Player;

import java.net.*;

import Multiplayer.Packet.Packet03Keypress;

public class JoystickReceiver implements Runnable{
	
	private Player player;
	private DatagramSocket socket;
	private int port;
	
	private boolean running = true;
	private Thread thread;
	
	public JoystickReceiver(Player player, int port) {
		this.player = player;
		this.port = port;
		
	}
	
	public void initiate()
	{
		try {
			socket = new DatagramSocket(port);
			thread = new Thread(this);
			thread.start();
		} catch (SocketException e) { e.printStackTrace(); }
	}
	
	public void terminate()
	{
		running = false;
		socket.close();
	}
	
	public void run() {
		while(running)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				if(socket.isClosed()) return;
				socket.receive(packet);
				System.out.println("packet received");
			} catch (Exception e) {  }
			
			Packet03Keypress keyPacket = new Packet03Keypress(new String(packet.getData()).trim());
			processKeypress(keyPacket.getKeyData());
		}
		
	}
	
	private void processKeypress(int[] key) {
		Player p = this.player;
		
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
	
}
