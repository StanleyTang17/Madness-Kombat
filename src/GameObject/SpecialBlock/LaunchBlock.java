package GameObject.SpecialBlock;

import java.awt.Point;

import GameObject.Player.Player;
import TileMap.TileMap;

public class LaunchBlock extends SpecialBlock {

	public static final double launchSpeed = -7;
	public static final int duration = 20;
	
	public LaunchBlock(Point point) {
		super(point);
		this.setWidth(30);
		this.setHeight(30);
		this.setHitBoxBySize();
	}

	
	public boolean condition(Player p, TileMap t) {
		if(p.collidesWith(this))
		{
			return true;
		}
		return false;
	}

	
	public void trigger(Player p, TileMap t) {
		p.setY(this.getY() - p.getHeight());
		p.setYVel(0);
		p.setDoubleJump(false);
		p.addExternalForce(duration, 0, launchSpeed);
		p.setInAir(true);
	}

}
