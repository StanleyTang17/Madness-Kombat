package GameObject.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import GameObject.GameObject;
import GameObject.Item.Buff;
import GameObject.Player.Graphics.ParticleEffect.*;
import GameObject.Player.Graphics.PlayerGraphics;
import GameObject.SpecialBlock.Spike;
import GameObject.Weapon.Weapon;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import Main.GamePanel;
import Main.KeyHandler;
import Main.SoundEffect;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import TileMap.TileMap;

public class Player extends GameObject {
	
	private int id;
	private boolean moveLeft, moveRight, doubleJump, downPressed, shootPressed;
	private double speed, jumpSpeed;
	private ArrayList<Force> forceList;
	private Team team;
	
	private String name = "Player";
	
	private PlayerKeyset keyset;
	private Weapon weapon;
	private boolean hasWeapon;
	private double hitpoints;
	private int armor;
	private int lives;
	private long invincibleTime;
	private PlayerGraphics playerGraphics;
	private ArrayList<Buff> buffList;
	private int score;
	private SoundEffect dyingSound;
	
	public static final int MAX_HITPOINTS = 100;
	public static ArrayList<Player> PlayerList = new ArrayList<Player>();
	private static ArrayList<Point> SpawnPoints = new ArrayList<Point>();
	
	public static void updatePlayers()
	{
		if(!PlayerList.isEmpty())
			for(int i = 0; i < PlayerList.size(); i++)
			{
				PlayerList.get(i).update();
			}
	}
	
	public static void drawPlayers(Graphics2D g)
	{
		if(!PlayerList.isEmpty())
			for(int i = 0; i < PlayerList.size(); i++)
			{
				Player p = PlayerList.get(i);
				String name = p.getName();
				p.draw(g);
				
				int x = 10 + i * 250;
				int y = GamePanel.HEIGHT - 20;
				g.setColor(Color.BLACK);
				g.setFont(new Font("impact", Font.PLAIN, 15));
				g.drawString(name, x, y);
				
				g.setColor(Color.GREEN);
				x += g.getFontMetrics().stringWidth(name) + 5;
				y -= 15;
				g.fill(new Rectangle(x, y, (int)p.getHitpoints(), 20));
				
				g.setColor(Color.BLACK);
				g.draw(new Rectangle(x, y, 100, 20));
				
				String score = String.valueOf(p.getScore());
				g.setFont(new Font("impact", Font.PLAIN, 15));
				x += 50 - g.getFontMetrics().stringWidth(score) / 2;
				y += 15;
				g.drawString(score, x, y);
			}
	}
	
	public Player(BufferedImage image, TileMap tileMap, PlayerKeyset keys, int ID, int score, Team team) {
		forceList = new ArrayList<Force>();
		buffList = new ArrayList<Buff>();
		playerGraphics = new PlayerGraphics(this);
		setImage(image);
		setSizeByImage();
		setTileMap(tileMap);
		speed = 4;
		jumpSpeed = -10;
		keyset = keys;
		lives = 1;
		setHitBoxByImage();
		PlayerList.add(this);
		hitpoints = MAX_HITPOINTS;
		this.id = ID;
		this.score = score;
		dyingSound = new SoundEffect(getClass().getResource("/dying.wav"));
		this.team = team;
	}
	
	public Player(BufferedImage image, TileMap tileMap, int ID, int score, Team team)
	{
		forceList = new ArrayList<Force>();
		buffList = new ArrayList<Buff>();
		playerGraphics = new PlayerGraphics(this);
		dyingSound = new SoundEffect(getClass().getResource("/dying.wav"));
		
		this.setImage(image);
		this.setSizeByImage();
		this.setHitBoxBySize();
		
		setTileMap(tileMap);
		this.setID(ID);
		this.score = score;
		this.team = team;
		
		speed = 4;
		jumpSpeed = -10;
		lives = 1;
		hitpoints = MAX_HITPOINTS;
		
		PlayerList.add(this);
	}
	
	private void respawn()
	{
		Random r = new Random();
		int index = r.nextInt(SpawnPoints.size());
		Point p = SpawnPoints.get(index);
		spawn(p);
		setYVel(0);
		hitpoints = MAX_HITPOINTS;
		setInvincible();
	}
	
	public void init() {
		armor = 0;
		buffList.clear();
		moveLeft = false;
		moveRight = false;
		doubleJump = false;
		setInAir(true);
		setXVel(0);
		setYVel(5);
		setDirection(FACING_RIGHT);
		setInvincible();
	}
	
