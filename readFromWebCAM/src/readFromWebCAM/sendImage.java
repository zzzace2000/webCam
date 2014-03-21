package readFromWebCAM;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class sendImage implements Runnable{
	
	Main theMain;
	Socket theSocket;
	OutputStream oStream;
	
	public sendImage(Main mn, Socket s) {
		theMain = mn;
		theSocket = s;
  	    try {
			oStream = theSocket.getOutputStream();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run() {
		
		try {
			while(true) {
				if (!theMain.imageBuffer.isEmpty()) {
					Thread.sleep(500);
					System.out.println("Write something");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					BufferedImage theImage = theMain.imageBuffer.remove(theMain.imageBuffer.size()-1);
					theMain.imageBuffer.clear();
					
					ImageIO.write(theImage, "jpg", baos);
					baos.close();
					System.out.println("Write byte size = " + baos.size());
					oStream.write((Integer.toString(baos.size()) + "\n").getBytes());
					System.out.println("Write byte stream");
					oStream.write(baos.toByteArray());
					//theMain.imageIsNotSent = false;
				}
			}
		} catch (IOException e) {
			System.out.println("Wrong in image WriteIO");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				theSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
