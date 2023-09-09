package com.keng;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.QRCodeDetector;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Load the OpenCV library
        URL location = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String path = (location.getPath().substring(0,location.getPath().lastIndexOf("/")));
        System.load(path+"/opencv_java452.dll");
        System.load(path+"/opencv_videoio_ffmpeg452_64.dll");

        // Create a VideoCapture object to access the camera (use 0 for the default camera)
        VideoCapture camera = new VideoCapture(0);

        // Check if the camera is opened successfully
        if (!camera.isOpened()) {
            System.out.println("Error: Unable to access the camera.");
            return;
        }

        // Create a JFrame to display the camera feed
        JFrame frame = new JFrame("Camera Feed");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Create a QRCodeDetector object
        QRCodeDetector qrCodeDetector = new QRCodeDetector();

        // Main loop to read frames from the camera and detect QR codes
        while (true) {
            // Read a frame from the camera
            Mat frameMat = new Mat();
            camera.read(frameMat);

            // Detect QR codes in the frame
            // Detect QR codes in the frame
            List<String> decodedTextList = new ArrayList<>();
            MatOfPoint2f points = new MatOfPoint2f();
            boolean success = qrCodeDetector.detectAndDecodeMulti(frameMat, decodedTextList, points);

            // If a QR code is detected, display the decoded text and draw the bounding box
            if (success) {
                System.out.println("Decoded Text: " + decodedTextList);
                Point[] pointsArray = points.toArray();
                for (int j = 0; j < pointsArray.length; j++) {
                    Imgproc.line(frameMat, pointsArray[j], pointsArray[(j + 1) % pointsArray.length], new Scalar(0, 255, 0), 2);
                }
            }

            // Display the frame with bounding box (if QR code detected)
            BufferedImage image = matToBufferedImage(frameMat);
            ImageIcon imageIcon = new ImageIcon(image);
            frame.setContentPane(new JLabel(imageIcon));
            frame.pack();

            // Wait for a while (e.g., 33ms) to simulate real-time display
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to convert a Mat object to a BufferedImage
    private static BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }
    // Method to convert MatOfPoint2f to List<String>
    private static List<String> convertMatOfPoint2fToListOfString(MatOfPoint2f matOfPoint2f) {
        List<String> pointsAsStringList = new ArrayList<>();
        Point[] pointsArray = matOfPoint2f.toArray();
        for (Point point : pointsArray) {
            pointsAsStringList.add("(" + point.x + ", " + point.y + ")");
        }
        return pointsAsStringList;
    }
}