package GameObject.Player.Graphics.GraphicComponent;

import GameObject.Player.Player;

public abstract class GraphicComponent {
	
	private Player player;
	
	
	public GraphicComponent(Player player) {
		this.player = player;
	}
	
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	
	public void setPlayer(Player player) { this.player = player; }
	public Player getPlayer() { return player; }
	
	
}
