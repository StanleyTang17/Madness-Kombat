package GameObject.Weapon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import GameObject.Player.Player;
import TileMap.TileMap;

public class TNT extends Weapon {
	
	private BufferedImage preIgnitedImg;
	private BufferedImage ignitedImg;
	private boolean ignited = false;
	private long explodeTime;
	private long blinkTime;
	
	public TNT(TileMap tileMap) {
		super(-1, 0, 0, 0, Weapon.SEMI_AUTOMATIC, 0, "/tnt.png", "/tnt.wav", tileMap);
		preIgnitedImg = this.getImage();
		try
		{
			ignitedImg = ImageIO.read(getClass().getResource("/tntIgnited.png"));
		} catch (Exception e) {e.printStackTrace();}
	}

	
	public void moveWithPlayer(Player p) {
		this.setX(p.getX() + p.getWidth() / 2 - this.getWidth() / 2);
		this.setY(p.getY());
	}

	@Override
	public void update() 
	{
		this.defaultUpdate();
		if(ignited && System.currentTimeMillis() > explodeTime)
		{
			if(this.isEquipped())
			{
				double prevHp = this.getPlayer().getHitpoints();
				this.getPlayer().setHitpoints(100);
				new Explosion(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, 45, 300);
				this.getPlayer().setHitpoints(prevHp);
			}
			else
			{
				new Explosion(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, 45, 300);
			}
			this.removeThis();
		}
		if(!ignited && !this.isEquipped())
		{
			try
			{
				for(Bullet b : Bullet.BulletList)
					if(b.collidesWith(this))
					{
						b.removeThis();
						new Explosion(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, 45, 300);
						this.removeThis();
					}
			} catch(Exception e) {}
		}
	}
	
	public void shootBullet() {
		if(ignited) return;
		ignited = true;
		explodeTime = System.currentTimeMillis() + 4120;
		blinkTime = System.currentTimeMillis();
		this.playSoundEffect();
	}
	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		if(ignited)
		{
			if(System.currentTimeMillis() <= blinkTime)
			{
				this.setImage(preIgnitedImg);
			}
			else
			{
				this.setImage(ignitedImg);
			}
			if(System.currentTimeMillis() >= blinkTime + 200)
			{
				blinkTime = System.currentTimeMillis() + 200;
			}
		}
		this.drawImage(g);

	}

}
