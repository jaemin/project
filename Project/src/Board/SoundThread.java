package Board;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class SoundThread implements Runnable {
	private static Clip c;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		File sound = new File("Ikson - Walk.wav");// mp3����x mav����o
		PlaySound(sound);
	}

	public void PlaySound(File sound) // �����������
	{
		try {
			c = AudioSystem.getClip();
			c.open(AudioSystem.getAudioInputStream(sound));
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			//c.start();
			// ���ѹݺ� (int�� Ƚ��-1)
			c.loop(Clip.LOOP_CONTINUOUSLY);
			Thread.sleep(c.getMicrosecondLength() / 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		c.stop();
	}
}