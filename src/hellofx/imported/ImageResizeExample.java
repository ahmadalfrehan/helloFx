
package hellofx.imported;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResizeExample {
    public static void main(String[] args) {
        String inputImagePath = "path/to/input/image.jpg";
        String outputImagePath = "path/to/output/resized_image.jpg";
        int newWidth = 300; // desired width in pixels
        int newHeight = 200; // desired height in pixels

        try {
            // Read the input image
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);

            // Create a scaled version of the input image
            Image scaledImage = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Create a BufferedImage object with the desired size
            BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

            // Draw the scaled image on the BufferedImage
            Graphics2D graphics = outputImage.createGraphics();
            graphics.drawImage(scaledImage, 0, 0, null);
            graphics.dispose();

            // Write the resized image to the output file
            File outputFile = new File(outputImagePath);
            ImageIO.write(outputImage, "jpg", outputFile);

            System.out.println("Image resized successfully.");
        } catch (IOException e) {
            System.out.println("Error while resizing the image: " + e.getMessage());
        }
    }
}
