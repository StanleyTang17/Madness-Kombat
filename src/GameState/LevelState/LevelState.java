package GameState.LevelState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import GameObject.Library;
import GameObject.Player.Team;
import GameState.GameState;
import GameState.GameStateManager;
import Main.MusicLoop;

public class LevelState extends GameState{
	
	private int stateIndex;
	private GameState[] states;
	private Library library;
	private Team[] teamSet;
	private BufferedImage[] playerImages;
	private MusicLoop soundtrack;
	
	private static GameMode gameMode = GameMode.LAST_MAN_STANDING;
	
	public static GameMode getGameMode() { return gameMode; }
	public static void setGameMode(GameMode gameMode) { LevelState.gameMode = gameMode; }
	
	public static final int PLAYING = 0;
	public static final int TRANSITION = 1;
	public static final int PAUSE = 2;
	
	public LevelState(GameStateManager gsm) {
		this.gsm = gsm;
		states = new GameState[3];
		states[PLAYING] = new PlayingState(gsm, this);
		states[TRANSITION] = new TransitionState(this);
		states[PAUSE] = new PauseState(this);
		soundtrack = new MusicLoop(getClass().getResource("/levelSoundtrack.wav"));
	}

	
	public void init() {
		soundtrack.play();
		states[PLAYING] = new PlayingState(gsm, this);
		states[TRANSITION] = new TransitionState(this);
		states[PAUSE] = new PauseState(this);
		PlayingState ps = (PlayingState) states[LevelState.PLAYING];
		ps.setLibrary(library);
		ps.setTeamSet(teamSet);
		ps.setPlayerImages(playerImages);
		setState(PLAYING);
		initState(PLAYING);
	}

	
	public void update() {
		if(states[stateIndex] != null)
		states[stateIndex].update();
	}

	
	public void draw(Graphics2D g) {
		if(states[stateIndex] != null)
		states[stateIndex].draw(g);
		
	}

	
	public void keyPressed(int k) {
		states[stateIndex].keyPressed(k);
	}

	
	public void keyReleased(int k) {
		states[stateIndex].keyReleased(k);
	}
	
	public void initState(int stateIndex) { states[stateIndex].init(); }
	public void setState(int stateIndex) { this.stateIndex = stateIndex; }
	public GameState getState(int index) { return states[index]; }
	public void exitLevelState() { soundtrack.stop(); this.gsm.setState(GameStateManager.MENUSTATE); }
	public void setLibrary(GameObject.Library lib) { this.library = lib; }
	public void setTeamSet(Team[] teamSet) { this.teamSet = teamSet; }
	public void setPlayerImages(BufferedImage[] playerImages) { this.playerImages = playerImages; }
	public void pauseMusic() { this.soundtrack.pause(); }
	public void resumeMusic() { this.soundtrack.resume(); }
	public void restartMusic() { this.soundtrack.play(); }
}
