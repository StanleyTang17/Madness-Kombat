package Multiplayer.Client;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import Main.KeyHandler;
import Multiplayer.Window;
import Multiplayer.WindowListener;
import Multiplayer.Packet.*;
import Multiplayer.Network.*;

public class GameClient extends JPanel implements WindowListener, Runnable, ClientListener{
	
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	private static final long serialVersionUID = 1L;
	private int port;
	private String username;
	private ClientLevel level;
	private Thread levelThread;
	private boolean levelRunning;
	private KeyHandler keyHandler;
	
	private Client client;
	private ClientLevelHandler handler;
	
	public GameClient() {
		try {
			port = 1331;
			JTextField ipTxt = new JTextField();
			JTextField usrTxt = new JTextField();
			Object[] objs = {"IP address", ipTxt, "Username", usrTxt};
			int returnVal = JOptionPane.showConfirmDialog(null, objs, "Login", JOptionPane.OK_OPTION);
			if(returnVal == JOptionPane.NO_OPTION || returnVal == JOptionPane.CLOSED_OPTION) return;
			username = usrTxt.getText();
			String ip = ipTxt.getText();
			client = new Client(ip, port, this);
			Packet00Login loginPacket = new Packet00Login("00" + username);
			loginPacket.writeData(this);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		this.setPreferredSize(new java.awt.Dimension(1366, 768));
		this.setFocusable(true);
		new Window(this, this, new java.awt.Dimension(1366, 768), "Madness Kombat Client");
		this.requestFocus();
		new Thread(this).start();
		initKeyHandler();
	}
	
	public void update() {
		
		
	}
	
	public void draw(Graphics2D g) {
		if(level != null && level.isInitialized())
			level.draw(g);
		/*
		g.setColor(Color.BLACK);
		g.setFont(new Font("impact",Font.PLAIN, 30));
		g.drawString(username, 50, 50);
		
		if(tmp != null)
		{
			g.drawString(tmp, 50, 200);
		}
		*/
		
	}
	
	private void parsePacket(String data)
	{
		String msg = new String(data).trim();
		PacketType type = Packet.lookupPacket(Integer.parseInt(msg.substring(0, 2)));
		switch(type)
		{
		case MESSAGE:
			Packet02Message msgPacket = new Packet02Message(msg);
			System.out.println("Message from server: " + msgPacket.getMessage());
			break;
		case INIT:
			Packet04Init initPacket = new Packet04Init(data);
			level = new ClientLevel();
			level.init(initPacket.getMapFileName(), initPacket.getPlayerNames(), initPacket.getTeamSet(),
					   initPacket.getPlayerImageFileNames(), initPacket.getWeaponIds(), initPacket.getMysteryWeaponIds());
			handler = new ClientLevelHandler(level, this);
			initLevelThread();
			break;
		case NEW_SPAWN:
			Packet05NewSpawn spawnPacket = new Packet05NewSpawn(data);
			String spawnType = spawnPacket.getType();
			if(spawnType.equalsIgnoreCase("weapon"))
				level.spawnWeapon(spawnPacket.getId(), spawnPacket.getSpawnPoint());
			else if(spawnType.equalsIgnoreCase("item"))
				level.spawnItem(spawnPacket.getId(), spawnPacket.getSpawnPoint());
		case MOVE_OBJECTS:
			Packet06MoveObjects movePacket = new Packet06MoveObjects(data);
			handler.processMoveObjectPacket(movePacket.getPlayerCount(), movePacket.getPlayerData(), 
											movePacket.getWeaponCount(), movePacket.getWeaponData(), 
											movePacket.getItemCount(), movePacket.getItemData());
			break;
		default:
			
			
		}
	}

	public void sendData(String data)
	{
		client.send(data);
	}
	
	/*
	private void disconnect()
	{
		Packet01Disconnect disconnectPacket = new Packet01Disconnect(this.username);
		disconnectPacket.writeData(this);
	}
	*/
	
	private void initLevelThread()
	{
		levelRunning = true;
		levelThread = new Thread(new Runnable() {
			public void run() {
				runLevel();
			}
		});
		levelThread.start();
	}
	
	private void runLevel()
	{
		long start, elapsed, wait = 1000000000;
		while(levelRunning)
		{
			start = System.nanoTime();
			
			//level.update();
			if(level.isGameOver()) levelRunning = false;
			
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
	
	public static void main(String[] args)
	{
		new GameClient();
	}

	
	public void run() {
		while(true)
		{
			update();
		}
		
	}

	@Override
	public void unknownHost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void couldNotConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recivedInput(String data) {
		parsePacket(data);
		
	}

	@Override
	public void serverClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectedToServer() {
		// TODO Auto-generated method stub
		
	}
	
	private void sendKeypressData(int k, int j)
	{
		Packet03Keypress keyPacket = new Packet03Keypress(this.username + " " + Integer.toString(k) + " " + Integer.toString(j));
		keyPacket.writeData(this);
	}
	
	@SuppressWarnings("serial")
	private void initKeyHandler()
	{
		keyHandler = new KeyHandler(this);
		keyHandler.addAction(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up pressed", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(0, -1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "up released", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(0, 1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("S"), "down pressed", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(1, -1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("released S"), "down released", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(1, 1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("A"), "left pressed", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(2, -1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("released A"), "left released", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(2, 1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("D"), "right pressed", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(3, -1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("released D"), "right released", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(3, 1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("SPACE"), "shoot pressed", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(4, -1); }
		});
		keyHandler.addAction(KeyStroke.getKeyStroke("released SPACE"), "shoot released", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) { sendKeypressData(4, 1); }
		});
	}
	
	
}
