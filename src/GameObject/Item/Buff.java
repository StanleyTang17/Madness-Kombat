package GameObject.Item;

import GameObject.Player.Player;

public abstract class Buff {
	
	private Player player;
	private int duration;
	private long startTime;
	
	public Buff(Player player) {
		this.player = player;
		startTime = System.currentTimeMillis();
		
	}
	
	public Player getPlayer() { return player; }
	public int getDuration() { return duration; }
	public void setDuration(int duration) { this.duration = duration; }
	
	public abstract void update();
	public abstract void undoBuff();
	
	public void checkFinished()
	{
		if(System.currentTimeMillis() > startTime + duration)
		{
			undoBuff();
			player.getBuffs().remove(this);
		}
	}
}
