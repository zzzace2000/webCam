package readFromWebCAM;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

//Code runs fine with OpenCV 2.4.4

public class Main {
	
	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}
	
        // OR load the native library this way.
        // System.loadLibrary("opencv_java244");
	
	public static void main(String[] args) throws Exception {  //the exception is for the sleep call
          System.out.println("Hello, OpenCV");

           Mat frame = new Mat();
          VideoCapture cap = new VideoCapture(0);
                                
          Thread.sleep(500);	// 0.5 sec of a delay. This is not obvious but its necessary
                                // as the camera simply needs time to initialize itself
          
          if(!cap.isOpened()){
            System.out.println("Did not connect to camera");
          }else System.out.println("found webcam: "+ cap.toString());
         
        
          cap.retrieve(frame);// The current frame in the camera is saved in "frame"
          System.out.println("Captured image with "+ frame.width()+ " pixels wide by "         		  										+ frame.height() + " pixels tall.");
          Highgui.imwrite("me1.jpg", frame);
          Mat frameBlur = new Mat();
          Imgproc.blur(frame, frameBlur, new Size(5,5) );
          Highgui.imwrite("me2-blurred.jpg", frameBlur);

          cap.release(); // Remember to release the camera
        }
	
	
   }