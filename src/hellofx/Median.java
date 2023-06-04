package hellofx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Median {

    public static void main(String[] args) {
        BufferedImage sampleImage = null;
        try {
            sampleImage = ImageIO.read(new File("C:/Users/dell/Desktop/girl.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int[][] flattenedImgArray = new int[sampleImage.getWidth() * sampleImage.getHeight()][5];
        int index = 0;
        for (int r = 0; r < sampleImage.getHeight(); r++) {
            for (int c = 0; c < sampleImage.getWidth(); c++) {
                Color color = new Color(sampleImage.getRGB(c, r));
                flattenedImgArray[index][0] = color.getRed();
                flattenedImgArray[index][1] = color.getGreen();
                flattenedImgArray[index][2] = color.getBlue();
                flattenedImgArray[index][3] = r;
                flattenedImgArray[index][4] = c;
                index++;
            }
        }

        splitIntoBuckets(sampleImage, flattenedImgArray, 3);

        // Save the modified image
        File outputImageFile = new File(System.getProperty("user.home") + "/Desktop/output4.png");
        try {
            ImageIO.write(sampleImage, "png", outputImageFile);
            System.out.println("image saved successfully: " + outputImageFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save image: " + e.getMessage());
        }
    }

    public static void splitIntoBuckets(BufferedImage img, int[][] imgArr, int depth) {
        if (imgArr.length == 0) {
            return;
        }

        if (depth == 0) {
            medianCutQuantize(img, imgArr);
            return;
        }

        int rRange = getMax(imgArr, 0) - getMin(imgArr, 0);
        int gRange = getMax(imgArr, 1) - getMin(imgArr, 1);
        int bRange = getMax(imgArr, 2) - getMin(imgArr, 2);

        int spaceWithHighestRange = 0;

        if (gRange >= rRange && gRange >= bRange) {
            spaceWithHighestRange = 1;
        } else if (bRange >= rRange && bRange >= gRange) {
            spaceWithHighestRange = 2;
        } else if (rRange >= bRange && rRange >= gRange) {
            spaceWithHighestRange = 0;
        }

        System.out.println("spaceWithHighestRange: " + spaceWithHighestRange);

        sortByColorSpace(imgArr, spaceWithHighestRange);

        int medianIndex = (imgArr.length + 1) / 2;
        System.out.println("medianIndex: " + medianIndex);

        splitIntoBuckets(img, getSubArray(imgArr, 0, medianIndex), depth - 1);
        splitIntoBuckets(img, getSubArray(imgArr, medianIndex, imgArr.length), depth - 1);
    }

    public static void medianCutQuantize(BufferedImage img, int[][] imgArr) {
        System.out.println("to quantize: " + imgArr.length);

        int rAverage = getAverage(imgArr, 0);
        int gAverage = getAverage(imgArr, 1);
        int bAverage = getAverage(imgArr, 2);

        for (int[] data : imgArr) {
            int r = data[3];
            int c = data[4];
            int rgb = (rAverage << 16) | (gAverage << 8) | bAverage;
            img.setRGB(c, r, rgb);
        }
    }

    public static int getMin(int[][] arr, int column) {
        int min = Integer.MAX_VALUE;
        for (int[] data : arr) {
            min = Math.min(min, data[column]);
        }
        return min;
    }

    public static int getMax(int[][] arr, int column) {
        int max = Integer.MIN_VALUE;
        for (int[] data : arr) {
            max = Math.max(max, data[column]);
        }
        return max;
    }

    public static int getAverage(int[][] arr, int column) {
        int sum = 0;
        for (int[] data : arr) {
            sum += data[column];
        }
        return sum / arr.length;
    }

    public static void sortByColorSpace(int[][] arr, int space) {
        Arrays.sort(arr, (a, b) -> Integer.compare(a[space], b[space]));
  
      
        // for (int i = 0; i < arr.length - 1; i++) {
        //     for (int j = 0; j < arr.length - i - 1; j++) {
        //         if (arr[j][space] > arr[j + 1][space]) {
        //             int[] temp = arr[j];
        //             arr[j] = arr[j + 1];
        //             arr[j + 1] = temp;
        //         }
        //     }
        // }
    }

    public static int[][] getSubArray(int[][] arr, int start, int end) {
        int[][] subArray = new int[end - start][arr[0].length];
        for (int i = start; i < end; i++) {
            System.arraycopy(arr[i], 0, subArray[i - start], 0, arr[0].length);
        }
        return subArray;
    }
}