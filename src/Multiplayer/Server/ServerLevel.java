package Multiplayer.Server;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import GameObject.Library;
import GameObject.Item.Item;
import GameObject.Player.Player;
import GameObject.Player.Team;
import GameObject.SpecialBlock.LaunchBlock;
import GameObject.SpecialBlock.MysteryBlock;
import GameObject.SpecialBlock.SpecialBlock;
import GameObject.SpecialBlock.Spike;
import GameObject.Weapon.Bullet;
import GameObject.Weapon.Explosion;
import GameObject.Weapon.Weapon;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import Multiplayer.Network.ClientInstance;
import TileMap.TileMap;

public class ServerLevel{
	
	public TileMap tileMap;
	public ArrayList<Player> players;
	public ArrayList<Weapon> weapons;
	public ArrayList<Bullet> bullets;
	public ArrayList<Item> items;
	public ArrayList<SpecialBlock> specialBlocks;
	public ArrayList<Integer> playerPreset;
	public Team[] teamSet = { Team.BLUE, Team.RED, Team.BLUE, Team.RED };
	public Library library;
	//private long spawnTime;
	private int[] scores = {0, 0, 0, 0};
	private boolean initialized;
	private boolean countdown;
	private boolean GameOver;
	private long countdownTime;
	private ServerLevelHandler handler;
	private long spawnTime;
	
	//private int ticksPerServerUpdate = 2;
	//private int curTick = ticksPerServerUpdate + 1;
	
	public ServerLevel()
	{
		players = Player.PlayerList;
		weapons = Weapon.WeaponList;
		bullets = Bullet.BulletList;
		items = Item.ItemList;
		specialBlocks = SpecialBlock.SpecialBlockList;
		library = new Library();
		//spawnTime = 0;
		initialized = false;
	}
	
	public void init(ArrayList<ClientInstance> clients, String mapFileName) {
		clearObjects();
		initTileMap(mapFileName);
		initPlayers(clients);
		initWeapons(clients.size());
		countdown = false;
		GameOver = false;
		//countdownTime = System.currentTimeMillis() + 3000;
		newSpawnTime();
		initialized = true;
	}

	private void initPlayers(ArrayList<ClientInstance> clients) {
		for(int i = 0; i < clients.size(); i++)
		{
			try
			{
				BufferedImage image = ImageIO.read(getClass().getResource("/playerAgent.png"));
				Player p = new Player(image, tileMap, i + 1, scores[i], teamSet[i]);
				p.init();
				p.setName(clients.get(i).username);
				p.spawnRandom(tileMap.getPlayerSpawnPoints());
			} catch(IOException e) { e.printStackTrace(); }
		}
	}
	
	private void initWeapons(int count) {
		for(int i = 0; i < count; i++)
		{
			library.newRandomFirearm(tileMap);
		}
	}

	public void update() {
		if(!initialized) return;
		if(!countdown)
		{
			Player.updatePlayers();
			Weapon.updateWeapons();
			Bullet.updateBullets();
			Item.updateItems();
			SpecialBlock.updateSpecialBlocks(tileMap);
			checkRoundEnd();
			checkSpawns();
			checkServerUpdate();
			
		}
		else
		{
			Weapon.updateWeapons();
			if(System.currentTimeMillis() > countdownTime)
				countdown = false;
		}
	}
	
	private void checkServerUpdate() {
		
			handler.sendObjectMovementPacket();
		
	}

	private void checkSpawns() {
		long cur = System.currentTimeMillis();
		if(cur > spawnTime)
		{
			Random r = new Random();
			String[] types = {"weapon","item"};
			String type = types[r.nextInt(2)];
			int id = 0;
			if(type.equals("weapon")) id = library.getWeaponPreset().get(r.nextInt(library.getWeaponPreset().size()));
			else if(type.equals("item")) id = library.getItemPreset().get(r.nextInt(library.getItemPreset().size()));
			ArrayList<Point> ps = tileMap.getFirearmSpawnPoints();
			Point p = ps.get(r.nextInt(ps.size()));
			
			handler.sendSpawnPacket(type, id, p);
			
			newSpawnTime();
		}
		
	}

