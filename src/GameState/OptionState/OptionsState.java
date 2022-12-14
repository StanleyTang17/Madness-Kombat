package GameState.OptionState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import GameObject.Library;
import GameObject.Player.PlayerKeyset;
import GameObject.Player.Team;
import GameState.GameState;
import GameState.GameStateManager;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import Main.GamePanel;
import Main.MusicLoop;
import Main.SoundEffect;

public class OptionsState extends GameState{
	
	private PlayerKeyset[] keysets;
	private String levelName, filePath;
	private String[] playerNames;
	private ArrayList<OptionBlock> blocks;
	private int currentChoice = 0;
	private int playerSet[] = new int[4];
	private Library library;
	private Team[] teamSet;
	private int[] playerImageIndices;
	private String[] imageNames;
	
	private FolderSelectorBlock filePathBlock;
	private GameModeBlock gameModeBlock;
	private FileSelectorBlock levelSelectorBlock;
	private NewLevelBlock newLevelOptionBlock;
	private SpawnOptionBlock spawnOptionBlock;
	private BoolBlock soundBoolBlock;
	private BoolBlock musicBoolBlock;
	private File level;
	
	public OptionsState(GameStateManager gsm) {
		this.gsm = gsm;
		keysets = gsm.getKeyset();
		playerNames = new String[4];
		teamSet = new Team[4];
		library = new Library();
		playerImageIndices = new int[4];
		try {
			loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void init() {
		keysets = gsm.getKeyset();
		try {
			loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initOptionBlocks();	
	}

	
	public void update() {
		gsm.bg.update();
	}

	
	public void draw(Graphics2D g) {
		gsm.bg.draw(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		FontMetrics fm = g.getFontMetrics();
		g.drawString("Game Data Folder", 25, 84);
		g.drawString("Game Mode", 25, 144);
		g.drawString("Map File", 25, 204);
		String s = "Sound Effects: ";
		g.drawString(s, 355 - fm.stringWidth(s), 690);
		s = "Music: ";
		g.drawString(s, 1035 - fm.stringWidth(s), 690);
		for(int i = 0; i < blocks.size(); i++)
		{
			blocks.get(i).draw(g);
		}
	}

	
	public void keyPressed(int k) {
		blocks.get(currentChoice).keyPressed(k);
		if(k == KeyEvent.VK_ESCAPE)
		{
			exit();
			return;
		}
		
	}

	
	public void keyReleased(int k) {
		
		
	}
	
	public void exit()
	{
		try {
			saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gsm.setState(GameStateManager.MENUSTATE);
	}
	
	public void setOption(int index)
	{
		currentChoice = index;
		blocks.get(index).setSelected(true);
	}
	
	public void setOption(OptionBlock block)
	{
		int index = blocks.indexOf(block);
		setOption(index);
	}
	
	private void loadConfig() throws IOException
	{
		String configPath = GamePanel.getRootFilePath() + "/configs.txt"; // Unix file path by default
		if (System.getProperty("os.name").contains("Windows")) {
			configPath = GamePanel.getRootFilePath() + "\\configs.txt";
		}

		BufferedReader reader = new BufferedReader(new FileReader(configPath));
		filePath = reader.readLine();
		GamePanel.setRootFilePath(filePath);
		levelName = reader.readLine();
		GamePanel.setLevelName(levelName);
		LevelState.setGameMode(GameMode.valueOf(reader.readLine()));
		
		for(int a = 0; a < 4; a++)
		{
			int[] k = new int[5];
			playerNames[a] = reader.readLine();
			playerSet[a] = Integer.parseInt(reader.readLine());
			teamSet[a] = Team.valueOf(reader.readLine());
			playerImageIndices[a] = Integer.parseInt(reader.readLine());
			for(int b = 0; b < k.length; b++)
			{
				k[b] = Integer.parseInt(reader.readLine());
			}
			keysets[a].setAllKeys(k);
		}
		gsm.setPlayerNames(playerNames);
		ArrayList<Integer> preset = new ArrayList<Integer>();
		for(int a = 0; a < 4; a++)
		{
			if(playerSet[a] == 1)
				preset.add(a + 1);
		}
		gsm.setPlayerPreset(preset);
		level = new File(GamePanel.getLevelFilePath());
		reader.readLine();
		preset = new ArrayList<Integer>();
		for(int a = 0; a < library.getWeaponCount(); a++)
		{
			if(Integer.parseInt(reader.readLine()) == 1)
				preset.add(a);
		}
		library.setWeaponPreset(preset);
		reader.readLine();
		preset = new ArrayList<Integer>();
		for(int a = 0; a < library.getItemCount(); a++)
		{
			if(Integer.parseInt(reader.readLine()) == 1)
				preset.add(a);
		}
		library.setItemPreset(preset);
		LevelState ls = (LevelState) this.gsm.getState(GameStateManager.LEVELSTATE);
		ls.setLibrary(library);
		ls.setTeamSet(teamSet);
		reader.readLine();
		int numImages = Integer.parseInt(reader.readLine());
		imageNames = new String[numImages];
		for(int i = 0; i < numImages; i++)
		{
			imageNames[i] = reader.readLine();
		}
		if(ImageBlock.PlayerImages.size() != numImages)
			ImageBlock.loadImages(imageNames);
		BufferedImage[] images = new BufferedImage[4];
		for(int i = 0; i < playerImageIndices.length; i++)
		{
			images[i] = ImageBlock.PlayerImages.get(playerImageIndices[i]);
		}
		ls.setPlayerImages(images);
		reader.readLine();
		if(reader.readLine().equals("-1")) SoundEffect.MUTE = true;
		else SoundEffect.MUTE = false;
		if(reader.readLine().equals("-1")) MusicLoop.MUTE = true;
		else MusicLoop.MUTE = false;
		reader.close();
	}
	
	public void saveConfig() throws IOException
	{
		LevelState ls = (LevelState) this.gsm.getState(GameStateManager.LEVELSTATE);
		ls.setLibrary(library);
		ls.setTeamSet(teamSet);
		filePath = filePathBlock.getText();
		levelName = levelSelectorBlock.getText();
		level = levelSelectorBlock.file;
		GamePanel.setRootFilePath(filePath);
		GamePanel.setLevelName(levelName);
		GamePanel.setLevelFile(level);
		
		SoundEffect.MUTE = soundBoolBlock.getBool() == 1 ? false : true;
		MusicLoop.MUTE = musicBoolBlock.getBool() == 1 ? false : true;
		for(int a = 0; a < 4; a++)
		{
			int[] keys = new int[5];
			TextBlock textBlock = (TextBlock) blocks.get(a * 9 + 5);
			BoolBlock boolBlock = (BoolBlock) blocks.get(a * 9 + 6);
			TeamOptionBlock teamBlock = (TeamOptionBlock) blocks.get(a * 9 + 12);
			ImageBlock imageBlock = (ImageBlock) blocks.get(a * 9 + 13);
			playerSet[a] = boolBlock.getBool();
			playerNames[a] = textBlock.getText();
			teamSet[a] = teamBlock.getTeam();
			playerImageIndices[a] = imageBlock.getImageIndex();
			for(int b = 0; b < keys.length; b++)
			{
				KeyBlock block = (KeyBlock) blocks.get(a * 9 + 7 + b);
				keys[b] = block.getKey();
			}
			keysets[a].setAllKeys(keys);
		}
		BufferedImage[] images = new BufferedImage[4];
		for(int i = 0; i < playerImageIndices.length; i++)
		{
			images[i] = ImageBlock.PlayerImages.get(playerImageIndices[i]);
		}
		ls.setPlayerImages(images);
		ArrayList<Integer> preset = new ArrayList<Integer>();
		for(int a = 0; a < 4; a++)
		{
			if(playerSet[a] == 1)
				preset.add(a + 1);
		}
		gsm.setPlayerPreset(preset);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(GamePanel.getRootFilePath() + "\\configs.txt")));
		writer.write(filePath);
		writer.newLine();
		writer.write(levelName);
		writer.newLine();
		writer.write(LevelState.getGameMode().name());
		writer.newLine();
		for(int a = 0; a < 4; a++)
		{
			writer.write(playerNames[a]);
			writer.newLine();
			writer.write(Integer.toString(playerSet[a]));
			writer.newLine();
			writer.write(teamSet[a].name());
			writer.newLine();
			writer.write(Integer.toString(playerImageIndices[a]));
			writer.newLine();
			for(int b = 0; b < 5; b++)
			{
				writer.write(Integer.toString(keysets[a].getKey(b)));
				writer.newLine();
			}
		}
		writer.write("");
		writer.newLine();
		for(int i = 0; i < library.getWeaponCount(); i++)
		{
			if(library.getWeaponPreset().contains(i))
				writer.write("1");
			else
				writer.write("0");
			writer.newLine();
		}
		writer.write("");
		writer.newLine();
		for(int i = 0; i < library.getItemCount(); i++)
		{
			if(library.getItemPreset().contains(i))
				writer.write("1");
			else
				writer.write("0");
			writer.newLine();
		}
		writer.write("");
		writer.newLine();
		writer.write(Integer.toString(imageNames.length));
		writer.newLine();
		for(int i = 0; i < imageNames.length; i++)
		{
			writer.write(imageNames[i]);
			writer.newLine();
		}
		writer.write("");
		writer.newLine();
		writer.write(SoundEffect.MUTE ? "-1" : "1");
		writer.newLine();
		writer.write(MusicLoop.MUTE ? "-1" : "1");
		writer.close();
	}
	
	private void initOptionBlocks()
	{
		blocks = new ArrayList<OptionBlock>();
		filePathBlock = new FolderSelectorBlock(this, new Rectangle(250, 50, 1086, 50), filePath);
		gameModeBlock = new GameModeBlock(this, new Rectangle(250, 110, 1086, 50));
		levelSelectorBlock = new FileSelectorBlock(this, new Rectangle(250, 170, 406, 50), levelName);
		newLevelOptionBlock = new NewLevelBlock(this, new Rectangle(693, 170, 300, 50));
		spawnOptionBlock = new SpawnOptionBlock(this, new Rectangle(1033, 170, 300, 50));
		soundBoolBlock = new BoolBlock(this, new Rectangle(355, 660, 300, 50), SoundEffect.MUTE ? -1 : 1);
		musicBoolBlock = new BoolBlock(this, new Rectangle(1035, 660, 300, 50), MusicLoop.MUTE ? -1 : 1);
		filePathBlock.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, gameModeBlock);
		gameModeBlock.setNearbyBlock(OptionBlock.UPPER_BLOCK, filePathBlock);
		gameModeBlock.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, levelSelectorBlock);
		levelSelectorBlock.setNearbyBlock(OptionBlock.UPPER_BLOCK, gameModeBlock);
		levelSelectorBlock.setNearbyBlock(OptionBlock.RIGHT_BLOCK, newLevelOptionBlock);
		newLevelOptionBlock.setNearbyBlock(OptionBlock.LEFT_BLOCK, levelSelectorBlock);
		newLevelOptionBlock.setNearbyBlock(OptionBlock.UPPER_BLOCK, gameModeBlock);
		newLevelOptionBlock.setNearbyBlock(OptionBlock.RIGHT_BLOCK, spawnOptionBlock);
		spawnOptionBlock.setNearbyBlock(OptionBlock.UPPER_BLOCK, gameModeBlock);
		spawnOptionBlock.setNearbyBlock(OptionBlock.LEFT_BLOCK, newLevelOptionBlock);
		
		blocks.add(filePathBlock);
		blocks.add(gameModeBlock);
		blocks.add(levelSelectorBlock);
		blocks.add(newLevelOptionBlock);
		blocks.add(spawnOptionBlock);
		
		for(int a = 0; a < keysets.length; a++)
		{
			PlayerKeyset pk = keysets[a];
			int w = 90;
			int h = 40;
			int x = a * (4 * w - 20) + 15;
			int y = 360;
			TextBlock name = new TextBlock(this, new Rectangle(x, y - 120, 3 * (w + 10), 50), playerNames[a]);
			BoolBlock onoff = new BoolBlock(this, new Rectangle(x, y - 60, 3 * (w + 10), 50), playerSet[a]);
			KeyBlock up = new KeyBlock(this, new Rectangle(x + w + 10, y, w, h), pk.getUpKey());
			KeyBlock down = new KeyBlock(this, new Rectangle(x + w + 10, y + h + 10, w, h), pk.getDownKey());
			KeyBlock left = new KeyBlock(this, new Rectangle(x, y + h + 10, w, h), pk.getLeftKey());
			KeyBlock right = new KeyBlock(this, new Rectangle(x + 2 * (w + 10), y + h + 10, w, h), pk.getRightKey());
			KeyBlock shoot = new KeyBlock(this, new Rectangle(x + w + 10, y + 2 * (h + 10), w, h), pk.getShootKey());
			TeamOptionBlock team = new TeamOptionBlock(this, new Rectangle(x, y + 3 * (h + 10), 3 * (w + 10), 50), teamSet[a]);
			ImageBlock image = new ImageBlock(this, new Rectangle(x + 105, y + 4 * (h + 10) + 10, 80, 80), playerImageIndices[a]);
			name.setNearbyBlock(OptionBlock.UPPER_BLOCK, blocks.get(2));
			name.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, onoff);
			onoff.setNearbyBlock(OptionBlock.UPPER_BLOCK, name);
			onoff.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, up);
			up.setNearbyBlock(OptionBlock.UPPER_BLOCK, onoff);
			up.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, down);
			up.setNearbyBlock(OptionBlock.LEFT_BLOCK, left);
			up.setNearbyBlock(OptionBlock.RIGHT_BLOCK, right);
			down.setNearbyBlock(OptionBlock.UPPER_BLOCK, up);
			down.setNearbyBlock(OptionBlock.LEFT_BLOCK, left);
			down.setNearbyBlock(OptionBlock.RIGHT_BLOCK, right);
			down.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, shoot);
			left.setNearbyBlock(OptionBlock.RIGHT_BLOCK, down);
			left.setNearbyBlock(OptionBlock.UPPER_BLOCK, up);
			left.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, shoot);
			right.setNearbyBlock(OptionBlock.LEFT_BLOCK, down);
			right.setNearbyBlock(OptionBlock.UPPER_BLOCK, up);
			right.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, shoot);
			shoot.setNearbyBlock(OptionBlock.UPPER_BLOCK, down);
			shoot.setNearbyBlock(OptionBlock.LEFT_BLOCK, left);
			shoot.setNearbyBlock(OptionBlock.RIGHT_BLOCK, right);
			shoot.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, team);
			team.setNearbyBlock(OptionBlock.UPPER_BLOCK, shoot);
			team.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, image);
			image.setNearbyBlock(OptionBlock.UPPER_BLOCK, team);
			blocks.add(name);
			blocks.add(onoff);
			blocks.add(up);
			blocks.add(down);
			blocks.add(left);
			blocks.add(right);
			blocks.add(shoot);
			blocks.add(team);
			blocks.add(image);
			if(a > 0)
			{
				int size = blocks.size();
				KeyBlock prevRight = (KeyBlock) blocks.get(size - 13);
				left.setNearbyBlock(OptionBlock.LEFT_BLOCK, prevRight);
				prevRight.setNearbyBlock(OptionBlock.RIGHT_BLOCK, left);
				TextBlock prevText = (TextBlock) blocks.get(size - 18);
				name.setNearbyBlock(OptionBlock.LEFT_BLOCK, prevText);
				prevText.setNearbyBlock(OptionBlock.RIGHT_BLOCK, name);
				BoolBlock prevOnOff = (BoolBlock) blocks.get(size - 17);
				onoff.setNearbyBlock(OptionBlock.LEFT_BLOCK, prevOnOff);
				prevOnOff.setNearbyBlock(OptionBlock.RIGHT_BLOCK, onoff);
				TeamOptionBlock prevTeam = (TeamOptionBlock) blocks.get(size - 11);
				team.setNearbyBlock(OptionBlock.LEFT_BLOCK, prevTeam);
				prevTeam.setNearbyBlock(OptionBlock.RIGHT_BLOCK, team);
				ImageBlock prevImage = (ImageBlock) blocks.get(size - 10);
				prevImage.setNearbyBlock(OptionBlock.RIGHT_BLOCK, image);
				image.setNearbyBlock(OptionBlock.LEFT_BLOCK, prevImage);
			}
			if(a == 1)
			{
				image.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, soundBoolBlock);
				soundBoolBlock.setNearbyBlock(OptionBlock.UPPER_BLOCK, image);
			}
			else if(a == 3)
			{
				image.setNearbyBlock(OptionBlock.BOTTOM_BLOCK, musicBoolBlock);
				musicBoolBlock.setNearbyBlock(OptionBlock.UPPER_BLOCK, image);
			}
		}
		blocks.get(2).setNearbyBlock(OptionBlock.BOTTOM_BLOCK, blocks.get(5));
		blocks.get(3).setNearbyBlock(OptionBlock.BOTTOM_BLOCK, blocks.get(23));
		blocks.get(23).setNearbyBlock(OptionBlock.UPPER_BLOCK, blocks.get(3));
		blocks.get(4).setNearbyBlock(OptionBlock.BOTTOM_BLOCK, blocks.get(32));
		blocks.get(32).setNearbyBlock(OptionBlock.UPPER_BLOCK, blocks.get(4));
		blocks.add(soundBoolBlock);
		blocks.add(musicBoolBlock);
		this.setOption(0);
	}
	
	private class FileSelectorBlock extends TextBlock
	{
		
		public File file;
		public JFileChooser fileChooser;
		
		public FileSelectorBlock(OptionsState optionsState, Rectangle box, String text) {
			super(optionsState, box, text);
			fileChooser = new JFileChooser();
		}
		
		@Override
		public void keyPressed(int k)
		{
			if(k == KeyEvent.VK_ENTER)
			{
				fileChooser.setCurrentDirectory(new File(GamePanel.getRootFilePath()));
				int returnVal = fileChooser.showOpenDialog(null);
				File tmp;
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					tmp = fileChooser.getSelectedFile();
					if(!tmp.isDirectory() && tmp.getName().contains(".map"))
					{
						file = tmp;
						this.setText(file.getName());
					}
					else return;
				}
			}
			else
			{
				this.switchBlock(k);
			}
		}
	}
	
	private class FolderSelectorBlock extends FileSelectorBlock
	{
		
		public FolderSelectorBlock(OptionsState optionsState, Rectangle box, String text) {
			super(optionsState, box, text);
			this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		
		@Override
		public void keyPressed(int k)
		{
			if(k == KeyEvent.VK_ENTER)
			{
				fileChooser.setCurrentDirectory(new File(GamePanel.getRootFilePath()));
				int returnVal = fileChooser.showOpenDialog(null);
				File tmp;
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					tmp = fileChooser.getSelectedFile();
					if(tmp.isDirectory())
					{
						file = tmp;
						this.setText(file.getAbsolutePath());
						GamePanel.setRootFilePath(file.getAbsolutePath());
						System.out.println(file.getAbsolutePath());
					}
					else return;
				}
			}
			else
			{
				this.switchBlock(k);
			}
		}
		
	}
	
	
	public Library getLibrary() { return library; }
}
