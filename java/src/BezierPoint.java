import java.awt.*;

public class BezierPoint extends Point {
    public int fx, fy, bx, by;
    public static final int DIAMETER = 8;
    public static final int BUFFER = 16;
    private int currentPt;

    public BezierPoint(Point p) {
        super(p);
        fx = p.x;
        fy = p.y;
        bx = p.x;
        by = p.y;
        currentPt = 3;
    }

    public BezierPoint(double x,
                       double y,
                       double fx,
                       double fy,
                       double bx,
                       double by,
                       int frameSize) {

    }

    public BezierPoint() {
        this(new Point(0,0));
    }

    public boolean clickedOn(Point point, boolean lock) {
        if (distance(point, new Point(x,y)) < BUFFER && lock) {
            currentPt = 2;
            return true;
        } else if (distance(point, new Point(bx,by)) < BUFFER) {
            currentPt = 1;
            return true;
        } else if (distance(point, new Point(fx,fy)) < BUFFER) {
            currentPt = 3;
            return  true;
        }
        currentPt = 2;
        return false;
    }

    public void moveMe(Point point, boolean locked, boolean distLock) {
        switch (currentPt) {
            case 1:
                setBackwardHandle(point, locked, distLock);
                break;
            case 2:
                setLocation(point);
                break;
            case 3:
                setForwardHandle(point, locked, distLock);
                break;
        }
    }

    private static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x-b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    @Override
    public void setLocation(Point p) {
        fx += p.x - x;
        fy += p.y - y;
        bx += p.x - x;
        by += p.y - y;
        setLocation(p.x, p.y);
    }

    public void setForwardHandle(Point pt, boolean lock, boolean distLock) {
        fx = pt.x;
        fy = pt.y;

        if (lock) {
            Point other = extendOpposite(fx, fy, bx, by, distLock);
            bx = other.x;
            by = other.y;
        }
    }

    private Point extendOpposite(int x, int y, int opx, int opy, boolean distLock) {
        double dist;
        if (distLock) {
            dist = Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(y - this.y, 2));
        } else {
            dist = Math.sqrt(Math.pow(opx-this.x, 2) + Math.pow(opy - this.y, 2));
        }
        double vx = this.x - x;
        double vy = this.y - y;
        double vnorm = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
        vx = vx/vnorm;
        vy = vy/vnorm;
        return new Point((int) Math.round(this.x + dist * vx), (int) Math.round(this.y + dist*vy));
    }

    public void setBackwardHandle(Point pt, boolean lock, boolean distLock) {
        bx = pt.x;
        by = pt.y;

        if (lock) {
            Point other = extendOpposite(bx, by, fx, fy, distLock);
            fx = other.x;
            fy = other.y;
        }
    }

    public void draw(Graphics g) {
        g.drawLine(bx, by, x, y);
        g.drawLine(x, y, fx, fy);
        g.drawOval(fx - DIAMETER/2, fy - DIAMETER/2, DIAMETER, DIAMETER);
        g.fillOval(x - DIAMETER/2, y - DIAMETER/2, DIAMETER, DIAMETER);
        g.drawOval(bx - DIAMETER/2, by - DIAMETER/2, DIAMETER, DIAMETER);
    }

    @Override
    public String toString() {
        return "BezierPoint{" +
                "fx=" + fx +
                ", fy=" + fy +
                ", bx=" + bx +
                ", by=" + by +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public BezierPoint clone() {
        BezierPoint toReturn = new BezierPoint();
        toReturn.x = this.x;
        toReturn.y = this.y;
        toReturn.fx = this.fx;
        toReturn.fy = this.fy;
        toReturn.bx = this.bx;
        toReturn.by = this.by;
        toReturn.currentPt = this.currentPt;
        return toReturn;
    }
}
