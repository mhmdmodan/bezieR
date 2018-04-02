import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.util.List;

public class CurveDrawer {
    public static void draw(List<BezierPoint> points, Graphics g, boolean compeleted) {
        if (points.size() == 1) return;
        Graphics2D newG = (Graphics2D) g;
        for (int i = 1; i < points.size(); i++) {
            CubicCurve2D currentCurve = new CubicCurve2D.Double(
                    points.get(i-1).x,
                    points.get(i-1).y,
                    points.get(i-1).fx,
                    points.get(i-1).fy,
                    points.get(i).bx,
                    points.get(i).by,
                    points.get(i).x,
                    points.get(i).y);
            newG.draw(currentCurve);
        }
        if (compeleted) {
            int last = points.size()-1;
            CubicCurve2D lastCurve = new CubicCurve2D.Double(
                    points.get(last).x,
                    points.get(last).y,
                    points.get(last).fx,
                    points.get(last).fy,
                    points.get(0).bx,
                    points.get(0).by,
                    points.get(0).x,
                    points.get(0).y);
            newG.draw(lastCurve);
        }
    }
}
