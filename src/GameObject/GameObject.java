package GameObject;

import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;

import TileMap.TileMap;

//import java.awt.BasicStroke;
//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class GameObject {
	
	private double x, y;
	private int width, height;
	private BufferedImage image;
	private Rectangle hitbox;
	private TileMap tileMap;
	private double xVel, yVel;
	private boolean air;
	private boolean hitLeft, hitRight;
	private int direction;
	
	public final double gravity = -0.5;
	
	public static final int FACING_LEFT = -1;
	public static final int FACING_RIGHT = 1;
	public static final int BOUND_X = 1350;
	public static final int BOUND_Y = 770;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	
	public double getX() { return x; }
	public double getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public BufferedImage getImage() { return image; }
	public Rectangle getHitbox() { return hitbox; }
	public double getXVel() { return xVel; }
	public double getYVel() { return yVel; }
	public boolean inAir() { return air; }
	public TileMap getTileMap() { return tileMap; }
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setWidth(int w) { this.width = w; }
	public void setHeight(int h) { this.height = h; }
	public void setSizeByImage() { this.width = image.getWidth(); this.height = image.getHeight(); }
	public void setImage(BufferedImage i) { this.image = i; }
	public void setHitbox(Rectangle r) { this.hitbox = r; }
	public void setHitBoxByImage() { this.hitbox = new Rectangle((int)getX(), (int)getY(), image.getWidth(), image.getHeight()); }
	public void setHitBoxBySize() { this.hitbox = new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight()); }
	public void setXVel(double vel) { xVel = vel; }
	public void setYVel(double vel) { yVel = vel; }
	public void setInAir(boolean b) { air = b; }
	public void setTileMap(TileMap m) { tileMap = m; }
	public int getDirection() { return direction; }
	public void setDirection(int dir) { direction = dir; }
	
	public void spawnRandom(ArrayList<Point> spawnPoints)
	{
		int index = new Random().nextInt(spawnPoints.size());
		spawn(spawnPoints.get(index));
	}
	
	public void spawn(Point spawnPoint)
	{
		Point p = spawnPoint;
		setX(p.y * TileMap.getTileSize());
		setY(p.x * TileMap.getTileSize());
	}
	
	public void updateHitbox()
	{
		if(hitbox != null)
		{
			hitbox.x = (int) getX();
			hitbox.y = (int) getY();
		}
		
	}
	
	public void drawImage(Graphics2D g)
	{
		int dir = getDirection();
		if(dir == FACING_LEFT)
		{
			g.drawImage(getImage(), (int)getX() + getWidth(), (int)getY(), getDirection() * getWidth(), getHeight(), null);
		}
		else
		{
			g.drawImage(getImage(), (int)getX(), (int)getY(), null);
		}
	}
	
	public void move()
	{
		if((xVel > 0 && !hitRight) || (xVel < 0 && !hitLeft))
		{
			this.x += xVel;
		}
		
		if(inAir())
		{
			yVel -= gravity;
		}
		
		this.y += yVel;		
	}
	
	public boolean collidesWith(GameObject g)
	{
		if(g != null) return hitbox.intersects(g.getHitbox()); return false;
	}
	
	/*
	private void drawCollide(int row, int col, double collideX, double collideY, Graphics2D g)
	{
		g.setStroke(new java.awt.BasicStroke(3));
		g.setColor(java.awt.Color.red);
		g.drawRect(col * 30, row * 30, 30, 30);
		g.drawOval((int)collideX, (int)collideY, 3, 3);
	}
	*/
	
	private void checkHitTop(TileMap m)
	{	
		int ts = TileMap.getTileSize();
		double collideX = x;
		double collideY = y + yVel;
		double rightMostX = x + width - ts;
		int collideCol = (int) collideX / ts - 1;
		int collideRow = (int) collideY / ts;
		
		if(collideY <= 0) return;
		
		do
		{
			collideCol++;
			int tileId = m.getType(collideRow, collideCol);
			if(tileId / 10 == 1)
			{
				y = collideRow * ts + ts;
				yVel = 0;
			}
		}
		while(collideCol * ts < rightMostX);
	}
	
	private void checkHitGround(TileMap m)
	{
		int ts = TileMap.getTileSize();
		double collideX = x;
		double collideY = y + height + yVel;
		double rightMostX = x + width - ts;
		int collideCol = (int) collideX / ts - 1;
		int collideRow = (int) collideY / ts;
		
		do
		{
			collideCol++;
			if(collideRow * ts < 0) continue;
			int tileId = m.getType(collideRow, collideCol);
			if(tileId / 10 == 1)
			{
				y = collideRow * ts - height;
				air = false;
				yVel = 0;
				break;
			}
			else
			{
				air = true;
			}
		}
		while(collideCol * ts < rightMostX);
	}
	
	private void checkHitLeft(TileMap m)
	{	
		int ts = TileMap.getTileSize();
		double collideX = x + xVel;
		double collideY = y;
		double bottomY = y + height - ts;
		int collideCol = (int) collideX / ts;
		int collideRow = (int) collideY / ts - 1;
		
		if(collideX <= 0)
		{
			x = 0;
			hitLeft = true;
			return;
		}
		
		do
		{
			collideRow++;
			if(collideRow * ts < 0) continue;
			int tileId = m.getType(collideRow, collideCol);
			if(tileId / 10 == 1)
			{
				x = collideCol * ts + ts;
				hitLeft = true;
				xVel = 0;
				break;
			}
			else
			{
				hitLeft = false;
			}
			
		}while(collideRow * ts < bottomY);
	}
	
	private void checkHitRight(TileMap m)
	{	
		int ts = TileMap.getTileSize();
		double collideX = x + width + xVel;
		double collideY = y;
		double bottomY = y + height - ts;
		int collideCol = (int) collideX / ts;
		int collideRow = (int) collideY / ts - 1;
		
		if(collideX >= BOUND_X)
		{
			x = BOUND_X - width;
			hitRight = true;
			return;
		}
		
		do
		{
			collideRow++;
			if(collideRow * ts < 0) continue;
			int tileId = m.getType(collideRow, collideCol);
			if(tileId / 10 == 1)
			{
				x = collideCol * ts - width;
				hitRight = true;
				xVel = 0;
				break;
			}
			else
			{
				hitRight = false;
			}
			
		}while(collideRow * ts < bottomY);
	}
	
	public void terrainCollision()
	{
		if(yVel < 0)
		{
			checkHitTop(tileMap);
		}
		else
		{
			checkHitGround(tileMap);
		}
		if(xVel > 0)
		{
			checkHitRight(tileMap);
		}
		else if(xVel < 0)
		{
			checkHitLeft(tileMap);
		}
	}
	
	public boolean checkOutOfBound()
	{
		double X = getX() + xVel;
		double Y = getY() + yVel;
		if(X < 0 || X + width > BOUND_X) return true;
		if(Y < -height || Y + height > BOUND_Y) return true;		
		return false;
	}
}