	private void checkRoundEnd()
	{
		GameMode gm = LevelState.getGameMode();
		if(gm == GameMode.LAST_MAN_STANDING)
		{
			if(players.size() == 1)
			{
				scores[this.getWinningPlayer().getID() - 1] += 1;
				endRound();
			}
			else if(players.size() == 0)
			{
				endRound();
			}
		}
		else if(gm == GameMode.TEAM)
		{
			if(players.size() == 1)
			{
				Team winningTeam = this.getWinningPlayer().getTeam();
				for(int i = 0; i < 4; i++)
				{
					if(teamSet[i] == winningTeam)
					{
						scores[i] += 1;
					}
				}
				endRound();
			}
			else if(players.size() == 0)
			{
				endRound();
			}
			else
			{
				Team team = this.getWinningPlayer().getTeam();
				for(int i = 1; i < players.size(); i++)
				{
					if(players.get(i).getTeam() != team) return;
				}
				for(int i = 0; i < 4; i++)
				{
					if(teamSet[i] == team)
					{
						scores[i] += 1;
					}
				}
				endRound();
			}
		}
	}
	
	private void endRound()
	{
		//GameOver = true;
	}
	
	private void initTileMap(String mapFileName)
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/tileset.png");
		tileMap.loadMap(new File(System.getProperty("user.dir") + "//" + mapFileName));
		Player.setSpawnPoints(tileMap.getPlayerSpawnPoints());
		Weapon.setSpawnPoints(tileMap.getFirearmSpawnPoints());
		ArrayList<Point> spikeCoords = tileMap.getSpikeCoords();
		if(!spikeCoords.isEmpty())
			for(int i = 0; i < spikeCoords.size(); i++)
			{
				Point coord = spikeCoords.get(i);
				new Spike(coord);
			}
		ArrayList<Point> mysteryCoords = tileMap.getMysteryBlockCoords();
		if(!mysteryCoords.isEmpty())
			for(int i = 0; i < mysteryCoords.size(); i++)
			{
				Point coord = mysteryCoords.get(i);
				new MysteryBlock(coord, library.getWeaponPreset().get(new Random().nextInt(library.getWeaponPreset().size())));
			}
		ArrayList<Point> launchCoords = tileMap.getLaunchBlockCoords();
		if(!launchCoords.isEmpty())
			for(Point p : launchCoords)
			{
				new LaunchBlock(p);
			}
	}
	
	private void clearObjects()
	{
		players.clear();
		while(!weapons.isEmpty()) weapons.get(0).removeThis();
		bullets.clear();
		items.clear();
		specialBlocks.clear();
		Explosion.ExplosionList.clear();
	}
	
	public ArrayList<Integer> getPlayerPreset()
	{
		return playerPreset;
	}
	
	public void setPlayerPreset(ArrayList<Integer> preset)
	{
		playerPreset = preset;
	}
	
	public Player getWinningPlayer()
	{ 
		Player winner = null;
		if(!players.isEmpty()) winner = players.get(0);
		return winner; 
	}
	
	private void newSpawnTime()
	{
		spawnTime = System.currentTimeMillis() + new Random().nextInt(10000) + 5000;
	}
	
	
	
	public boolean isInitialized() { return initialized; }
	public int getScore(int id) { return scores[id - 1]; }
	public void setLibrary(Library lib) { library = lib; }
	public Library getLibrary() { return this.library; }
	public Team[] getTeamSet() { return teamSet; }
	public void setTeamSet(Team[] teamSet) { this.teamSet = teamSet; }
	public boolean isGameOver() { return GameOver; }
	public void setLevelHandler(ServerLevelHandler handler) { this.handler = handler; }
	
	
}
