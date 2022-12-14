package GameObject.Item;

import java.awt.Graphics2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GameObject.GameObject;
import GameObject.Player.Player;
import Main.SoundEffect;
import TileMap.TileMap;

public abstract class Item extends GameObject{
	
	public static ArrayList<Item> ItemList = new ArrayList<Item>();
	
	private SoundEffect sound;
	
	public static void updateItems()
	{
		ArrayList<Player> players = Player.PlayerList;
		ArrayList<Item> items = ItemList;
		if(!items.isEmpty() && !players.isEmpty())
			for(int a = 0; a < items.size(); a++)
			{
				Item i = items.get(a);
				i.update();
				for(int b = 0; b < players.size(); b++)
				{
					Player p = players.get(b);
					if(i.collidesWith(p) && i.condition(p))
					{
						i.takeEffect(p);
						items.remove(a);
						break;
					}
				}
			}
	}
	
	public static void drawItems(java.awt.Graphics2D g)
	{
		if(!ItemList.isEmpty())
			for(int i = 0; i < ItemList.size(); i++)
				ItemList.get(i).draw(g);
	}
	
	public Item(String imageFileName, TileMap tileMap) {
		this.setTileMap(tileMap);
		try {
			this.setImage(ImageIO.read(this.getClass().getResource(imageFileName)));
			this.setSizeByImage();
			this.setHitBoxByImage();
			ItemList.add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract boolean condition(Player player);
	public abstract void takeEffect(Player player);
	
	public void setSoundEffect(URL url) { sound = new SoundEffect(url); }
	public void playSoundEffect() { sound.play(); }
	
	public void draw(Graphics2D g) {
		this.drawImage(g);
		
	}
	
	public void init() {
		

	}

	
	public void update() {
		this.terrainCollision();
		this.move();
	}
}
