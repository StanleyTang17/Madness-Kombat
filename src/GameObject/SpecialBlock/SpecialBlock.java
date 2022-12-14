package GameObject.SpecialBlock;

import java.awt.Point;
import java.util.ArrayList;

import GameObject.GameObject;
import GameObject.Player.Player;
import TileMap.TileMap;

public abstract class SpecialBlock extends GameObject{
	
	private int row, col;
	
	public static ArrayList<SpecialBlock> SpecialBlockList = new ArrayList<SpecialBlock>();
	
	public abstract boolean condition(Player p, TileMap t);
	public abstract void trigger(Player p, TileMap t);
	
	public int getRow() { return row; }
	public int getCol() { return col; }
	public void setRow(int row) { this.row = row; }
	public void setCol(int col) { this.col = col; }
	
	public static void updateSpecialBlocks(TileMap t)
	{
		if(!SpecialBlockList.isEmpty())
			for(int a = 0; a < SpecialBlockList.size(); a++)
			{
				SpecialBlock block = SpecialBlockList.get(a);
				for(int b = 0; b < Player.PlayerList.size(); b++)
				{
					Player p = Player.PlayerList.get(b);
					if(block.condition(p, t))
					{
						block.trigger(p, t);
					}
				}
			}
	}
	
	public SpecialBlock(Point point)
	{
		row = point.x;
		col = point.y;
		setX(col * TileMap.getTileSize());
		setY(row * TileMap.getTileSize());
		SpecialBlockList.add(this);
	}
	
	public void init()
	{
		
	}
	
	public void update()
	{
		
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		
	}
}
