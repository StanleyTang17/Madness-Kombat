package GameObject.Weapon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import GameObject.Player.Player;
import TileMap.TileMap;

public class RPG extends Weapon {
	
	private BufferedImage headImg;
	
	public RPG(TileMap tileMap) {
		super(1, 5, 16, 7.5, Weapon.SEMI_AUTOMATIC, Weapon.RPG, "/RPG_body.png", "/RPG.wav", tileMap);
		try {
			headImg = ImageIO.read(getClass().getResource("/RPG_head.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX() - 10);
		else
			this.setX(p.getX() - 16);
		this.setY(p.getY() + 7);

	}

	
	public void shootBullet() {
		if(this.getAmmoCount() != 0)
		this.shootSingleBullet(new RPG_Bullet(this.getTileMap(), this.getPlayer().getTeam()));
		
	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);
		if(headImg != null)
		if(this.getAmmoCount() == 1)
		{
			if(this.getDirection() == FACING_LEFT)
				g.drawImage(headImg, (int)this.getX(), (int)this.getY() + 1, this.getDirection() * headImg.getWidth(), headImg.getHeight(), null);
			else
				g.drawImage(headImg, (int)this.getX() + this.getWidth(), (int)this.getY() + 1, null);
		}

	}
	
}
