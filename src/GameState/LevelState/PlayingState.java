package GameState.LevelState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import GameObject.Library;
import GameObject.Item.Item;
import GameObject.Player.Player;
import GameObject.Player.PlayerKeyset;
import GameObject.Player.Team;
import GameObject.SpecialBlock.LaunchBlock;
import GameObject.SpecialBlock.MysteryBlock;
import GameObject.SpecialBlock.SpecialBlock;
import GameObject.SpecialBlock.Spike;
import GameObject.Weapon.Bullet;
import GameObject.Weapon.Explosion;
import GameObject.Weapon.Weapon;
import GameState.GameState;
import GameState.GameStateManager;
import Main.GamePanel;
import Main.KeyHandler;
import TileMap.TileMap;

public class PlayingState extends GameState{
	
	private GameStateManager gsm;
	private TileMap tileMap;
	private ArrayList<Player> players;
	private ArrayList<Weapon> weapons;
	private ArrayList<Bullet> bullets;
	private ArrayList<Item> items;
	private ArrayList<SpecialBlock> specialBlocks;
	private ArrayList<Integer> playerPreset;
	private PlayerKeyset[] keysets;
	private Team[] teamSet;
	private BufferedImage[] playerImages;
	private KeyHandler keyHandler;
	private GamePanel gp;
	private LevelState ls;
	private Library library;
	private long spawnTime;
	private int[] scores = {0, 0, 0, 0};
	private boolean initialized;
	private boolean countdown;
	private long countdownTime;
	private long countSec;
	
	public PlayingState(GameStateManager gsm, LevelState ls)
	{
		this.ls = ls;
		this.gsm = gsm;
		players = Player.PlayerList;
		weapons = Weapon.WeaponList;
		bullets = Bullet.BulletList;
		items = Item.ItemList;
		specialBlocks = SpecialBlock.SpecialBlockList;
		gp = gsm.gp;
		spawnTime = 0;
		initialized = false;
	}
	
	public void init() {
		countdown = true;
		countdownTime = System.currentTimeMillis() + 3000;
		tileMap = new TileMap(30);
		tileMap.loadTiles("/tileset.png");
		tileMap.loadMap(GamePanel.getLevelFile());
		keysets = gsm.getKeyset();
		playerPreset = gsm.getPlayerPreset();
		keyHandler = new KeyHandler(gp);
		players.clear();
		while(!weapons.isEmpty()) weapons.get(0).removeThis();
		bullets.clear();
		items.clear();
		specialBlocks.clear();
		Explosion.ExplosionList.clear();
		Player.setSpawnPoints(tileMap.getPlayerSpawnPoints());
		Weapon.setSpawnPoints(tileMap.getFirearmSpawnPoints());
		ArrayList<Point> tmpPlayerSpawnPoints = Player.getSpawnPoints();
		ArrayList<Point> tmpFirearmSpawnPoints = Weapon.getSpawnPoints();
		Random r = new Random();
		for(int a = 0; a < playerPreset.size(); a++)
		{
			int index = r.nextInt(tmpPlayerSpawnPoints.size());
			Point pt = tmpPlayerSpawnPoints.get(index);
			int id = playerPreset.get(a);
			Player p = new Player(playerImages[a], tileMap, keysets[id - 1], id, scores[id - 1], teamSet[a]);
			p.init();
			p.initKeyActions(keyHandler);
			p.spawn(pt);
			p.setName(gsm.getPlayerNames()[id - 1]);
			tmpPlayerSpawnPoints.remove(index);
		}
		for(int i = 0; i < players.size(); i++)
		{
			Weapon f = library.getRandomFirearm(tileMap);
			int index = new Random().nextInt(tmpFirearmSpawnPoints.size());
			f.spawn(tmpFirearmSpawnPoints.get(index));
			tmpFirearmSpawnPoints.remove(index);
		}
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
				int index = new Random().nextInt(library.getWeaponPreset().size());
				new MysteryBlock(coord, library.getWeaponPreset().get(index));
			}
		ArrayList<Point> launchCoords = tileMap.getLaunchBlockCoords();
		if(!launchCoords.isEmpty())
			for(Point p : launchCoords)
			{
				new LaunchBlock(p);
			}
		newSpawnTime();
		initialized = true;
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
			checkSpawnWeapon();
		}
		else
		{
			Weapon.updateWeapons();
			countSec = (int)((countdownTime - System.currentTimeMillis()) / 1000) + 1;
			if(System.currentTimeMillis() > countdownTime)
				countdown = false;
		}
	}

	public void draw(Graphics2D g) {
		if(!initialized) return;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		if(tileMap != null) tileMap.draw(g);		
		Player.drawPlayers(g);
		Item.drawItems(g);
		Weapon.drawWeapons(g);
		Bullet.drawBullets(g);
		if(countdown)
		{
			String time = String.valueOf(countSec);
			g.setColor(Color.BLACK);
			g.setFont(new Font("impact", Font.PLAIN, 60));
			g.drawString(time, 
					GamePanel.WIDTH / 2 - g.getFontMetrics().stringWidth(time) / 2, 
					GamePanel.HEIGHT / 2 + g.getFontMetrics().getHeight() / 2);
		}
	}

	public void keyPressed(int k) {
		
		if(k == KeyEvent.VK_ESCAPE)
		{
			ls.setState(LevelState.PAUSE);
			ls.initState(LevelState.PAUSE);
		}
		
	}

	public void keyReleased(int k) {
		
	}
	
	private void checkRoundEnd()
	{
		GameMode gm = LevelState.getGameMode();
		if(gm == GameMode.LAST_MAN_STANDING)
		{
			if(players.size() == 1)
			{
				scores[this.getWinningPlayer().getID() - 1] += 1;
				ls.initState(LevelState.TRANSITION);
				ls.setState(LevelState.TRANSITION);
			}
			else if(players.size() == 0)
			{
				ls.initState(LevelState.TRANSITION);
				ls.setState(LevelState.TRANSITION);
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
				ls.initState(LevelState.TRANSITION);
				ls.setState(LevelState.TRANSITION);
			}
			else if(players.size() == 0)
			{
				ls.initState(LevelState.TRANSITION);
				ls.setState(LevelState.TRANSITION);
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
				ls.initState(LevelState.TRANSITION);
				ls.setState(LevelState.TRANSITION);
			}
		}
	}
	
	private void checkSpawnWeapon()
	{
		if(spawnTime == 0) return;
		if(System.currentTimeMillis() >= spawnTime)
		{
			library.newRandomSpawn(tileMap);
			newSpawnTime();
		}
	}
	
	private void newSpawnTime()
	{
		Random r = new Random();
		spawnTime = System.currentTimeMillis() + r.nextInt(10000) + 3000;
	}
	
	public void setKeyset(PlayerKeyset keysets[])
	{
		this.keysets = keysets;
	}
	
	public void setKeyset(int index, PlayerKeyset keyset)
	{
		keysets[index] = keyset;
	}
	
	public PlayerKeyset getKeyset(int index)
	{
		return keysets[index];
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
	public int getScore(int id) { return scores[id - 1]; }
	public void setLibrary(Library lib) { library = lib; }
	public Team[] getTeamSet() { return teamSet; }
	public void setTeamSet(Team[] teamSet) { this.teamSet = teamSet; }
	public void setPlayerImages(BufferedImage[] images) { this.playerImages = images; }
	
}
