package Main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicLoop {
	
	public static boolean MUTE;
	
	private Clip clip;
	private int lastPaused = 0;
	
	public MusicLoop(URL url) {
		try {
			AudioInputStream sound = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(sound);
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void play()
	{
		if(MUTE) return;
		clip.setFramePosition(0);
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void pause()
	{
		if(MUTE) return;
		lastPaused = clip.getFramePosition();
		stop();
	}
	
	public void resume()
	{
		if(MUTE) return;
		clip.setFramePosition(lastPaused);
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop()
	{
		if(MUTE) return;
		if(clip != null) clip.stop();
	}
	
}
