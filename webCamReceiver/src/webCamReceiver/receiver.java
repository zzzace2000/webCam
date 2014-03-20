package webCamReceiver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class receiver {
	
	static receiver theReceiver;
	static ServerSocket ss;
	Socket theSocket;
	static int port = 19999;
	static FacePanel facePanel;
	
	public static void main(String[] args)
	{
		theReceiver = new receiver();
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
			ss = new ServerSocket(port);
			
			Socket theSocket = ss.accept();
			System.out.println("Assign the Socket");
			
			DataInputStream is = new DataInputStream(theSocket.getInputStream());
			
			if (is.readUTF().startsWith("/s")) {
				System.out.println("get the value /s");
				while (true) {
					BufferedImage image = ImageIO.read(theSocket.getInputStream());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (image != null) {
						facePanel.assignBufferedImage(image);
						facePanel.repaint();
						System.out.println("image is gotten!");
					}
					else {
						System.out.println("image is empty in receiver!");
					}
	
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}
	
	public receiver() {
		
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
          g.drawImage(this.image,10,10,this.image.getWidth(),this.image.getHeight(), null);
    }
       
}  

