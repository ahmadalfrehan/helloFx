package hellofx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;

public class OctreeQuantizer {

    private static final int MAX_DEPTH = 8;
    private static final int MAX_COLORS = 64;

    public static BufferedImage quantize(BufferedImage inputImage) {
        OctreeNode root = new OctreeNode(0);
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                root.insertColor(new Color(rgb), MAX_DEPTH);
            }
        }

        root.calculateRepresentativeColors();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                Color mappedColor = root.getNearestColor(new Color(rgb));
                outputImage.setRGB(x, y, mappedColor.getRGB());
            }
        }
        return outputImage;
    }
}
