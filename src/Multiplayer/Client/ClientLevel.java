package Multiplayer.Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import Main.GamePanel;
import TileMap.TileMap;

public class ClientLevel{
	
	public TileMap tileMap;
	public ArrayList<Player> players;
	public ArrayList<Weapon> weapons;
	public ArrayList<Bullet> bullets;
	public ArrayList<Item> items;
	public ArrayList<SpecialBlock> specialBlocks;
	private ArrayList<Integer> playerPreset;
	private Team[] teamSet;
	private Library library;
	//private long spawnTime;
	private int[] scores = {0, 0, 0, 0};
	private boolean initialized;
	private boolean countdown;
	private boolean GameOver;
	private long countdownTime;
	private ClientLevelHandler handler;
	
	public ClientLevel()
	{
		players = Player.PlayerList;
		weapons = Weapon.WeaponList;
		bullets = Bullet.BulletList;
		items = Item.ItemList;
		specialBlocks = SpecialBlock.SpecialBlockList;
		library = new Library();
		//spawnTime = 0;
		initialized = false;
		
		//useless code right here
		handler.hashCode();
	}
	
	public void init(String mapFileName, String[] pNames, Team[] teamSet, 
			String[] imageFileNames, int[] weaponIds, int[] mysteryWeaponIds) {
		clearObjects();
		initTileMap(mapFileName, mysteryWeaponIds);
		initPlayers(pNames, teamSet, imageFileNames);
		initWeapons(weaponIds);
		countdown = false;
		GameOver = false;
		//countdownTime = System.currentTimeMillis() + 3000;
		//newSpawnTime();
		initialized = true;
	}

	private void initPlayers(String[] names, Team[] teamSet, String[] imageFileNames) {
		for(int i = 0; i < names.length; i++)
		{
			try
			{
				BufferedImage image = ImageIO.read(getClass().getResource(imageFileNames[i]));
				Player p = new Player(image, tileMap, i + 1, scores[i], teamSet[i]);
				p.init();
				p.setName(names[i]);
				p.spawn(new Point(1, 1));
			} catch(IOException e) { e.printStackTrace(); }
		}
	}
	
	private void initWeapons(int[] ids) {
		for(int i = 0; i < ids.length; i++)
		{
			Weapon w = library.getNewFirearm(ids[i], tileMap);
			w.spawn(new Point(1, 1));
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
			//checkSpawns();
		}
		else
		{
			Weapon.updateWeapons();
			if(System.currentTimeMillis() > countdownTime)
				countdown = false;
		}
	}

	public void draw(Graphics2D g) {
		if(!initialized) return;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		if(tileMap != null) tileMap.draw(g);
		Item.drawItems(g);
		Player.drawPlayers(g);
		Weapon.drawWeapons(g);
		Bullet.drawBullets(g);
		if(countdown)
		{
			String time = String.valueOf((int)((countdownTime - System.currentTimeMillis()) / 1000) + 1);
			g.setColor(Color.BLACK);
			g.setFont(new Font("impact", Font.BOLD, 60));
			g.drawString(time, 
					GamePanel.WIDTH / 2 - g.getFontMetrics().stringWidth(time) / 2, 
					GamePanel.HEIGHT / 2 + g.getFontMetrics().getHeight() / 2);
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
	
	private void initTileMap(String mapFileName, int[] mysteryWeaponIds)
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
				new MysteryBlock(coord, mysteryWeaponIds[i]);
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
	
	public void spawnWeapon(int id, Point p)
	{
		Weapon w = library.getNewFirearm(id, tileMap);
		w.spawn(p);
	}
	
	public void spawnItem(int id, Point p)
	{
		Item i = library.getNewItem(id, tileMap);
		i.spawn(p);
	}
	
	public boolean isInitialized() { return initialized; }
	public int getScore(int id) { return scores[id - 1]; }
	public void setLibrary(Library lib) { library = lib; }
	public Library getLibrary() { return this.library; }
	public Team[] getTeamSet() { return teamSet; }
	public void setTeamSet(Team[] teamSet) { this.teamSet = teamSet; }
	public boolean isGameOver() { return GameOver; }
	public void setHandler(ClientLevelHandler handler) { this.handler = handler; }
}
