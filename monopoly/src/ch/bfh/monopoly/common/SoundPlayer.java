package ch.bfh.monopoly.common;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

	public SoundPlayer(){
		playSound("/ch/bfh/monopoly/resources/theme.wav");
	}
	
	public void playSound(Sounds sound) {
		switch (sound) {
		case FOOT_STEP:
			playSound("/ch/bfh/monopoly/resources/footstep.wav");
			break;
		case MOUSE_DOWN:
			playSound("/ch/bfh/monopoly/resources/mouseDown.wav");
			break;
		case MOUSE_UP:
			playSound("/ch/bfh/monopoly/resources/mouseUp.wav");
			break;
		case CASH:
			playSound("/ch/bfh/monopoly/resources/cash.wav");
			break;
		case ROLL:
			playSound("/ch/bfh/monopoly/resources/roll.wav");
			break;
		case SAW:
			playSound("/ch/bfh/monopoly/resources/saw.wav");
			break;
		case THEME:
			playSound("/ch/bfh/monopoly/resources/theme.wav");
			break;
		default:
			break;
		}
		
	}
	
	public synchronized void playSound(String soundFile) {
		final String soundFileFinal = soundFile; 
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless
									// it blocks on the Clip finishing, see
									// comments
					public void run() {
						try {
							Clip clip = AudioSystem.getClip();
							AudioInputStream inputStream = AudioSystem
									.getAudioInputStream(getClass()
											.getResourceAsStream(
													soundFileFinal));
							clip.open(inputStream);
							clip.start();
						} catch (Exception e) {
							System.err.println(e.getMessage()+ "sound file location:" +soundFileFinal);
						}
					}
				}).start();
	}
}
