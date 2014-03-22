package soundRecorderByUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class SoundRecorderMain {

	private static final String IP_TO_STREAM_TO = "localhost";
	private static final int PORT_TO_STREAM_TO = 8888;
	DatagramSocket theSocket;

	TargetDataLine targetDataLine;
	DataLine.Info dataLineInfo;

	/** Creates a new instance of SoundRecorderMain */
	public SoundRecorderMain() {
		try {
			
			theSocket = new DatagramSocket();

		} catch (SocketException e) {
			e.printStackTrace();
		}

		Mixer.Info minfo[] = AudioSystem.getMixerInfo();
		for (int i = 0; i < minfo.length; i++) {
			System.out.println(minfo[i]);
		}

		if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
			try {

				dataLineInfo = new DataLine.Info(TargetDataLine.class,
						getAudioFormat());
				targetDataLine = (TargetDataLine) AudioSystem
						.getLine(dataLineInfo);
				targetDataLine.open(getAudioFormat());
				targetDataLine.start();
				byte tempBuffer[] = new byte[8000];
				int cnt = 0;
				while (true) {
					targetDataLine.read(tempBuffer, 0, tempBuffer.length);
					sendThruUDP(tempBuffer);
				}

			} catch (Exception e) {
				System.out.println(" not correct ");
				System.exit(0);
			} finally {
				targetDataLine.drain();
				targetDataLine.close();
				theSocket.close();
			}
		}

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		new SoundRecorderMain();
	}

	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	public void sendThruUDP(byte soundpacket[]) {
		try {
			theSocket.send(new DatagramPacket(soundpacket, soundpacket.length,
					InetAddress.getByName(IP_TO_STREAM_TO), PORT_TO_STREAM_TO));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" Unable to send soundpacket using UDP ");
		}

	}

}