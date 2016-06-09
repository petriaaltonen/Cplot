/*
 * PlotPanel.java
 * 29.3.2014
 * Petri Aaltonen
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Formatter;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class PlotPanel extends JPanel {

    private BufferedImage backgroundImage = null;
    protected BufferedImage plotImage = null;

    private boolean enabledToolTip = true;
    private boolean enabledBox = true;
    private boolean enabledCrosshair = true;

    private int backgroundWidth = 0;
    private int backgroundHeight = 0;
    private int plotWidth = 0;
    private int plotHeight = 0;
    private int plotLeft = 0;
    private int plotTop = 0;
    private int plotRight = 0;
    private int plotBottom = 0;

    private static final int DELTA_X1_BOX = 70;
    private static final int DELTA_Y1_BOX = 20;
    private static final int DELTA_X2_BOX = 70;
    private static final int DELTA_Y2_BOX = 50;

    private static final int BOX_CLEARANCE = 10;
    private static final int TIC_LEN = 8;

    private static final int TOOLTIP_CLEARANCE = 10;
    private static final int TEXT_CLEARANCE = 5;

    private int numberOfTics = 7;

    public enum DraggingTool {SCAN, ZOOM}
    private DraggingTool draggingTool = DraggingTool.SCAN;

    private int mouseXPos = 0;
    private int mouseYPos = 0;
    private int zoomX0, zoomY0;
    private int zoomX1, zoomY1;
    private Complex scanZ0 = null;
    private int scanX0, scanY0;
    private int scanX1, scanY1;
    private boolean isDragging = false;
    private boolean isComputing = false;

    private Plot plot = null;
    private MainWindow mainWindow = null;

    //
    // Constructor is used to set up event handlers.
    //
    public PlotPanel(MainWindow mainRef, Plot plotRef) {

        super();
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        plot = plotRef;
        mainWindow = mainRef;

        // computePlotSize();
        // plot.resize(plotWidth, plotHeight);

        //
        // Handle resize events.
        //
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });

        //
        // Handle mouse motion events.
        //
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isComputing) return;
                else if (draggingTool == DraggingTool.ZOOM) {
                    if (!isDragging) return;

                    zoomX1 = cropX(e.getX());
                    zoomY1 = cropY(e.getY());
                    mouseXPos = e.getX();
                    mouseYPos = e.getY();

                    repaint();
                }
                else if (draggingTool == DraggingTool.SCAN) {
                    if (!isDragging) return;

                    scanX1 = cropX(e.getX());
                    scanY1 = cropY(e.getY());
                    int deltaX = scanX1 - scanX0;
                    int deltaY = scanY1 - scanY0;
                    scanX0 = scanX1;
                    scanY0 = scanY1;

                    plot.scan(-deltaX, -deltaY);

                    try { updateBackgroundImage(); }
                    catch (PlotException ex) { mainWindow.bailOut(ex); }

                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseXPos = e.getX();
                mouseYPos = e.getY();
                repaint();
            }
        });

        //
        // Handle mouse button events.
        //
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isComputing) return;
                else if (draggingTool == DraggingTool.ZOOM) {
                    if (e.getX() >= plotLeft && e.getX() <= plotRight
                            && e.getY() >= plotTop && e.getY() <= plotBottom) {
                        zoomX0 = e.getX();
                        zoomY0 = e.getY();
                        isDragging = true;
                    } else isDragging = false;
                }
                else if (draggingTool == DraggingTool.SCAN) {
                    if (e.getX() >= plotLeft && e.getX() <= plotRight
                            && e.getY() >= plotTop && e.getY() <= plotBottom) {
                        scanX0 = e.getX();
                        scanY0 = e.getY();
                        scanZ0 = plot.getCoordinates().getComplexCoordinates(scanX0 - plotLeft, scanY0 - plotTop);
                        isDragging = true;
                    } else isDragging = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isComputing) return;
                else if (draggingTool == DraggingTool.ZOOM) {
                    if (!isDragging)
                        return;

                    zoomX1 = cropX(e.getX());
                    zoomY1 = cropY(e.getY());

                    int x1 = Math.min(zoomX0, zoomX1);
                    int x2 = Math.max(zoomX0, zoomX1);
                    int y1 = Math.min(zoomY0, zoomY1);
                    int y2 = Math.max(zoomY0, zoomY1);

                    x1 -= plotLeft;
                    x2 -= plotLeft;
                    y1 -= plotTop;
                    y2 -= plotTop;

                    isDragging = false;

                    try {
                        plot.zoom(x1, y1, x2, y2);
                        updateBackgroundImage();
                        repaint();
                    } catch (PlotException ex) {
                        mainWindow.bailOut(ex);
                    }
                }
                else if (draggingTool == DraggingTool.SCAN) {
                    isDragging = false;
                    try {
                        plot.postScanUpdate();
                        updateBackgroundImage();
                    }
                    catch (PlotException ex) { mainWindow.bailOut(ex); }
                    repaint();
                }
            }
        });

        // Add callbacks to handle locking the tools and repainting the component.
        plotRef.addStartCallback(new StartCallback() {
            @Override
            public void callback() {
                isComputing = true;
                repaint();
            }
        });

        plotRef.addDoneCallback(new DoneCallback() {
            @Override
            public void callback() {
                isComputing = false;
                repaint();
            }
        });
    }

    public void enableBox(boolean val) { enabledBox = val; }
    public void enableCrosshair(boolean val) { enabledCrosshair = val; }
    public void enableToolTip(boolean val) { enabledToolTip = val; }

    public boolean isBoxEnabled() { return enabledBox; }
    public boolean isCrosshairEnabled() { return enabledCrosshair; }
    public boolean isToolTipEnabled() { return enabledToolTip; }

    public DraggingTool getDraggingTool() { return draggingTool; }
    public void setDraggingTool(DraggingTool tool) { draggingTool = tool; }

    //
    // Compute plot size given image panel width and height.
    //
    private void computePlotSize() {

        backgroundWidth = getWidth();
        backgroundHeight = getHeight();

        int deltaX1 = 0;
        int deltaX2 = 0;
        int deltaY1 = 0;
        int deltaY2 = 0;

        if (enabledBox) {
            deltaX1 += DELTA_X1_BOX;
            deltaX2 += DELTA_X2_BOX;
            deltaY1 += DELTA_Y1_BOX;
            deltaY2 += DELTA_Y2_BOX;
        }

        int w = backgroundWidth - deltaX1 - deltaX2;
        int h = backgroundHeight - deltaY1 - deltaY2;
        int size = Math.min(w, h);
        if (size < 0) size = 0;
        w = size;
        h = size;
        plotLeft = (backgroundWidth - size) / 2;
        plotTop = (backgroundHeight - size) / 2;
        plotRight = plotLeft + size;
        plotBottom = plotTop + size;
        plotWidth = size;
        plotHeight = size;
    }

    //
    // Crop x component inside the plot.
    //
    private int cropX(int x) {
        if (x < plotLeft) return plotLeft;
        if (x >= plotRight) return plotRight - 1;
        return x;
    }

    //
    // Crop y component inside the plot.
    //
    private int cropY(int y) {
        if (y < plotTop) return plotTop;
        if (y >= plotBottom) return plotBottom - 1;
        return y;
    }

    //
    // Update the plot.
    //
    public void resize() {
        try {
            computePlotSize();
            if (plotWidth == 0) {
                backgroundImage = null;
                plotImage = null;
            }
            else {
                plot.resize(plotWidth, plotHeight);
                updateBackgroundImage();
            }
        }
        catch (PlotException e) { mainWindow.bailOut(e); }
    }

    //
    // Draw crosshair.
    //
    private void drawCrosshair(Graphics g) {

        // If the cursor is positioned outside the plot do not draw the crosshair.
        if (!(mouseXPos >= plotLeft && mouseXPos <= plotRight
                && mouseYPos >= plotTop && mouseYPos <= plotBottom)) return;

        g.setColor(new Color(0, 0, 0, 255));

        int y1 = plotTop;
        int y2 = plotBottom;
        int x1 = plotLeft;
        int x2 = plotRight;
        if (enabledBox) {
            x1 -= BOX_CLEARANCE;
            x2 += BOX_CLEARANCE;
            y1 -= BOX_CLEARANCE;
            y2 += BOX_CLEARANCE;
        }

        int xPos = 0, yPos = 0;

        if (isDragging && draggingTool == DraggingTool.SCAN) {
            Point point = plot.getCoordinates().getMatrixIndex(scanZ0);
            if (point != null) {
                xPos = point.x + plotLeft;
                yPos = point.y + plotTop;
            }
            else {
                return;
            }
        }
        else {
            xPos = mouseXPos;
            yPos = mouseYPos;
        }

        g.drawLine(xPos, y1, xPos, y2);
        g.drawLine(x1, yPos, x2, yPos);
    }

    //
    // Draw a translucent zoom-box.
    //
    private void drawZoomBox(Graphics g) {
        int x1 = Math.min(zoomX0, zoomX1);
        int x2 = Math.max(zoomX0, zoomX1);
        int y1 = Math.min(zoomY0, zoomY1);
        int y2 = Math.max(zoomY0, zoomY1);
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    //
    // Draw a tooltip box.
    //
    private void drawToolTip(Graphics g) {

        // If the cursor is positioned outside the plot do not draw tooltip.
        if (!(mouseXPos >= plotLeft && mouseXPos < plotRight
                && mouseYPos >= plotTop && mouseYPos < plotBottom)) return;

        int xPos, yPos;

        if (isDragging && draggingTool == DraggingTool.SCAN) {
            Point point = plot.getCoordinates().getMatrixIndex(scanZ0);
            if (point != null) {
                xPos = point.x + plotLeft;
                yPos = point.y + plotTop;
            }
            else {
                return;
            }
        }
        else {
            xPos = mouseXPos;
            yPos = mouseYPos;
        }

        // Construct the text.
        Complex z = plot.getCoordinates().getComplexCoordinates(xPos - plotLeft, yPos - plotTop);
        Complex fz = plot.getComplexValue(xPos - plotLeft, yPos - plotTop);

        String [] str = new String[4];
        StringBuilder strBuilder = new StringBuilder();
        Formatter formatter = new Formatter(strBuilder);

        formatter.format("%.5f", z.x);
        str[0] = new String("Re(z): " + strBuilder.toString());
        strBuilder.delete(0, strBuilder.length());

        formatter.format("%.5f", z.y);
        str[1] = new String("Im(z): " + strBuilder.toString());
        strBuilder.delete(0, strBuilder.length());

        formatter.format("%.5f", fz.x);
        str[2] = new String("Re(w): " + strBuilder.toString());
        strBuilder.delete(0, strBuilder.length());

        formatter.format("%.5f", fz.y);
        str[3] = new String("Im(w): " + strBuilder.toString());
        formatter.close();

        // Set up the font.
        Font font = new Font("Monospaced", Font.PLAIN, 10);
        FontMetrics metric = g.getFontMetrics(font);
        int width = metric.stringWidth(str[0]) + 2*TEXT_CLEARANCE;
        for (int j = 1; j < 4; j++)
            if (metric.stringWidth(str[j]) > width)
                width = metric.stringWidth(str[j]);
        int height = 4*metric.getHeight() + 2*TEXT_CLEARANCE;

        // Determine tooltip location.
        int x1 = xPos + TOOLTIP_CLEARANCE;
        if (x1 + width > backgroundWidth)
            x1 = xPos - TOOLTIP_CLEARANCE - width;
        int y1 = yPos + TOOLTIP_CLEARANCE;
        if (y1 + height > backgroundHeight)
            y1 = yPos - TOOLTIP_CLEARANCE - height;

        // Draw the tooltip box.
        g.setColor(new Color(255, 255, 0, 125));
        g.fillRect(x1, y1, width, height);

        // Print the text.
        g.setFont(font);
        g.setColor(new Color(0, 0, 0, 255));
        for (int j = 0; j < 4; j++) {
            g.drawString(str[j], x1 + TEXT_CLEARANCE,
                    y1 + TEXT_CLEARANCE + (j+1)*metric.getHeight());
        }
    }

    //
    // NOTE: We need to override paintComponent NOT paint.
    //
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }
        if (!isComputing) {
            if (plot.getImage() != null) {
                g.drawImage(
                        plot.getImage(),
                        plotLeft,
                        plotTop,
                        plotLeft + plot.getCoordinates().getViewportWidth(),
                        plotTop + plot.getCoordinates().getViewportHeight(),
                        plot.getCoordinates().getLeft(),
                        plot.getCoordinates().getTop(),
                        plot.getCoordinates().getRight(),
                        plot.getCoordinates().getBottom(),
                        null);
            }
            if (enabledCrosshair) drawCrosshair(g);
            if (isDragging && draggingTool == DraggingTool.ZOOM) drawZoomBox(g);
            if (enabledToolTip) drawToolTip(g);
        }
    }

    // 
    // Take a floor of a floating point number at the p:th decimal. Eg.
    // floorDecimal(1.234, -1) = 1.2
    // floorDecimal(0.123, 0) = 0
    // floorDecimal(-1.234, -1) = -1.3
    //
    static private double floorDecimal(double x, int p) {
        double tmp = Math.pow(10.0, -p);
        return x - Math.pow(10.0, p)*(tmp*x - Math.floor(tmp*x));
    }

    // 
    // Take a ceiling of a floating point number at the p:th decimal. Eg.
    // floorDecimal(1.234, -1) = 1.3
    // floorDecimal(0.123, 0) = 1
    // floorDecimal(-1.234, -1) = -1.2
    //
    static private double ceilDecimal(double x, int p) {
        double tmp = Math.pow(10.0, -p);
        return x - Math.pow(10.0, p)*(tmp*x - Math.ceil(tmp*x));
    }

    //
    // Compute tic locations.
    //
    private double [] computeTicLocations(double a, double b) {

        assert a < b : "a must be less than b";

        double delta = b - a;
        double h = delta / (numberOfTics - 1);
        int p = (int)Math.floor(Math.log10(h));
        h = ceilDecimal(h, p);
        double a0 = ceilDecimal(a, p);

        int n = (int)Math.floor((b - a0) / h) + 1;

        // Determine the best centering of tick marks along the axis.
        double min_val = Math.abs(a0 - a - (b - (a0 + (n-1)*h)));
        double min_a0 = a0;
        double new_a0 = a0 + Math.pow(10.0, p);

        while (true) {
            double val = Math.abs(new_a0 - a - (b - (new_a0 + (n-1)*h)));
            if (val < min_val) {
                min_val = val;
                min_a0 = new_a0;
            }
            else break;
            new_a0 += Math.pow(10.0, p);
        }

        a0 = min_a0;

        double [] positions = new double[n];
        for (int i = 0; i < n; i++)
            positions[i] = a0 + i*h;

        return positions;
    }

    //
    // A class used to carry around tic-mark data.
    //
    private static class TicData {
        public int [] xpos = null;
        public int [] ypos = null;
        public String [] xlabel = null;
        public String [] ylabel = null;
        public String xmod = null;
        public String ymod = null;
    }

    //
    // Determine tic locations and labels.
    //
    private void computeTics(TicData data) {

        double [] xtics;
        double [] ytics;

        if (isDragging && draggingTool == DraggingTool.SCAN) {
            xtics = new double[2];
            ytics = new double[2];
            xtics[0] = plot.getCoordinates().getXmin();
            xtics[1] = plot.getCoordinates().getXmax();
            ytics[0] = plot.getCoordinates().getYmin();
            ytics[1] = plot.getCoordinates().getYmax();
        }
        else {
            xtics = computeTicLocations(plot.getCoordinates().getXmin(), plot.getCoordinates().getXmax());
            ytics = computeTicLocations(plot.getCoordinates().getYmin(), plot.getCoordinates().getYmax());
        }

        int nx = xtics.length;
        int ny = ytics.length;

        data.xpos = new int[nx];
        data.ypos = new int[ny];
        data.xlabel = new String[nx];
        data.ylabel = new String[ny];
        StringBuilder strBuilder = new StringBuilder();
        Formatter formatter = new Formatter(strBuilder);
        data.xmod = null;
        data.ymod = null;

        String formatStringX = "%." + (Utils.significantDecimalDigits(xtics[0]) + 1) + "f";
        String formatStringY = "%." + (Utils.significantDecimalDigits(ytics[0]) + 1) + "f";

        for (int i = 0; i < nx; i++) {
            Point p = plot.getCoordinates().getMatrixIndex(new Complex(xtics[i], plot.getCoordinates().getYmax()));
            data.xpos[i] = p.x;
            formatter.format(formatStringX, xtics[i]);
            data.xlabel[i] = strBuilder.toString();
            strBuilder.delete(0, strBuilder.length());
        }
        for (int i = 0; i < ny; i++) {
            Point p = plot.getCoordinates().getMatrixIndex(new Complex(plot.getCoordinates().getXmax(), ytics[i]));
            data.ypos[i] = p.y;
            formatter.format(formatStringY, ytics[i]);
            data.ylabel[i] = strBuilder.toString();
            strBuilder.delete(0, strBuilder.length());
        }

        formatter.close();
    }

    //
    // Draw a box on the backgroundImage.
    //
    private void drawBox() {

        assert backgroundImage != null;

        Graphics2D g = (Graphics2D)backgroundImage.getGraphics();
        g.setColor(new Color(0, 0, 0, 255));

        // Draw the box around the plot.
        g.drawLine(	plotLeft - BOX_CLEARANCE,
                plotTop - BOX_CLEARANCE,
                plotLeft - BOX_CLEARANCE,
                plotBottom + BOX_CLEARANCE);
        g. drawLine(	plotLeft - BOX_CLEARANCE,
                plotTop - BOX_CLEARANCE,
                plotRight + BOX_CLEARANCE,
                plotTop - BOX_CLEARANCE);
        g.drawLine(	plotRight + BOX_CLEARANCE,
                plotTop - BOX_CLEARANCE,
                plotRight + BOX_CLEARANCE,
                plotBottom + BOX_CLEARANCE);
        g.drawLine(	plotLeft - BOX_CLEARANCE,
                plotBottom + BOX_CLEARANCE,
                plotRight + BOX_CLEARANCE,
                plotBottom + BOX_CLEARANCE);

        TicData tics = new TicData();
        computeTics(tics);

        Font font = new Font("Monospaced", Font.PLAIN, 10);
        FontMetrics metric = g.getFontMetrics(font);
        g.setFont(font);
        g.setStroke(new BasicStroke(2.5f));

        // Draw the tic marks.
        for (int i = 0; i < tics.xpos.length; i++) {
            int x = plotLeft + tics.xpos[i];
            g.drawLine(x, plotBottom + BOX_CLEARANCE - TIC_LEN/2,
                    x, plotBottom + BOX_CLEARANCE + TIC_LEN/2);
            g.drawString(tics.xlabel[i], x - metric.stringWidth(tics.xlabel[i])/2,
                    plotBottom + BOX_CLEARANCE + TIC_LEN + metric.getHeight());
        }

        for (int i = 0; i < tics.ypos.length; i++) {
            int y = plotTop + tics.ypos[i];
            g.drawLine(plotLeft - BOX_CLEARANCE - TIC_LEN/2, y,
                    plotLeft - BOX_CLEARANCE + TIC_LEN/2, y);
            g.drawString(tics.ylabel[i], plotLeft - BOX_CLEARANCE - TIC_LEN
                    - metric.stringWidth(tics.ylabel[i]) - 3, y + metric.getAscent()/2);
        }
    }

    //
    // Update the backgroundImage.
    //
    void updateBackgroundImage() throws PlotException {

        if (backgroundImage != null) backgroundImage = null;
        if (backgroundWidth == 0 || backgroundHeight == 0) return;

        backgroundImage = new BufferedImage(backgroundWidth, backgroundHeight,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = backgroundImage.createGraphics();
        g.setColor(new Color(255, 255, 255, 255));
        g.fill(new Rectangle2D.Double(0.0, 0.0, backgroundWidth, backgroundHeight));
        if (enabledBox) drawBox();
    }

}
