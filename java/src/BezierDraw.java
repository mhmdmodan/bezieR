import javax.swing.*;

public class BezierDraw {
    private int frameSize;
    private BezierPanel p;

    public BezierDraw(int frameSize) {
        this.frameSize = frameSize;
        drawer();
    }

    public void drawer() {
        JFrame f = new JFrame("Draw your curves");
        f.setSize(frameSize, frameSize);
        f.setLocation(100, 100);
        f.setResizable(false);
        p = new BezierPanel();
        f.add(p);
        f.setVisible(true);
    }

    public double[][] getPoints() {
        return p.getPoints(frameSize);
    }

    public static void main(String[] args) {
        new BezierDraw(800);
    }
}
