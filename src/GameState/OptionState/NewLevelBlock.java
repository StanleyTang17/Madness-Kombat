package GameState.OptionState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import Main.GamePanel;

public class NewLevelBlock extends OptionBlock {

	public NewLevelBlock(OptionsState optionsState, Rectangle displayBox) {
		super(optionsState, displayBox);
		
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		String text = "create new map";
		g.drawString(text, this.getX() + 10, this.getY() + g.getFontMetrics().getHeight() + 5);
	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
		{
			createFile();
		}
		else
		{
			this.switchBlock(k);
		}
	}
	
	private void createFile()
	{
		String levelName = JOptionPane.showInputDialog("Enter level name");
		if(levelName != null && levelName != "")
		{
			String path = GamePanel.getRootFolderPath() + levelName + ".map";
			try {
				TileMap.TileMap.writeEmptyMap(path);
				System.out.println(levelName + ".map created");
				System.out.println("File path: " + path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
