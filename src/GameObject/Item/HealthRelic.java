package GameObject.Item;

import java.util.Random;

import GameObject.Player.Player;
import GameObject.Player.Graphics.ParticleEffect.*;
import TileMap.TileMap;

public class HealthRelic extends Item {
	
	private int healing;
	private int max = 50;
	private int min = 20;
	
	public HealthRelic(TileMap tileMap) {
		super("/relic.png", tileMap);
		healing = new Random().nextInt(max - min) + min;
		this.setSoundEffect(getClass().getResource("/healing.wav"));
	}
	
	public boolean condition(Player player) 
	{
		if(player.getHitpoints() < 100) return true;
		return false;
	}
	
	public void takeEffect(Player player) {
		player.setHitpoints(player.getHitpoints() + healing);
		if(player.getHitpoints() > 100) player.setHitpoints(100);
		player.getGraphics().addParticleEffect(new HealingParticle(player));
		this.playSoundEffect();
	}

	

}
