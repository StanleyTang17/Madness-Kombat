package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class M4A1 extends Weapon {
	
	private long burstTime;
	private long bursts;
	private boolean shooting;
	
	public M4A1(TileMap tileMap) {
		super(30, 7, 13, 2.5, Weapon.FULLY_AUTOMATIC, Weapon.RIFLE, "/M4A1.png", "/M4A1.wav", tileMap);
		this.setFireRate(450);
		this.mute();
		shooting = false;
		bursts = 0;
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX() - 10);
		else
			this.setX(p.getX() - 30);
		this.setY(p.getY() + 12);
		if(shooting)
		{
			if(System.currentTimeMillis() >= burstTime)
			{
				shoot();
			}
			if(bursts == 3) shooting = false;
		}
		else
		{
			bursts = 0;
		}
		
	}
	
	private void shoot()
	{
		burstTime = System.currentTimeMillis() + 50;
		this.shootSingleBullet(new RifleBullet(this.getTileMap(), this.getPlayer().getTeam()));
		bursts++;
	}
	
	public void shootBullet() {
		if(this.getAmmoCount() == 0) return;
		shooting = true;
		shoot();
		this.playSoundEffect();
	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		if(this.isEquipped())
		{
			this.setY(getY() - 5);
			this.drawImage(g);
			this.setY(getY() + 5);
		}
		else
			this.drawImage(g);

	}

}
