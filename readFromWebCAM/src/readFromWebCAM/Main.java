package readFromWebCAM;
/*  
 * Captures the camera stream with OpenCV  
 * Search for the faces  
 * Display a circle around the faces using Java  
 */  
import java.awt.*;  
import java.awt.image.BufferedImage;  
import java.io.ByteArrayInputStream;  
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;  
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;  
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;  

import org.opencv.core.Core;  
import org.opencv.core.Mat;  
import org.opencv.core.MatOfByte;  
import org.opencv.core.MatOfRect;  
import org.opencv.core.Point;  
import org.opencv.core.Rect;  
import org.opencv.core.Scalar;  
import org.opencv.core.Size;  
import org.opencv.highgui.Highgui;  
import org.opencv.highgui.VideoCapture;  
import org.opencv.imgproc.Imgproc;  
import org.opencv.objdetect.CascadeClassifier;  

class FacePanel extends JPanel{  
     private static final long serialVersionUID = 1L;  
     private BufferedImage image;  
     // Create a constructor method  
     public FacePanel(){  
          super();   
     }  
     /*  
      * Converts/writes a Mat into a BufferedImage.  
      *   
      * @param matrix Mat of type CV_8UC3 or CV_8UC1  
      * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
      */       
     public boolean matToBufferedImage(Mat matrix) {  
          MatOfByte mb=new MatOfByte();  
          Highgui.imencode(".jpg", matrix, mb);  
          try {  
               this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
          } catch (IOException e) {  
               e.printStackTrace();  
               return false; // Error  
          }  
       return true; // Successful  
     }  
     public void paintComponent(Graphics g){  
          super.paintComponent(g);   
          if (this.image==null) return;         
           g.drawImage(this.image,10,10,this.image.getWidth(),this.image.getHeight(), null);
     }
        
}  
class FaceDetector {  
     private CascadeClassifier face_cascade;  
     // Create a constructor method  
     public FaceDetector(){  
         // face_cascade=new CascadeClassifier("./cascades/lbpcascade_frontalface_alt.xml");  
         //..didn't have not much luck with the lbp
         
        face_cascade=new CascadeClassifier(".//res//lbpcascade_frontalface.xml"); 
          if(face_cascade.empty())  
          {  
               System.out.println("--(!)Error loading A\n");  
                return;  
          }  
          else  
          {  
               System.out.println("Face classifier loooaaaaaded up");  
          }  
     }  
     public Mat detect(Mat inputframe){  
          Mat mRgba=new Mat();  
          Mat mGrey=new Mat();  
          MatOfRect faces = new MatOfRect();  
          inputframe.copyTo(mRgba);  
          inputframe.copyTo(mGrey);  
          Imgproc.cvtColor( mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);  
          Imgproc.equalizeHist( mGrey, mGrey );  
          face_cascade.detectMultiScale(mGrey, faces);  
          System.out.println(String.format("Detected %s faces", faces.toArray().length));  
          for(Rect rect:faces.toArray())  
          {  
               Point center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5 );  
               Core.rectangle(mRgba,  //where to draw the box
           		    new Point(rect.x, rect.y),   //bottom left
           		    new Point(rect.x + rect.width, rect.y + rect.height), //top right 
           		    new Scalar(255, 0, 0)); //RGB colour
               //draw a blue eclipse around face
               //Size s = new Size( rect.width*0.5, rect.height*0.5), 0, 0, 360, new Scalar( 0, 0, 255 );
               //Core.ellipse( mRgba, center,s , 4, 8, 0 );  
          }  
          return mRgba;  
     }  
}  
public class Main {  

    static Socket theSocket;
    
	public static void main(String arg[]) throws InterruptedException{  
      // Load the native library.  
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      //or ...     System.loadLibrary("opencv_java244"); 
      new Main();
	}  
    
	public Main() {
		//make the JFrame
	      JFrame frame = new JFrame("WebCam Capture - Face detection");  
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	     
	      FaceDetector faceDetector=new FaceDetector();  
	      FacePanel facePanel = new FacePanel();  
	      frame.setSize(400,400); //give the frame some arbitrary size 
	      frame.setBackground(Color.BLUE);
	      frame.add(facePanel,BorderLayout.CENTER);       
	      frame.setVisible(true);  
	      DataOutputStream outputStream;
	      //Open and Read from the video stream  
	       Mat webcam_image=new Mat();  
	       VideoCapture webCam =new VideoCapture(0);   
	      
	      try {
			theSocket = new Socket(InetAddress.getByName("140.112.18.196"), 19999);
			System.out.println("Client connect!");
			
	      } catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	   try {
	   OutputStream oStream = theSocket.getOutputStream();
	   ArrayList<BufferedImage> imageBuffer = new ArrayList<BufferedImage>();

	   if( webCam.isOpened())  {  
	        Thread.sleep(500);
			outputStream = new DataOutputStream(oStream);
	        outputStream.writeUTF("/s");
	      
	        while( true )  
	        {  
	        	 webCam.read(webcam_image);
	        	 System.out.println("Catch an image!");
	             if( !webcam_image.empty() )  
	             {   
	            	 /// This delay eases the computational load .. with little performance leakage
	                   //frame.setSize(webcam_image.width()+40,webcam_image.height()+60);  
	                   //Apply the classifier to the captured image  
	                   //webcam_image=faceDetector.detect(webcam_image);  
	                  //Display the image
	            	   MatOfByte mb=new MatOfByte();
	            	   Highgui.imencode(".jpg", webcam_image, mb);
	            	
	            	   BufferedImage image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
	            	   ImageIO.write(image, "JPEG", oStream);
					  
	                   facePanel.matToBufferedImage(webcam_image);  
	                   facePanel.repaint();   
	             }  
	             else  
	             {   
	                   System.out.println(" --(!) No captured frame from webcam !");   
	                   break;   
	             }  
	        }  
	         
	        webCam.release(); //release the webcam
	   }
	   } catch (InterruptedException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
    } //end main 
	
	
}
	 