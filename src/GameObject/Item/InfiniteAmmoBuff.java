package GameObject.Item;

import GameObject.Player.Player;
import GameObject.Weapon.Weapon;

public class InfiniteAmmoBuff extends Buff {

	public InfiniteAmmoBuff(Player player) {
		super(player);
		this.setDuration(7000);
	}

	
	public void update() {
		Weapon w = this.getPlayer().getWeapon();
		if(w != null && w.getAmmoCount() != w.getMaxAmmoCount())
			w.setAmmoCount(w.getMaxAmmoCount());
		this.checkFinished();

	}

	
	public void undoBuff() {
		

	}

}
