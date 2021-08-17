import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

// This class is used to provide a (colored) ball component for the current solution, only.
// As written, the component is not highly reusable because its sizes and positions and borders
// are all optimized for this specific application.

public class ColorBall extends JComponent{
    private static final long serialVersionUID = 1L;
    private ItemColor theItemColor;
    Color theColor;
    Color borderColor;
    int borderWidth;   // Best values divisible evenly by 2
    int downShift = 3;
    int diameter;

    public ColorBall(Color color) {
        super();
        theColor = color;
        borderColor = Color.WHITE;
        if(color == Color.BLACK) borderColor = color;
        borderWidth = 2;
        diameter = 40;
        this.setSize(diameter+borderWidth+4, diameter+borderWidth+6);
    }

    public ColorBall(ItemColor theItemColor) {
        this(theItemColor.getColor());
        this.theItemColor = theItemColor;
    }

    public ItemColor getItemColor() {
        if(theItemColor != null) return theItemColor;
        return null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // The additional values on x and y are for visually-pleasing padding between the outermost
        // pixels of the ball and whatever container it will be added to.  For future reusability, may need
        // to implement some variant of 'insets'.  For now, values are hardcoded for the current app.

        // Border first (because it is a bit larger, and will still stick out around the edges after the smaller ball is overlaid).
        g.setColor(borderColor);
        g.fillOval(4, downShift, diameter+borderWidth, diameter+borderWidth);

        // Then the ball, overlaying the 'border' oval.
        g.setColor(theColor);
        g.fillOval(borderWidth/2 + 4 , borderWidth/2 + downShift, diameter, diameter);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        // The additional values on width and height are for visually-pleasing padding between the outermost
        // pixels of the ball and whatever container it will be added to.  For future reusability, may need
        // to implement some variant of 'insets'.  For now, values are hardcoded for the current app.
        return new Dimension(diameter+borderWidth*2 + 2, diameter+borderWidth*2 + 4);
    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        TubeRackPanel.activeTube.add(new ColorBall(this.theColor));
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {

    }


}
