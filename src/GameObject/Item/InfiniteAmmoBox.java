package GameObject.Item;

import GameObject.Player.Player;
import TileMap.TileMap;

public class InfiniteAmmoBox extends Item {

	public InfiniteAmmoBox(TileMap tileMap) {
		super("/infiniteAmmoBox.png", tileMap);
		
	}

	
	public boolean condition(Player player) {
		return player.hasWeapon() && player.getWeapon().getMaxAmmoCount() != -1;
	}

	
	public void takeEffect(Player player) {
		player.getBuffs().add(new InfiniteAmmoBuff(player));

	}

}
