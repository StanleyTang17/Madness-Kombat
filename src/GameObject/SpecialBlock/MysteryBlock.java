package GameObject.SpecialBlock;

import java.awt.Point;
import GameObject.Player.Player;
import GameObject.Weapon.Weapon;
import GameObject.Library;
import TileMap.TileMap;

public class MysteryBlock extends SpecialBlock{
	
	private int weaponIndex;
	private boolean empty;
	
	public MysteryBlock(Point point, int weaponIndex) {
		super(point);
		this.setWidth(30);
		this.setHeight(30);
		this.setHitBoxBySize();
		this.weaponIndex = weaponIndex;
		empty = false;
	}
	
	public boolean condition(Player p, TileMap t) {
		if(!empty && p.collidesWith(this) && p.isDownPressed()) return true;
		return false;
	}

	
	public void trigger(Player p, TileMap t) {
		Weapon weapon = new Library().getNewFirearm(weaponIndex, t);
		if(p.hasWeapon())
		{
			int ts = TileMap.getTileSize();
			weapon.spawn(new Point((int)(getY() / ts), (int)(getX() / ts)));
		}
		else
		{
			p.equipFirearm(weapon);
		}
		t.setTile(getRow(), getCol(), 7);
		empty = true;
		
	}
	
	public int getWeaponId() { return weaponIndex; }
	
}
