package Multiplayer.Server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JPanel;

import Multiplayer.Window;
import Multiplayer.Packet.*;
import Multiplayer.Network.*;

public class GameServer extends JPanel implements Multiplayer.WindowListener, Runnable, ServerListener{

	private static final long serialVersionUID = 1L;
	private Server server;
	private int port;
	
	public String mapFileName = "2.map";
	
	private boolean levelRunning;
	private ServerLevel level;
	private ServerLevelHandler levelHandler;
	
	private ArrayList<ClientInstance> clientInstances = new ArrayList<ClientInstance>();
	
	public GameServer() {
		try {
			port = 1331;
			server = new Server(port, this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setPreferredSize(new java.awt.Dimension(800, 600));
		this.setFocusable(true);
		new Window(this, this, new java.awt.Dimension(800, 600), "Madness Kombat Server");
		this.requestFocus();
		new Thread(this).start();
	}

	public void update() {
		
		
	}


	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 800, 800);
		g.setColor(Color.BLACK);
		g.setFont(new Font("consolas",Font.PLAIN,24));
		g.drawString("connected clients: ", 50, 70);
		for(int i = 0; i < server.getClients().size(); i++)
		{
			Socket s = server.getClients().get(i);
			String str = s.getInetAddress().toString() + " " + Integer.toString(s.getPort());
			g.drawString(str, 50, 110 + i * 50);
		}
		
		
	}

	
	private void parsePacket(String data, InetAddress ip, int port)
	{
		PacketType type = Packet.lookupPacket(Integer.parseInt(data.substring(0, 2)));
		String s = null;
		switch(type)
		{
		case INVALID:
			s = "Invalid packet received from " + "[" + ip.getHostAddress().trim() + ":" + Integer.toString(port) + "] ";
			break;
		case LOGIN:
			Packet00Login loginPacket = new Packet00Login(data);
			s = "[" + ip.getHostAddress().trim() + ":" + Integer.toString(port) + "] " + loginPacket.getUsername() + " has joined";
			this.clientInstances.get(this.clientInstances.size() - 1).username = loginPacket.getUsername();
			if(server.getClientCount() > 1) startLevel();
			break;
		case DISCONNECT:
			
			break;
		case MESSAGE:
			
			break;
		case KEYPRESS:
			Packet03Keypress keyPacket = new Packet03Keypress(data);
			levelHandler.processKeypress(keyPacket.getUsername(), keyPacket.getKeyData());
			break;
		default:
			
			break;
		}
		if(s!=null)System.out.println(s);
	}
	
	private void startLevel()
	{
		level = new ServerLevel();
		levelHandler = new ServerLevelHandler(level, this);
		level.init(clientInstances, mapFileName);
		
		sendInitPacket();
		
		levelRunning = true;
		new Thread(new Runnable() {
			public void run() {
				runLevel();
			}
		}).start();
	}
	
	public void sendData(String data, InetAddress ipAddress, int port)
	{
		
	}
	
	public void sendDataToEveryOther(ClientInstance client, String data)
	{
		try
		{
			server.sendDataToEveryOtherClient(client, data);
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	public void sendDataToAll(String data) {
		try
		{
			server.sendDataToAllClients(data);
		}catch(IOException e) { e.printStackTrace(); }
		
	}
	
	public static void main(String[] args)
	{
		new GameServer();
	}
	
	private void runLevel()
	{
		long start, elapsed, wait, targetTime = 1000 / 60;
		while(levelRunning)
		{
			start = System.nanoTime();
			
			level.update();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			try
			{
				Thread.sleep(wait);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void sendInitPacket()
	{
		Packet04Init initPacket = levelHandler.getInitPacket();
		initPacket.writeData(this);
		
	}
	
	public void run() {
		while(true)
		{
			update();
		}
		
	}

	@Override
	public void clientConncted(ClientInstance client, PrintWriter out) {
		clientInstances.add(client);
		
	}

	@Override
	public void clientDisconnected(ClientInstance client) {
		
		
	}

	@Override
	public void recivedInput(ClientInstance client, String data) {
		parsePacket(data, client.ip, client.port);
		
	}

	@Override
	public void serverClosed() {
		// TODO Auto-generated method stub
		
	}
	
}
