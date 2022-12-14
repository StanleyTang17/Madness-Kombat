package GameObject.Item;

import GameObject.Player.Player;
import TileMap.TileMap;

public class AmmoBox extends Item {

	public AmmoBox(TileMap tileMap) {
		super("/ammoBox.png", tileMap);
		this.setSoundEffect(getClass().getResource("/refill.wav"));
	}

	
	public boolean condition(Player player) {
		if(player.hasWeapon() && player.getWeapon().getAmmoCount() != player.getWeapon().getMaxAmmoCount())
		{
			return true;
		}
		return false;
	}

	
	public void takeEffect(Player player) {
		player.getWeapon().refill();
		this.playSoundEffect();
	}

}
