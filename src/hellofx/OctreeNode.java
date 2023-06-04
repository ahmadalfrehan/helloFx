package hellofx;


import java.awt.Color;
import java.util.Arrays;

public class OctreeNode {

    private final int depth;
    private int count;
    private Color color;
    private Color representativeColor;
    private OctreeNode[] children;

    public OctreeNode(int depth) {
        this.depth = depth;
        this.count = 0;
        this.color = null;
        this.representativeColor = null;
        this.children = null;
    }

    public void insertColor(Color color, int maxDepth) {
        if (depth == maxDepth) {
            this.count++;
            this.color = blend(this.color, color, this.count);
            return;
        }

        if (children == null) {
            children = new OctreeNode[8];
        }

        int idx = getIndexForColor(color);
        if (children[idx] == null) {
            children[idx] = new OctreeNode(depth + 1);
        }

        children[idx].insertColor(color, maxDepth);
    }

    public void calculateRepresentativeColors() {
        if (children == null) {
            representativeColor = color;
            return;
        }

        for (OctreeNode child : children) {
            if (child != null) {
                child.calculateRepresentativeColors();
            }
        }
    }

    public Color getNearestColor(Color target) {
        if (children == null) {
            return representativeColor;
        }

        int idx = getIndexForColor(target);
        return children[idx] != null ? children[idx].getNearestColor(target) : representativeColor;
    }

    private int getIndexForColor(Color color) {
        int r = (color.getRed() > 127) ? 1 : 0;
        int g = (color.getGreen() > 127) ? 1 : 0;
        int b = (color.getBlue() > 127) ? 1 : 0;
        return (r << 2) | (g << 1) | b;
    }

    private Color blend(Color c1, Color c2, int count) {
        if (c1 == null) {
            return c2;
        }

        int r = (c1.getRed() * (count - 1) + c2.getRed()) / count;
        int g = (c1.getGreen() * (count - 1) + c2.getGreen()) / count;
        int b = (c1.getBlue() * (count - 1) + c2.getBlue()) / count;
        return new Color(r, g, b);
    }
}