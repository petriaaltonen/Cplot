/*
 * PlotCoordinates.java
 * 10.9.2014
 * Petri Aaltonen
 */

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author petri
 */
public class PlotCoordinates {
    private static final double DEF_XMIN = -1.0;
    private static final double DEF_XMAX = 1.0;
    private static final double DEF_YMIN = -1.0;
    private static final double DEF_YMAX = 1.0;

    private double deltaX;
    private double deltaY;

    private Rectangle rectViewport = null;
    private Rectangle2D.Double rectViewportCmplx = null;
    private Rectangle rectMatrix = null;
    private Rectangle2D rectMatrixCmplx = null;

    public PlotCoordinates(int viewPortXsize, int viewPortYsize) {
        rectMatrix = new Rectangle(
                0,
                0,
                3 * viewPortXsize,
                3 * viewPortYsize);

        rectViewport = new Rectangle(
                viewPortXsize,
                viewPortYsize,
                2*viewPortXsize,
                2*viewPortYsize);

        rectMatrixCmplx = new Rectangle2D.Double(
                DEF_XMIN - (DEF_XMAX - DEF_XMIN),
                DEF_YMIN - (DEF_YMAX - DEF_YMIN),
                3.0 * (DEF_XMAX - DEF_XMIN),
                3.0 * (DEF_YMAX - DEF_YMIN));

        rectViewportCmplx = new Rectangle2D.Double(
                DEF_XMIN,
                DEF_YMIN,
                DEF_XMAX - DEF_XMIN,
                DEF_YMAX - DEF_YMIN);

        computeDeltaValues();
    }

    private void computeDeltaValues() {
        deltaX = rectViewportCmplx.width / (double)(rectViewport.width - 1);
        deltaY = rectViewportCmplx.height / (double)(rectViewport.height - 1);
    }

    Complex matToCmplx(int x, int y) {
        if (!rectMatrix.contains(x, y)) return null;
        Complex z = new Complex();
        z.x = rectMatrixCmplx.getMinX() + deltaX*x;
        z.y = rectMatrixCmplx.getMinY() + deltaY*(rectMatrix.height - y - 1);
        return z;
    }

    Point cmplxToMat(Complex z) {
        Point p = new Point();
        p.x = (int)((z.x - rectMatrixCmplx.getMinX()) / deltaX);
        p.y = (int)((double)(rectMatrix.height - 1)
                - (z.y - rectMatrixCmplx.getMinY())/deltaY);
        if (p.x == -1) p.x = 0;
        if (p.y == rectMatrix.width) p.x = rectMatrix.width - 1;
        if (p.y == -1) p.y = 0;
        if (p.y == rectMatrix.height) p.y = rectMatrix.height - 1;
        return p;
    }

    public double getXmin() { return rectViewportCmplx.getMinX(); }
    public double getXmax() { return rectViewportCmplx.getMaxX(); }
    public double getYmin() { return rectViewportCmplx.getMinY(); }
    public double getYmax() { return rectViewportCmplx.getMaxY(); }
    public int getViewportWidth() { return rectViewport.width; }
    public int getViewportHeight() { return rectViewport.height; }
    public int getMatrixWidth() { return rectMatrix.width; }
    public int getMatrixHeight() { return rectMatrix.height; }
    public int getLeft() { return rectViewport.x; }
    public int getTop() { return rectViewport.y; }
    public int getRight() { return (rectViewport.x + rectViewport.width); }
    public int getBottom() { return (rectViewport.y + rectViewport.height); }

    /**
     * Given a point (x,y) inside the viewport of the matrix return the complex
     * coordinates of the point (x,y).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the complex coordinates
     */
    public Complex getComplexCoordinates(int x, int y) {
        x += rectViewport.x;
        y += rectViewport.y;
        if (!rectViewport.contains(x, y)) return null;
        return matToCmplx(x, y);
    }

    /**
     * Given complex coordinates z inside the viewport of the matrix return the
     * the point (x,y) representing the integer coordinates of a point inside the
     * matrix viewport.
     *
     * @param z the complex coordinates 
     * @return the integer coordinates
     */
    public Point getMatrixIndex(Complex z) {
        Complex w = new Complex(z);
        Point p = cmplxToMat(w);
        p.x -= rectViewport.x;
        p.y -= rectViewport.y;
        return p;
    }

