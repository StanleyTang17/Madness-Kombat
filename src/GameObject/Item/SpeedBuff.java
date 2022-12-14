package GameObject.Item;

import GameObject.Player.Player;

public class SpeedBuff extends Buff {
	
	private double speedBuff;
	
	public SpeedBuff(Player player, int duration) {
		super(player);
		this.setDuration(duration);
		speedBuff = 3;
		player.setSpeed(player.getSpeed() + speedBuff);
	}

	
	public void update() {
		this.checkFinished();

	}

	
	public void undoBuff() {
		this.getPlayer().setSpeed(this.getPlayer().getSpeed() - speedBuff);
		
	}
	
	
}
