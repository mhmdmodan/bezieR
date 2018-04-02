import javax.swing.*;

public class BezierDraw {
    public void drawer() {
        JFrame f = new JFrame("Draw a Red Line");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(800, 800);
        f.setLocation(300, 300);
        f.setResizable(false);
        JPanel p = new BezierPanel();
        f.add(p);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new BezierDraw().drawer();
    }
}
