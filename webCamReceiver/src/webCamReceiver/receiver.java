package webCamReceiver;

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
	ServerSocket ss;
	Socket theSocket;
	int port = 19999;
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
			ss = new ServerSocket(port);
			
			Socket theSocket = ss.accept();
			InputStream iStream = theSocket.getInputStream();
			OutputStream oStream = theSocket.getOutputStream();
			
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			
			System.out.println("Assign the Socket");
			
			if (theSocket != null) {
				while (true) {
					String size = readResponse(iStream);
					int expectedByteCount = Integer.parseInt(size);
					System.out.println("Expecting "+expectedByteCount);
					
					baos = new ByteArrayOutputStream(expectedByteCount);
					byte[] buffer = new byte[1024];
					int bytesRead = 0;
                    int bytesIn = 0;
                    
                    // Read the image from the server...
                    while (bytesRead < expectedByteCount) {
                        bytesIn = iStream.read(buffer);
                        bytesRead += bytesIn;
                        baos.write(buffer, 0, bytesIn);
                    }
                    System.out.println("Read " + bytesRead);
                    baos.close();
                    
                    // Wrap the result in an InputStream
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	protected String readResponse(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder(128);
        int in = -1;
        while ((in = is.read()) != '\n') {
            sb.append((char) in);
        }
        return sb.toString();
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

