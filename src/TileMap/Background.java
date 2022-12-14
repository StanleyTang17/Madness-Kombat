package TileMap;

import Main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.imageio.*;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms)
	{
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(s));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		moveScale = ms;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update()
	{
		x += dx;
		y += dy;
		if(x + image.getWidth() < 0) x = 0;
		if(y - image.getHeight() > GamePanel.HEIGHT) y = 0;
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(image, (int)x, (int)y, null);
		if(x < 0)
		{
			g.drawImage(image, (int)x + image.getWidth(), (int)y, null);
		}
	}
}
