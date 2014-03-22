package webCamReceiverUDP;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class receiver {
	
	receiver theReceiver;
	DatagramSocket ss;
	Socket theSocket;
	static int PORT_TO_STREAM_TO = 19998;
	static int UDP_MAX_SIZE = 64000;
	FacePanel facePanel;
    boolean initialSizeNotSet = true;

	
	public static void main(String[] args)
	{
		new receiver();
	}
	
	
	public receiver() {
		//make the JFrame
	    JFrame frame = new JFrame("WebCam Capture - Face detection");  
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	     
	    //FaceDetector faceDetector=new FaceDetector();  
		facePanel = new FacePanel();
	    frame.setSize(400,400); //give the frame some arbitrary size 
	    frame.setBackground(Color.BLUE);
	    frame.add(facePanel,BorderLayout.CENTER);       
	    frame.setVisible(true);
	
	    try {
			ss = new DatagramSocket(PORT_TO_STREAM_TO);
			
			ByteArrayInputStream bais = null;
			
			System.out.println("Assign the Socket");
			
			if (ss != null) {
				while (true) {
					byte[] receivedBuffer = new byte[UDP_MAX_SIZE];
					
					DatagramPacket receivedPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
					
					ss.receive(receivedPacket);					
					
                    // Wrap the result in an InputStream
                    bais = new ByteArrayInputStream(receivedPacket.getData());
                    
					BufferedImage image = ImageIO.read(bais);
					
					if (image != null) {
						facePanel.assignBufferedImage(image);
						if (initialSizeNotSet) {
							frame.setSize(image.getWidth(), image.getHeight());
							initialSizeNotSet = false;
						}
						facePanel.repaint();
						System.out.println("image is gotten!");
					}
					else {
						System.out.println("image is empty in receiver!");
					}
	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				theSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}

class FacePanel extends JPanel{  
    private static final long serialVersionUID = 1L;  
    private BufferedImage image;  
    // Create a constructor method  
    public FacePanel(){  
         super();
         this.setBackground(Color.blue);
    }  
    /*  
     * Converts/writes a Mat into a BufferedImage.  
     *   
     * @param matrix Mat of type CV_8UC3 or CV_8UC1  
     * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
     */       
    public boolean assignBufferedImage(BufferedImage theImage) {  
         if (theImage != null) {
       	  this.image = theImage;
       	  return true;
         }
         return false;
    }  
    public void paintComponent(Graphics g){  
         super.paintComponent(g);   
         if (this.image==null) return;         
          g.drawImage(this.image,0,0,this.image.getWidth(),this.image.getHeight(), null);
    }
       
}  

