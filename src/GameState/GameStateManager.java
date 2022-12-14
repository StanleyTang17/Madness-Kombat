package GameState;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import GameObject.Player.PlayerKeyset;
import GameState.LevelState.LevelState;
import GameState.OptionState.OptionsState;
import Main.GamePanel;
import TileMap.Background;

public class GameStateManager {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	private LevelState levelState;
	private PlayerKeyset[] playerKeysets;
	private ArrayList<Integer> playerPreset;
	private String[] playerNames;
	
	public static final int MENUSTATE = 0;
	public static final int LEVELSTATE = 1;
	public static final int EDITORSTATE = 2;
	public static final int OPTIONSTATE = 3;
	
	public Background bg;
	public GamePanel gp;
	
	public GameStateManager(GamePanel gp)
	{
		this.gp = gp;
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		
		bg = new Background("/menubg.png", 1);
		bg.setVector(-0.5, 0);
		
		playerKeysets = new PlayerKeyset[4];
		playerKeysets[0] = new PlayerKeyset(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SLASH);
		playerKeysets[1] = new PlayerKeyset(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_C);
		playerKeysets[2] = new PlayerKeyset(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H, KeyEvent.VK_N);
		playerKeysets[3] = new PlayerKeyset(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_SEMICOLON);
		playerPreset = new ArrayList<Integer>();
		levelState = new LevelState(this);
		gameStates.add(levelState);
		gameStates.add(new EditorState(this));
		gameStates.add(new OptionsState(this));
	}
	
	public void setState(int state)
	{
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public GameState getState(int state)
	{
		return gameStates.get(state);
	}
	
	public void update()
	{
		gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int k)
	{
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k)
	{
		gameStates.get(currentState).keyReleased(k);
	}
	
	public PlayerKeyset getKeyset(int index)
	{
		return playerKeysets[index];
	}
	
	public PlayerKeyset[] getKeyset()
	{
		return playerKeysets;
	}
	
	public void setKeyset(PlayerKeyset[] keysets)
	{
		playerKeysets = keysets;
	}
	
	public ArrayList<Integer> getPlayerPreset()
	{
		return playerPreset;
	}
	
	public void setPlayerPreset(ArrayList<Integer> preset)
	{
		playerPreset = preset;
	}
	
	public void setPlayerNames(String[] names) { playerNames = names; }
	public String[] getPlayerNames() { return playerNames; }
	
}
