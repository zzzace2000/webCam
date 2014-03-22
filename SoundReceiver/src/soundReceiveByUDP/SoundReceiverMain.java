package soundReceiveByUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundReceiverMain extends Thread {

	private static final String IP_TO_STREAM_TO = "localhost";
	private static final int PORT_TO_STREAM_TO = 8888;
	static DatagramSocket theSocket;
	DataLine.Info dataLineInfo;
	SourceDataLine sourceDataLine;

	/** Creates a new instance of RadioReceiver */
	public SoundReceiverMain() {
		try {
			theSocket = new DatagramSocket(PORT_TO_STREAM_TO);
			dataLineInfo = new DataLine.Info(SourceDataLine.class,
					getAudioFormat());
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(getAudioFormat());
			sourceDataLine.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		byte b[] = null;
		while (true) {
			b = receiveThruUDP();
			toSpeaker(b);
		}
	}

	public void shutDown() {
		theSocket.close();
		sourceDataLine.drain();
		sourceDataLine.close();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		SoundReceiverMain r = new SoundReceiverMain();
		r.start();

	}

	public static byte[] receiveThruUDP() {
		try {
			byte soundpacket[] = new byte[8000];
			DatagramPacket datagram = new DatagramPacket(soundpacket,
					soundpacket.length);
			theSocket.receive(datagram);

			return datagram.getData(); // soundpacket ;
		} catch (Exception e) {
			System.out.println(" Unable to send soundpacket using UDP ");
			return null;
		}

	}

	public void toSpeaker(byte soundbytes[]) {

		try {

			sourceDataLine.write(soundbytes, 0, soundbytes.length);

		} catch (Exception e) {
			System.out.println("not working in speakers ");
		}

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

}