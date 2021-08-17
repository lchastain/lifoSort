import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

// While several other areas of this app are trying to be oh-so dynamic, this one is not -
//   this class 'knows' that it holds four ColorBalls, no more and no less.  The non-dynamic
//   value of 4 is crucial to proper working.

public class TubeComponent extends JPanel implements MouseListener {
    static final int tubeWidth = 50;
    static final int tubeHeight = 198;
    static final Color theBackground = Color.BLACK;
    Color lineColor;

    public TubeComponent() {
        super();
        lineColor = Color.WHITE;
        addMouseListener(this);
        this.setSize(tubeWidth, tubeHeight);
        setLayout(new GridLayout(4, 1, 1, 0));
        setBackground(theBackground);

        // Make the tube appear to be empty, by filling it with four balls that are the same color as the background.
        super.add(new ColorBall(theBackground));
        super.add(new ColorBall(theBackground));
        super.add(new ColorBall(theBackground));
        super.add(new ColorBall(theBackground));
    }

    // The tube needs to 'hold' up to four balls, but a GridLayout has no such limit.
    // Also, a GridLayout will start to fill up from the top down, whereas we need to fill the tube from the bottom up.
    // A visually 'empty' tube is actually already filled with four (invisible) balls.
    // This method replaces the 'lowest' invisible ball in the tube with the ball to be added.
    void add(ColorBall colorBall) {
        ColorBall ball1 = (ColorBall) getComponent(0);
        ColorBall ball2 = (ColorBall) getComponent(1);
        ColorBall ball3 = (ColorBall) getComponent(2);
        ColorBall ball4 = (ColorBall) getComponent(3);

        if (ball4.theColor == theBackground) {  // the tube is empty
            removeAll();
            super.add(new ColorBall(theBackground));
            super.add(new ColorBall(theBackground));
            super.add(new ColorBall(theBackground));
            super.add(colorBall);     // 'bottom' of the tube
        } else if (ball3.theColor == theBackground) { // one ball in the tube
            removeAll();
            super.add(new ColorBall(theBackground));
            super.add(new ColorBall(theBackground));
            super.add(colorBall);  // this will appear to be the second ball in the tube
            super.add(ball4);
        } else if (ball2.theColor == theBackground) { // two balls already in the tube
            removeAll();
            super.add(new ColorBall(theBackground));
            super.add(colorBall); // this will appear to be the third ball in the tube
            super.add(ball3);
            super.add(ball4);
        } else if (ball1.theColor == theBackground) { // three balls already in the tube
            removeAll();
            super.add(colorBall); // this will appear to be the ball at the top of the tube
            super.add(ball2);
            super.add(ball3);
            super.add(ball4);
        } else {  // The tube is already full, or some other problem.
            System.out.println("There was a problem adding " + colorBall.toString());
        }
        revalidate();
    }

    void clear() {
        removeAll();
        super.add(new ColorBall(theBackground));
        super.add(new ColorBall(theBackground));
        super.add(new ColorBall(theBackground));
        super.add(new ColorBall(theBackground));
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return new Dimension(tubeWidth, tubeHeight);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(lineColor);
        g.drawRect(0, 0, tubeWidth, tubeHeight);  // additional thickness
        g.drawRect(1, 1, tubeWidth - 2, tubeHeight - 2);
        if(lineColor == Color.LIGHT_GRAY) {
            g.fillRect(1, 1, tubeWidth - 2, tubeHeight - 2);
        }
    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if (TubeRackPanel.activeTube == this) {
            TubeRackPanel.activeTube.lineColor = Color.WHITE;
            TubeRackPanel.activeTube.repaint();
            TubeRackPanel.activeTube = null;
            return;
        } else if (TubeRackPanel.activeTube != null) {
            TubeRackPanel.activeTube.lineColor = Color.WHITE;
            TubeRackPanel.activeTube.repaint();
        }

        if (lineColor == Color.WHITE) lineColor = Color.RED;
        else lineColor = Color.WHITE;
        TubeRackPanel.activeTube = this;
        repaint();
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        removeMouseListener(this);
        lineColor = Color.LIGHT_GRAY;
        repaint();
    }

    public ItemColor[] getContentArray() {
        ItemColor[] theArray = new ItemColor[4];
        ColorBall tmpBall;

        tmpBall =  (ColorBall) getComponent(0);
        if(tmpBall.theColor == theBackground) return null;
        else theArray[0] = tmpBall.getItemColor();

        tmpBall =  (ColorBall) getComponent(1);
        if(tmpBall.theColor == theBackground) return null;
        else theArray[1] = tmpBall.getItemColor();

        tmpBall =  (ColorBall) getComponent(2);
        if(tmpBall.theColor == theBackground) return null;
        else theArray[2] = tmpBall.getItemColor();

        tmpBall =  (ColorBall) getComponent(3);
        if(tmpBall.theColor == theBackground) return null;
        else theArray[3] = tmpBall.getItemColor();

        return theArray;
    }
}
