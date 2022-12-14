package TileMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TileMap {
	
	private int[][] map;
	private static int tileSize;
	private int numRows;
	private int numCols;
	
	private BufferedImage tileset;
	private Tile[][] tiles;
	private BufferedWriter writer;
	
	private ArrayList<Point> spikeCoords;
	private ArrayList<Point> mysteryBlockCoords;
	private ArrayList<Point> playerSpawnPoints;
	private ArrayList<Point> firearmSpawnPoints;
	private ArrayList<Point> launchBlockCoords;
	
	public static void writeEmptyMap(String s) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(s)));
		writer.write("45");
		writer.newLine();
		writer.write("24");
		writer.newLine();
		String emptyRow = "";
		for(int a = 0; a < 45; a++)
		{
			emptyRow += "0  ";
		}
		for(int b = 0; b < 24; b++)
		{
			writer.write(emptyRow);
			writer.newLine();
		}
		writer.close();
	}
	
	public TileMap(int TileSize)
	{
		tileSize = TileSize;
		spikeCoords = new ArrayList<Point>();
		playerSpawnPoints = new ArrayList<Point>();
		firearmSpawnPoints = new ArrayList<Point>();
		mysteryBlockCoords = new ArrayList<Point>();
		launchBlockCoords = new ArrayList<Point>();
	}
	
	public void loadTiles(String s)
	{
		try
		{
			tileset = ImageIO.read(getClass().getResource(s));
			numCols = tileset.getWidth() / tileSize;
			numRows = tileset.getHeight() / tileSize;
			tiles = new Tile[numRows][numCols];
			
			BufferedImage subimage;
			for(int row = 0; row < numRows; row++)
			{
				for(int col = 0; col < numCols; col++)
				{
					subimage = tileset.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
					tiles[row][col] = new Tile(subimage, Tile.BLOCKED);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadMap(File mapFile)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(mapFile));
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			for(int row = 0; row < numRows; row++)
			{
				String line = br.readLine();
				String[] tokens = line.split("\\s+");
				for(int col = 0; col < numCols; col++)
				{
					int id = Integer.parseInt(tokens[col]);
					map[row][col] = id;
					if(id == 2)
					{
						playerSpawnPoints.add(new Point(row - 1, col));
					}
					else if(id / 10 == 2)
					{
						spikeCoords.add(new Point(row, col));
					}
					else if(id == 5)
					{
						firearmSpawnPoints.add(new Point(row - 1, col));
					}
					else if(id == 6)
					{
						mysteryBlockCoords.add(new Point(row, col));
					}
					else if(id == 8)
					{
						launchBlockCoords.add(new Point(row, col));
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			/*
			e.printStackTrace();
			File file = new File(s);
			try {
				file.createNewFile();
				writeEmptyMap(s);
				System.out.println("File could not be found so a new file " + s + " is created\nPlease refresh the project folder");
				loadMap(s);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			*/
			
		}
	}
	
	public void writeMap(String s) throws IOException
	{
		writer = new BufferedWriter(new FileWriter(new File(s)));
		writer.write(Integer.toString(numCols));
		writer.newLine();
		writer.write(Integer.toString(numRows));
		writer.newLine();
		for(int a = 0; a < numRows; a++)
		{
			String row = "";
			for(int b = 0; b < numCols; b++)
			{
				int n = map[a][b];
				row += Integer.toString(n);
				if(n >= 10)
				{
					row += " ";
				}
				else
				{
					row += "  ";
				}
			}
			writer.write(row);
			writer.newLine();
		}
		writer.close();
	}
	
	public static int getTileSize() { return tileSize; }
	public ArrayList<Point> getSpikeCoords() { return spikeCoords; }
	public ArrayList<Point> getMysteryBlockCoords() { return mysteryBlockCoords; }
	public ArrayList<Point> getPlayerSpawnPoints() { return playerSpawnPoints; }
	public ArrayList<Point> getFirearmSpawnPoints() { return firearmSpawnPoints; }
	public ArrayList<Point> getLaunchBlockCoords() { return launchBlockCoords; }
	public int getType(int row, int col) { 
		try
		{
			return map[row][col]; 
		}catch(Exception e)
		{
			return 0;
		}
	}
	public void setType(int row, int col, int type) { map[row][col] = type; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public BufferedImage getImageByTile(int tileId)
	{
		int row = tileId / 10;
		int col = tileId % 10;
		return tiles[row][col].getImage();
	}
	
	public void setTile(int row, int col, int type)
	{
		map[row][col] = type;
	}
	
	public void draw(Graphics2D g)
	{
		if(map != null)
		for(int row = 0; row < numRows; row++)
		{
			if(row > numRows) break;
			for(int col = 0; col < numCols; col++)
			{
				if(col >= numCols) break;
				if(map[row][col] == 0) continue;
				int rc = map[row][col];
				int r = rc / 10;
				int c = rc % 10;
				int x = col * tileSize;
				int y = row * tileSize;
				g.drawImage(tiles[r][c].getImage(), x, y, null);
			}
		}
	}
}
