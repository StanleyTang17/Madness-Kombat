package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import Main.GamePanel;
import TileMap.TileMap;

public class EditorState extends GameState implements MouseListener, MouseMotionListener, MouseWheelListener{
	
	private int selectRow = 0, selectCol = 0;
	private TileMap tileMap;
	private int tileId = 11;
	private String tilesFileName = "/tileset.png";
	private String mapFilePath;
	private int tileSize;
	private GameStateManager gsm;
	private int numRows, numCols;
	private int[][] keyMap = {
			{
				KeyEvent.VK_0,
				KeyEvent.VK_1,
				KeyEvent.VK_2,
				KeyEvent.VK_3,
				KeyEvent.VK_4,
				KeyEvent.VK_5,
				KeyEvent.VK_6,
				KeyEvent.VK_7,
				KeyEvent.VK_Q,
				KeyEvent.VK_W,
				KeyEvent.VK_E,
				KeyEvent.VK_R,
				KeyEvent.VK_T,
				KeyEvent.VK_Y,
				KeyEvent.VK_U,
				KeyEvent.VK_I,
				KeyEvent.VK_O,
				KeyEvent.VK_P,
				KeyEvent.VK_A,
				KeyEvent.VK_S,
				KeyEvent.VK_D,
				KeyEvent.VK_F,
			}, {
				0, 1, 2, 3, 4, 5, 6, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23
			}
	};
	private int curKeyIndex = 0;
	
	public EditorState(GameStateManager gsm) {
		this.gsm = gsm;
		
	}
	
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles(tilesFileName);
		mapFilePath = GamePanel.getLevelFilePath();
		tileMap.loadMap(GamePanel.getLevelFile());
		tileSize = TileMap.getTileSize();
		numRows = tileMap.getNumRows();
		numCols = tileMap.getNumCols();
		tileId = keyMap[1][curKeyIndex];
		gsm.gp.addMouseListener(this);
		gsm.gp.addMouseMotionListener(this);
		gsm.gp.addMouseWheelListener(this);
	}

	
	public void update() {
		
		
	}

	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		if(tileMap == null) return;
		tileMap.draw(g);
		g.setColor(Color.RED);
		int x = selectCol * tileSize;
		int y = selectRow * tileSize;
		try
		{
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g.drawImage(tileMap.getImageByTile(tileId), x, y, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		catch(Exception e)
		{
			
		}
		g.drawRect(x, y, tileSize, tileSize);
	}
	
	private void placeBlock()
	{
		tileMap.setTile(selectRow, selectCol, tileId);
	}
	
	private void deleteBlock()
	{
		tileMap.setTile(selectRow, selectCol, 0);
	}
	
	private void moveSelectGridByMouse(MouseEvent e)
	{
		selectRow = e.getY() / tileSize + 1;
		selectCol = e.getX() / tileSize;
	}
	
	private void changeIdByMouseWheel(MouseWheelEvent e)
	{
		int tick = e.getWheelRotation();
		if(curKeyIndex == 0 && tick == -1) return;
		if(curKeyIndex == keyMap[0].length - 1 && tick == 1) return;
		curKeyIndex += tick;
		tileId = keyMap[1][curKeyIndex];
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE) placeBlock();
		else if(k == KeyEvent.VK_BACK_SPACE) deleteBlock();
		else if(k == KeyEvent.VK_DOWN) 
		{
			if(selectRow < numRows - 1)selectRow++;
		}
		else if(k == KeyEvent.VK_UP)
		{
			if(selectRow > 0)selectRow--;
		}
		else if(k == KeyEvent.VK_LEFT)
		{
			if(selectCol > 0)selectCol--;
		}
		else if(k == KeyEvent.VK_RIGHT)
		{
			if(selectCol < numCols - 1)selectCol++;
		}
		else if(k == KeyEvent.VK_ESCAPE)
		{
			try {
				tileMap.writeMap(mapFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			gsm.gp.removeMouseListener(this);
			gsm.gp.removeMouseMotionListener(this);
			gsm.gp.removeMouseWheelListener(this);
			gsm.setState(GameStateManager.MENUSTATE);
		}
		else
		{
			for(int i = 0; i < keyMap[0].length; i++)
			{	
				if(k == keyMap[0][i])
				{
					tileId = keyMap[1][i];
					curKeyIndex = i;
					return;
				}
			}
		}
	}

	public void keyReleased(int k) {
		
	}

	
	public void mouseClicked(MouseEvent e) {
		
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
		
	}

	
	public void mouseExited(MouseEvent e) {
		
		
	}

	
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1)
		{
			placeBlock();
		}
		else if(e.getButton() == 3)
		{
			deleteBlock();
		}
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
		
	}

	
	public void mouseDragged(MouseEvent e) {
		moveSelectGridByMouse(e);
		placeBlock();
	}

	
	public void mouseMoved(MouseEvent e) {
		if(tileMap != null)
		moveSelectGridByMouse(e);
		
	}

	
	public void mouseWheelMoved(MouseWheelEvent e) {
		changeIdByMouseWheel(e);
	}
	
}
