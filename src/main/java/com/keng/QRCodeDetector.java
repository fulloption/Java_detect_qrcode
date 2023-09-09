package com.keng;


import com.google.zxing.BarcodeReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

public class QRCodeDetector {
    public static void main(String[] args) {
        // Create a BarcodeReader object
        BarcodeReader barcodeReader = new MultiFormatReader();

        // Set the source image file for the BarcodeReader object
        BufferedImage image = ImageIO.read(new File("qrcode.png"));
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(image);
        BinaryBitmap binaryBitmap = new BinaryBitmap(luminanceSource);

        // Call the decode() method on the BarcodeReader object to decode the QR code
        Result result = barcodeReader.decode(binaryBitmap);

        // Get the decoded text from the BarcodeReader object
        String text = result.getText();

        System.out.println("The decoded text is: " + text);


    }
    private static void readQRCode(BufferedImage image) {

        // Enhance contrast
        BufferedImage enhancedImage = enhanceContrast(image);

//        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(enhancedImage)));

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));

        try {
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);
            handleQRCodeResult(result.getText());
        } catch (NotFoundException e) {
            System.out.println("Error NotFoundException: " + e.toString());
            // QR code not found in the image
        } catch (ChecksumException e) {
            System.out.println("Error ChecksumException: " + e.toString());
            //throw new RuntimeException(e);
        } catch (FormatException e) {
            System.out.println("Error FormatException: " + e.toString());
            //throw new RuntimeException(e);
        }
    }
    private static void handleQRCodeResult(String resultText) {
        // Handle the QR code result (e.g., display it)
        System.out.println("QR Code Result: " + resultText);
    }
    private static BufferedImage enhanceContrast(BufferedImage image) {
        // You can implement your contrast enhancement algorithm here
        // For simplicity, we'll convert white to black and black to white in this example
        BufferedImage enhancedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                // Invert colors (white to black, black to white)
                pixel = 0xFFFFFF - pixel;
                enhancedImage.setRGB(x, y, pixel);
            }
        }

        return enhancedImage;
    }

}