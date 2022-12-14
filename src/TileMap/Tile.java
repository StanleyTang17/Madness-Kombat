package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
	
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	private BufferedImage image;
	private int type;
	
	public Tile(BufferedImage image, int type)
	{
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }
}