    /**
     * Resize the viewport matrix by giving the new width and height values.
     * However, we keep the complex viewport size intact which means the aspect
     * ratio of the plot is likely to change.
     *
     * @param width new viewport width
     * @param height new viewport height
     */
    public void resize(int width, int height) {
        assert width >= 0 : "width < 0";
        assert height >= 0 : "height < 0";

        if (width == 0 || height == 0) {
            rectMatrix = null;
            rectViewport = null;
            return;
        }

        double dx = (rectViewportCmplx.x - rectMatrixCmplx.getMinX())
                / rectMatrixCmplx.getWidth();
        double dy = (rectMatrixCmplx.getMaxY() - rectViewportCmplx.getMaxY())
                / rectMatrixCmplx.getHeight();

        rectMatrix = new Rectangle(0, 0, 3*width, 3*height);
        rectViewport = new Rectangle(
                (int)(dx*rectMatrix.width),
                (int)(dy*rectMatrix.height),
                width,
                height);

        computeDeltaValues();
    }

    /**
     * Reset the x and y-limits of the viewport.
     *
     * @param xmin viewport xmin
     * @param xmax viewport xmax
     * @param ymin viewport ymin
     * @param ymax viewport ymax
     * @throws PlotException Invalid arguments
     */
    public void setLimits(double xmin, double xmax, double ymin, double ymax)
            throws PlotException {

        if (xmin >= xmax) throw new PlotException("error: xmin >= xmax");
        if (ymin >= ymax) throw new PlotException("error: ymin >= ymax");

        rectViewportCmplx = new Rectangle2D.Double(
                xmin,
                ymin,
                xmax - xmin,
                ymax - ymin);

        rectMatrixCmplx = new Rectangle2D.Double(
                2*xmin - xmax,
                2*ymin - ymax,
                3.0*(xmax - xmin),
                3.0*(ymax - ymin));

        computeDeltaValues();
    }

    /**
     *
     * @param x1 upper left x coordinate
     * @param y1 upper left y coordinate
     * @param x2 lower right x coordinate
     * @param y2  lower right y coordinate
     */
    public void zoom(int x1, int y1, int x2, int y2) {
        Complex topLeft = getComplexCoordinates(x1, y1);
        Complex bottomRight = getComplexCoordinates(x2, y2);
        try { setLimits(topLeft.x, bottomRight.x, bottomRight.y, topLeft.y); }
        catch(PlotException e) { assert false : "Should be unreachable"; }
    }

    /**
     *
     * @param dx delta x in matrix coordinates
     * @param dy delta y in matrix coordinates
     */
    public void scan(int dx, int dy) {
        if (dx < 0)
            if (rectViewport.x + dx < rectMatrix.x)
                dx = rectViewport.x - rectMatrix.x;
        if (dx > 0)
            if (rectViewport.x + dx + rectViewport.width >= rectMatrix.width)
                dx = rectMatrix.width - 1 - (rectViewport.x + rectViewport.width);
        if (dy < 0)
            if (rectViewport.y + dy < rectMatrix.y)
                dy = rectViewport.y - rectMatrix.y;
        if (dy > 0)
            if (rectViewport.y + dy + rectViewport.height >= rectMatrix.height)
                dy = rectMatrix.height - 1 - (rectViewport.y + rectViewport.height);

        if (dx != 0 || dy != 0) {
            double dxCmplx = dx * deltaX;
            double dyCmplx = -dy * deltaY;

            rectViewport.setLocation(rectViewport.x + dx, rectViewport.y + dy);
            rectViewportCmplx.setRect(
                    rectViewportCmplx.x + dxCmplx,
                    rectViewportCmplx.y + dyCmplx,
                    rectViewportCmplx.width,
                    rectViewportCmplx.height);
        }
    }

    /**
     * After scanning the plot and releasing mouse button recompute the matrix.
     */
    public void postScanUpdate() {
        rectMatrixCmplx = new Rectangle2D.Double(
                rectViewportCmplx.x - rectViewportCmplx.width,
                rectViewportCmplx.y - rectViewportCmplx.height,
                3.0*rectViewportCmplx.width,
                3.0*rectViewportCmplx.height);
        rectViewport.setLocation(rectViewport.width, rectViewport.height);
    }

    /**
     * Reset the viewport by centering it inside the matrix.
     */
    public void reset() {
        rectViewport.setLocation(rectViewport.width, rectViewport.height);
    }

}