	public void update() {
		checkMoveLeftRight();
		if(!inAir()) doubleJump = false;
		updateForces();
		updateBuffs();
		checkHitVoid();
		checkShootPressed();
		terrainCollision();
		move();
		updateHitbox();
		updatePlayerGraphics();
	}
	
	public void draw(Graphics2D g) {
		playerGraphics.draw(g);
		
	}
	
	public void equipFirearm(Weapon f)
	{
		if(!f.isEquipped())
		{
			f.equip(this);
			weapon = f;
			hasWeapon = true;
		}
	}
	
	public void getNearbyFirearm()
	{
		ArrayList<Weapon> f = Weapon.WeaponList;
		for(int i = 0; i < f.size(); i++)
		{
			if(f.get(i).collidesWith(this))
			{
				equipFirearm(f.get(i));
				break;
			}
		}
	}
	
	public void takeDamage(double damage)
	{
		if(isInvincible()) return;
		hitpoints -= (1 - (double)armor / 200) * damage;
		if(hitpoints <= 0)
		{
			die();
		}
		this.getGraphics().addParticleEffect(new BloodParticle(this));
	}
	
	public void hitSpike()
	{
		if(isInvincible()) return;
		takeDamage(Spike.DAMAGE);
		setInvincible();
	}
	
	public void die()
	{
		lives--;
		if(lives <= 0)
			removeThis();
		if(hasWeapon)
			dropWeapon();
		respawn();
		dyingSound.play();
	}
	
	public void dropWeapon()
	{
		hasWeapon = false;
		weapon.setEquipped(false);
		weapon.releaseTrigger();
		weapon.setPlayer(null);
		weapon = null;
	}
	
	public void setDoubleJump(boolean doubleJump) { this.doubleJump = doubleJump; }
	public void removeRecoil(Force recoil) { forceList.remove(recoil); }
	public void addExternalForce(int duration, double x, double y) { new Force(duration, x, y, this); }
	public void setInvincible() { invincibleTime = System.currentTimeMillis() + 1000; this.playerGraphics.setState(PlayerGraphics.BLINKING); }
	public void setVulnerable() { invincibleTime = System.currentTimeMillis() - 1000; this.playerGraphics.setState(PlayerGraphics.STATIC); }
	public boolean isInvincible() { return invincibleTime >= System.currentTimeMillis(); }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public PlayerKeyset getKeyset() { return keyset; }
	public void setKeyset(PlayerKeyset keys) { keyset = keys; }
	public int getID() { return id; }
	public void setID(int id) { this.id = id; if(name.equals("Player")) { name += " " + String.valueOf(id); } }
	public double getHitpoints() { return hitpoints; }
	public void setHitpoints(double hp) { hitpoints = hp; }
	public void removeThis() { PlayerList.remove(PlayerList.indexOf(this)); }
	public static void setSpawnPoints(ArrayList<Point> spawnPoints) { SpawnPoints = spawnPoints; }
	public static ArrayList<Point> getSpawnPoints() { return SpawnPoints; }
	public boolean isDownPressed() { return downPressed; }
	public boolean hasWeapon() { return hasWeapon; }
	public PlayerGraphics getGraphics() { return playerGraphics; }
	public ArrayList<Buff> getBuffs() { return buffList; }
	public double getSpeed() { return speed; }
	public void setSpeed(double speed) { this.speed = speed; }
	public int getScore() { return score; }
	public void setScore(int score) { this.score = score; }
	public Weapon getWeapon() { return this.weapon; }
	public int getArmor() { return armor; }
	public void setArmor(int armor) { this.armor = armor; }
	public Team getTeam() { return team; }
	public void setTeam (Team team) { this.team = team; }
	
	public void leftPressed() {moveRight = false; moveLeft = true;}
	public void leftReleased() {moveLeft = false;}
	public void rightPressed() {moveRight = true; moveLeft = false;}
	public void rightReleased() {moveRight = false;}
	public void upPressed() {checkJump();}
	public void upReleased() {}
	public void downPressed() {
		downPressed = true;
		if(hasWeapon)
		{
			this.dropWeapon();
		}
		else
		{
			this.getNearbyFirearm();
		}
	}
	public void downReleased() {downPressed = false;}
	public void shootPressed() {shootPressed = true;}
	public void shootReleased() {shootPressed = false;}
	
