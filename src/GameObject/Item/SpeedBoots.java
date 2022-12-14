package GameObject.Item;

import java.util.Random;

import GameObject.Player.Player;
import GameObject.Player.Graphics.ParticleEffect.SpeedTrailParticle;
import TileMap.TileMap;

public class SpeedBoots extends Item {
	
	private int speedBuffIndex;
	
	public SpeedBoots(TileMap tileMap) {
		super("/speedBoots.png", tileMap);
		speedBuffIndex = -1;
		this.setSoundEffect(getClass().getResource("/wind.wav"));
	}

	public boolean condition(Player player) {
		if(!player.getBuffs().isEmpty())
			for(int i = 0; i < player.getBuffs().size(); i++)
				if(player.getBuffs().get(i).getClass() == SpeedBuff.class)
					speedBuffIndex = i;
		return true;
	}

	
	public void takeEffect(Player player) {
		int duration = new Random().nextInt(5000) + 5000;
		if(speedBuffIndex == -1)
			player.getBuffs().add(new SpeedBuff(player, duration));
		else
			player.getBuffs().set(speedBuffIndex, new SpeedBuff(player, duration));
		this.playSoundEffect();
		player.getGraphics().addParticleEffect(new SpeedTrailParticle(player, duration));
	}

}
