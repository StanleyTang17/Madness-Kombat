package GameObject.Item;

import GameObject.Player.Player;
import TileMap.TileMap;

public class ClothArmor extends Item {

	public ClothArmor(TileMap tileMap) {
		super("/clothArmor.png", tileMap);
		
	}

	
	public boolean condition(Player player) {
		if(player.getArmor() < 100) return true;
		return false;
	}

	
	public void takeEffect(Player player) {
		player.setArmor(player.getArmor() + 30);
		if(player.getArmor() > 100) player.setArmor(100);
	}

}
