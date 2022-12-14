package GameObject.SpecialBlock;


import java.awt.Point;

import GameObject.Player.Player;
import Main.SoundEffect;
import TileMap.TileMap;

public class Spike extends SpecialBlock{
	
	public static final int DAMAGE = 20;
	
	private SoundEffect sound;
	
	public Spike(Point point) {
		super(point);
		this.setWidth(30);
		this.setHeight(30);
		this.setHitBoxBySize();
		sound = new SoundEffect(getClass().getResource("/spike.wav"));
	}

	public boolean condition(Player p, TileMap t) {
		if(p.collidesWith(this) && !p.isInvincible()) return true;
		return false;
	}

	public void trigger(Player p, TileMap t) {
		p.hitSpike();
		sound.play();
	}
}