	public void drawName(Graphics2D g)
	{
		Rectangle textBox = new Rectangle(100,20);
		textBox.x = (int) (this.getX() + this.getWidth() / 2 - textBox.width / 2);
		textBox.y = (int) (this.getY() - textBox.getHeight() - 5);
		String name = this.getName();
		if(LevelState.getGameMode() == GameMode.LAST_MAN_STANDING)
			g.setColor(Color.BLACK);
		else if(LevelState.getGameMode() == GameMode.TEAM)
			g.setColor(team.getColor(team));
		g.setFont(new Font("impact", Font.PLAIN, 15));
		FontMetrics fm = g.getFontMetrics();
		int fontX = textBox.x + (textBox.width - fm.stringWidth(name)) / 2;
		int fontY = textBox.y + ((textBox.height - fm.getHeight()) / 2) + fm.getAscent();
		g.drawString(name, fontX, fontY);
	}
	
	@SuppressWarnings("serial")
	public void initKeyActions(KeyHandler handler)
	{
		String id = String.valueOf(getID());
		handler.addAction(KeyStroke.getKeyStroke(keyset.getUpKey(), 0, false), "upPressed" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { checkJump(); }
				 });
		/*
		handler.addAction(KeyStroke.getKeyStroke(keyset.getUpKey(), 0, true), "upReleased" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { jump = false; }
				 });
		*/
		handler.addAction(KeyStroke.getKeyStroke(keyset.getLeftKey(), 0, false), "leftPressed" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { moveRight = false; moveLeft = true; }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getLeftKey(), 0, true), "leftReleased" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { moveLeft = false; }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getRightKey(), 0, false), "rightPressed" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { moveRight = true; moveLeft = false; }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getRightKey(), 0, true), "rightReleased" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { moveRight = false; }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getDownKey(), 0, false), "downPressed" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { downPressed(); }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getDownKey(), 0, true), "downReleased" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { downPressed = false; }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getShootKey(), 0, false), "shootPressed" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { shootPressed = true; }
				 });
		handler.addAction(KeyStroke.getKeyStroke(keyset.getShootKey(), 0, true), "shootReleased" + id,
				new AbstractAction() {
					@Override public void actionPerformed(ActionEvent arg0) { shootPressed = false; }
				 });
	}
	
	private void checkMoveLeftRight()
	{
		if(moveLeft)
		{
			setXVel(-speed);
			setDirection(FACING_LEFT);
			if(hasWeapon) weapon.setDirection(FACING_LEFT);
		}
		else if(moveRight)
		{
			setXVel(speed);
			setDirection(FACING_RIGHT);
			if(hasWeapon) weapon.setDirection(FACING_RIGHT);
		}
		else
		{
			setXVel(0);
		}
	}
	
	private void checkShootPressed()
	{
		if(shootPressed)
		{
			if(hasWeapon)
			{
				weapon.pullTrigger();
			}
		}
		else
		{
			if(hasWeapon) weapon.releaseTrigger();
		}
	}
	
	private void checkHitVoid()
	{
		if(getY() >= 800)
		{
			die();
			return;
		}
	}
	
	private void checkJump()
	{
		if(!inAir())
		{
			setYVel(jumpSpeed);
			setInAir(true);
		}
		else if(!doubleJump)
		{
			setYVel(jumpSpeed);
			setInAir(true);
			doubleJump = true;
		}
		
		
	}
	
	private void updatePlayerGraphics()
	{
		playerGraphics.update();
	}
	
	private void updateBuffs()
	{
		try
		{
			for(Buff buff : buffList)
				buff.update();
		} catch(Exception e) {}
	}
	
	private void updateForces()
	{
		for(int i = 0; i < forceList.size(); i++)
			forceList.get(i).update();
	}
	
	private class Force
	{
		private long startTime, duration;
		private double x, y;
		private Player player;
		
		public Force(long duration, double x, double y, Player player)
		{
			this.duration = duration;
			this.x = x;
			this.y = y;
			this.player = player;
			startTime = System.currentTimeMillis();
			forceList.add(this);
		}
		
		public void update()
		{
			player.setXVel(player.getXVel() + x);
			player.setYVel(player.getYVel() + y);
			if(System.currentTimeMillis() > startTime + duration)
			{
				forceList.remove(this);
			}
		}
		
	}
}
