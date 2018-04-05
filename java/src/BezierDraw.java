import javax.swing.*;

public class BezierDraw {
    public void drawer() {
        JFrame f = new JFrame("Draw your curves");
        f.setSize(800, 800);
        f.setLocation(100, 100);
        f.setResizable(false);
        JPanel p = new BezierPanel();
        f.add(p);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new BezierDraw().drawer();
    }
}
