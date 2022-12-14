package GameState.OptionState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageBlock extends OptionBlock {
	
	public static ArrayList<BufferedImage> PlayerImages = new ArrayList<BufferedImage>();
	
	private int imageIndex;
	private BufferedImage image;
	
	public static void loadImages(String[] imageNames) throws IOException
	{
		for(String name : imageNames)
		{
			BufferedImage image = ImageIO.read(ImageBlock.class.getResource(name));
			PlayerImages.add(image);
		}
		
	}
	
	public ImageBlock(OptionsState optionsState, Rectangle displayBox, int imageIndex) {
		super(optionsState, displayBox);
		this.imageIndex = imageIndex;
		this.image = PlayerImages.get(imageIndex);
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		int x = this.getX() + this.getWidth() / 2 - image.getWidth() / 2;
		int y = this.getY() + this.getHeight() / 2 - image.getHeight() / 2;
		g.drawImage(image, x, y, null);
	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
		{
			imageIndex++;
			if(imageIndex == PlayerImages.size()) 
				imageIndex = 0;
			image = PlayerImages.get(imageIndex);
			
		}
		else
			this.switchBlock(k);
	}
	
	public BufferedImage getImage() { return this.image; }
	public int getImageIndex() { return this.imageIndex;  }
}
