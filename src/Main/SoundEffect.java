package Main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffect {
	
	public static boolean MUTE;
	
	private AudioInputStream sound;
	private URL url;
	private Clip clip;
	
	public SoundEffect(URL url)
	{
		this.url = url;
	}
	
	public void play()
	{
		if(MUTE) return;
		try {
			sound = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(sound);
			clip.setFramePosition(0);
			clip.start();
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		if(clip != null) clip.stop();
	}
}
