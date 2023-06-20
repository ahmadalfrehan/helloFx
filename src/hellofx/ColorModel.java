package hellofx;

public class ColorModel {
    private Long blue;
    private Long red;
    private Long green;

    public ColorModel(Long blue, Long red, Long green) {
        this.blue = blue;
        this.red = red;
        this.green = green;
    }

    public double calculateDistance(ColorModel other) {
        Long blueDiff = this.blue - other.blue;
        Long redDiff = this.red - other.red;
        Long greenDiff = this.green - other.green;
        return Math.sqrt(blueDiff * blueDiff + redDiff * redDiff + greenDiff * greenDiff);
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "blue=" + blue +
                ", red=" + red +
                ", green=" + green +
                '}';
    }
}
