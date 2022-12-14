package GameObject.Player.Graphics.GraphicComponent;

import java.awt.Graphics2D;
import GameObject.Player.Player;

public class StaticGraphic extends GraphicComponent {

	public StaticGraphic(Player player) {
		super(player);
		
	}

	
	public void update() {

	}

	
	public void draw(Graphics2D g) {
		this.getPlayer().drawImage(g);
		this.getPlayer().drawName(g);
		
	}

}
