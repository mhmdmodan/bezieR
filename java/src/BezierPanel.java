import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BezierPanel extends JPanel {
    private List<BezierPoint> points = new ArrayList<>();
    private BezierPoint currentPoint;
    private boolean distLock, lock, completed, canComplete, canDelete;
    public static final Color BEZ_PT_COL = Color.blue;
    public static final Color BEZ_LINE_COL = Color.black;

    public BezierPanel() {
        completed = false;
        distLock = true;
        lock = true;
        canComplete = false;
        canDelete = false;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < points.size(); i++) {
                    if (points.get(i).clickedOn(e.getPoint(), lock)) {
                        if (canDelete) {
                            points.remove(i);
                            repaint();
                            return;
                        }
                        if (canComplete && points.get(i) == points.get(0)) {
                            completed = true;
                        }
                        distLock = false;
                        currentPoint = points.get(i);
                        repaint();
                        return;
                    }
                }
                if (completed) return;
                distLock = true;
                lock = true;
                BezierPoint newPoint = new BezierPoint(e.getPoint());
                points.add(newPoint);
                currentPoint = newPoint;
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                currentPoint = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentPoint != null) {
                    currentPoint.moveMe(e.getPoint(), lock, distLock);
                    repaint();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 17) lock = false;
                if (e.getKeyCode() == 16) canComplete = true;
                if (e.getKeyCode() == 18) canDelete = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 17) lock = true;
                if (e.getKeyCode() == 16) canComplete = false;
                if (e.getKeyCode() == 18) canDelete = false;
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BEZ_PT_COL);
        for (BezierPoint point:points) point.draw(g);
        g.setColor(BEZ_LINE_COL);
        CurveDrawer.draw(points, g, completed);
    }
}
