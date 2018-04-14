import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class BezierPanel extends JPanel {
    private List<BezierPoint> points = new ArrayList<>();
    private BezierPoint currentPoint;
    private boolean distLock, lock, completed, canDelete, hidden;
    private int canComplete;
    public static final Color BEZ_PT_COL = new Color(244,122,0);
    public static final Color BEZ_LINE_COL = Color.black;
    public static final int maxHistory = 200;

    private Deque<List<BezierPoint>> history = new ArrayDeque<>();
    private Deque<Boolean> completedHistory = new ArrayDeque<>();

    public BezierPanel() {
        hidden = false;
        completed = false;
        distLock = true;
        lock = true;
        canComplete = 0;
        canDelete = false;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                appendHistory();
                for (int i = 0; i < points.size(); i++) {
                    if (points.get(i).clickedOn(e.getPoint(), lock)) {
                        if (canDelete) {
                            points.remove(i);
                            repaint();
                            return;
                        }
                        if (points.get(i) == points.get(0)) {
                            canComplete++;
                            if (canComplete > 1) completed = true;
                        }
                        distLock = false;
                        currentPoint = points.get(i);
                        repaint();
                        return;
                    }
                }
                canComplete = 0;
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
                if (e.getKeyCode() == 18) canDelete = true;
                if (e.getKeyCode() == 90 && !lock && !history.isEmpty()) {
                    points = history.pop();
                    completed = completedHistory.pop();
                    repaint();
                }
                if (e.getKeyCode() == 83) save();
                if (e.getKeyCode() == 79) load();
                if (e.getKeyCode() == 72) {
                    hidden = !hidden;
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 17) lock = true;
                if (e.getKeyCode() == 18) canDelete = false;
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void appendHistory() {
        List<BezierPoint> pointsClone = new ArrayList<>(points.size());
        for (BezierPoint point: points) pointsClone.add(point.clone());
        history.push(pointsClone);
        completedHistory.push(completed);
        if (history.size() > maxHistory) {
            history.pollLast();
            completedHistory.pollLast();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BEZ_PT_COL);
        if (!hidden) for (BezierPoint point:points) point.draw(g);
        g.setColor(BEZ_LINE_COL);
        CurveDrawer.draw(points, g, completed);
    }

    public double[][] getPoints(int frameSize) {
        double[][] toReturn;
        if (completed) {
            toReturn = new double[points.size()][8];
        } else {
            toReturn = new double[points.size()-1][8];
        }
        for (int i=1; i < points.size(); i++) {
            toReturn[i-1] = new double[] {points.get(i-1).x,
                    frameSize - points.get(i-1).y,
                    points.get(i-1).fx,
                    frameSize - points.get(i-1).fy,
                    points.get(i).bx,
                    frameSize - points.get(i).by,
                    points.get(i).x,
                    frameSize - points.get(i).y};
        }
        if (completed) {
            int last = points.size()-1;
            toReturn[points.size()-1] = new double[] {points.get(last).x,
                    frameSize - points.get(last).y,
                    points.get(last).fx,
                    frameSize - points.get(last).fy,
                    points.get(0).bx,
                    frameSize - points.get(0).by,
                    points.get(0).x,
                    frameSize - points.get(0).y};
        }
        return toReturn;
    }

    public void save() {
        try {
            FileOutputStream fos = new FileOutputStream("points.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Pair<Boolean, List<BezierPoint>> pair = new Pair<>(completed, points);
            oos.writeObject(pair);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream fis = new FileInputStream("points.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Pair<Boolean, List<BezierPoint>> pair = (Pair<Boolean, List<BezierPoint>>) ois.readObject();
            points = pair.getValue();
            completed = pair.getKey();
            ois.close();
            repaint();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
