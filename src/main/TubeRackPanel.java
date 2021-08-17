import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDateTime;

import layout.*;

public class TubeRackPanel extends JPanel {
    GridBagConstraints gridBagConstraints;
    Color theBackground = Color.BLACK;
    BorderLayout theBaseLayout;
    JPanel northPanel;
    JPanel westPanel;
    ColumnPanel centerPanel11;
    ColumnPanel centerPanel14;
    JPanel eastPanel;
    ColumnPanel southPanel11;
    ColumnPanel southPanel14;
    static TubeComponent activeTube;
    JRadioButton layoutChoice;

    enum TubeCount {
        ELEVEN,
        FOURTEEN
    }

    ;

    // Buttons:  Clear, Solve, Help
    // Clear will clear all tubes; individual ones can be cleared by clicking on unwanted content.
    // Solve button disabled until tableau complete
    // Help - info on tube selection, clearing, etc.

    public TubeRackPanel() {
        theBaseLayout = new BorderLayout();
        setLayout(theBaseLayout);
        buildPanels();
    }

    void buildPanels() {
        gridBagConstraints = new ColumnPanel().getDefaultConstraints(); // wasteful, but...
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Action taken when the layout selector Radio Buttons change.
        ActionListener rbListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(activeTube != null) {
                    activeTube.lineColor = Color.WHITE;
                    activeTube.repaint();
                    activeTube = null;
                }
                layoutChoice = (JRadioButton) e.getSource();
                rebuildPanels(layoutChoice);
            }
        };

        // Build the NORTH panel -
        northPanel = new JPanel(new FlowLayout());
        JRadioButton elevenTubesButton = new JRadioButton("11 Tubes");
        elevenTubesButton.setFont(Font.decode("Dialog-bold-16"));
        elevenTubesButton.addActionListener(rbListener);
        JRadioButton fourteenTubesButton = new JRadioButton("14 Tubes");
        fourteenTubesButton.setFont(Font.decode("Dialog-bold-16"));
        fourteenTubesButton.addActionListener(rbListener);
        ButtonGroup tableauSizeChoice = new ButtonGroup();
        tableauSizeChoice.add(elevenTubesButton);
        tableauSizeChoice.add(fourteenTubesButton);
        fourteenTubesButton.setSelected(true); // default
        layoutChoice = fourteenTubesButton;
        northPanel.add(elevenTubesButton);
        northPanel.add(fourteenTubesButton);

        // Build the content for the WEST panel (a 'blank' space)
        westPanel = new JPanel();
        westPanel.setBackground(theBackground);
        Spacer leftSpace = new Spacer(100, 2);
        leftSpace.setColor(theBackground);
        westPanel.add(leftSpace);

        // Build the two CENTER panels
        centerPanel11 = buildCenterPanel(TubeCount.ELEVEN);
        centerPanel14 = buildCenterPanel(TubeCount.FOURTEEN);

        // Build the content for the EAST panel (a 'blank' space)
        eastPanel = new JPanel();
        eastPanel.setBackground(theBackground);
        Spacer rightSpace = new Spacer(100, 2);
        rightSpace.setColor(theBackground);
        eastPanel.add(rightSpace);

        // Build the two SOUTH panels
        southPanel14 = buildSouthPanel(TubeCount.FOURTEEN);
        southPanel11 = buildSouthPanel(TubeCount.ELEVEN);

        // Add the panels to each sector of the BorderLayout of this Panel.
        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel14, BorderLayout.CENTER);  // Default
        add(eastPanel, BorderLayout.EAST);
        add(southPanel14, BorderLayout.SOUTH);    // Default
    }

    ColumnPanel buildCenterPanel(TubeCount tubeCount) {

        // Create the content for the CENTER component panel
        ColumnPanel centerPanel = new ColumnPanel();
        JPanel upperTubeRow = new JPanel(new GridLayout(1, 0, 0, 0));
        upperTubeRow.setBackground(theBackground);
        JPanel lowerTubeRow = new JPanel(new GridLayout(1, 0, 0, 0));
        lowerTubeRow.setBackground(theBackground);

        centerPanel.setBackground(theBackground);

        // Build the 1st six tubes - these will all go into the first row.
        TubeComponent tube1 = new TubeComponent();
        upperTubeRow.add(tube1);

        TubeComponent tube2 = new TubeComponent();
        upperTubeRow.add(tube2);

        TubeComponent tube3 = new TubeComponent();
        upperTubeRow.add(tube3);

        TubeComponent tube4 = new TubeComponent();
        upperTubeRow.add(tube4);

        TubeComponent tube5 = new TubeComponent();
        upperTubeRow.add(tube5);

        TubeComponent tube6 = new TubeComponent();
        upperTubeRow.add(tube6);

        // Build the 7th tube - placement depends on rack size.
        TubeComponent tube7 = new TubeComponent();
        if (tubeCount == TubeCount.FOURTEEN) upperTubeRow.add(tube7);
        else lowerTubeRow.add(tube7);

        // Build the (rest of the) 2nd row of tubes
        TubeComponent tube8 = new TubeComponent();
        lowerTubeRow.add(tube8);

        TubeComponent tube9 = new TubeComponent();
        lowerTubeRow.add(tube9);

        TubeComponent tube10 = new TubeComponent();
        lowerTubeRow.add(tube10);

        TubeComponent tube11 = new TubeComponent();
        lowerTubeRow.add(tube11);

        if (tubeCount == TubeCount.ELEVEN) {
            tube10.setEnabled(false);
            tube11.setEnabled(false);
        }

        // The last 3 tubes will only be added to the larger rack.
        if (tubeCount == TubeCount.FOURTEEN) {
            TubeComponent tube12 = new TubeComponent();
            lowerTubeRow.add(tube12);

            TubeComponent tube13 = new TubeComponent();
            lowerTubeRow.add(tube13);

            TubeComponent tube14 = new TubeComponent();
            lowerTubeRow.add(tube14);

            tube13.setEnabled(false);
            tube14.setEnabled(false);
        }

        centerPanel.addRow(upperTubeRow, gridBagConstraints);
        centerPanel.addRow(new Spacer(2, 20));
        if (tubeCount == TubeCount.ELEVEN) { // Indent the second row because it has fewer tubes -
            GridBagConstraints tmpConstraints = (GridBagConstraints) gridBagConstraints.clone();
            tmpConstraints.insets = new Insets(0, 10, 0, 0);
            centerPanel.addRow(lowerTubeRow, tmpConstraints);
        } else {
            centerPanel.addRow(lowerTubeRow, gridBagConstraints);
        }

        return centerPanel;
    }

    ColumnPanel buildSouthPanel(TubeCount tubeCount) {
        // Create the South panel -
        ColumnPanel southPanel = new ColumnPanel();

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(TubeRackPanel.activeTube == null) {
                    System.out.println("You must select a tube before adding a ball.");
                } else {
                    ColorBall cb = (ColorBall) e.getSource();
                    TubeRackPanel.activeTube.add(new ColorBall(cb.getItemColor()));
                }
            }
        };

        // Create the 14 ColorBalls to be used in the ballChoiceRow -
        ColorBall cbLimeGreen = new ColorBall(ItemColor.LIMEGREEN);
        ColorBall cbSkyBlue = new ColorBall(ItemColor.SKYBLUE);
        ColorBall cbGreen = new ColorBall(ItemColor.GREEN);
        ColorBall cbPurple = new ColorBall(ItemColor.PURPLE);
        ColorBall cbBrown = new ColorBall(ItemColor.BROWN);
        ColorBall cbGray = new ColorBall(ItemColor.GRAY);
        ColorBall cbRed = new ColorBall(ItemColor.RED);
        ColorBall cbOrange = new ColorBall(ItemColor.ORANGE);
        ColorBall cbPaleGreen = new ColorBall(ItemColor.PALEGREEN);
        ColorBall cbYellow = new ColorBall(ItemColor.YELLOW);
        ColorBall cbPink = new ColorBall(ItemColor.PINK);
        ColorBall cbBlue = new ColorBall(ItemColor.BLUE);

        // Create the Ball selection row - first row of the south panel
        JPanel ballChoiceRow = new JPanel(new GridLayout(1, 0, 0, 0));
        ballChoiceRow.add(cbLimeGreen);
        ballChoiceRow.add(cbSkyBlue);
        if (tubeCount == TubeCount.FOURTEEN) ballChoiceRow.add(cbGreen);    // Not in the set of 9 colors
        ballChoiceRow.add(cbPurple);
        if (tubeCount == TubeCount.FOURTEEN) ballChoiceRow.add(cbBrown);    // Not in the set of 9 colors
        ballChoiceRow.add(cbGray);
        ballChoiceRow.add(cbRed);
        ballChoiceRow.add(cbOrange);
        ballChoiceRow.add(cbPaleGreen);
        if (tubeCount == TubeCount.FOURTEEN) ballChoiceRow.add(cbYellow);   // Not in the set of 9 colors
        ballChoiceRow.add(cbPink);
        ballChoiceRow.add(cbBlue);
        ballChoiceRow.setBackground(Color.DARK_GRAY);
        int ballChoiceCount = ballChoiceRow.getComponentCount();
        for(int i=0; i<ballChoiceCount; i++) {
            ColorBall cb = (ColorBall) ballChoiceRow.getComponent(i);
            cb.addMouseListener(ma); // Add the mouse adapter as a listener
        }
        southPanel.addRow(ballChoiceRow, gridBagConstraints);
        southPanel.addRow(new Spacer(2, 6));

        // Create the row of buttons - second 'real' row of the south panel
        JPanel buttonRow = new JPanel(new GridLayout(1, 0, 0, 0));
        JButton clearTubesButton = new JButton("Clear All Tubes");
        clearTubesButton.setFont(Font.decode("Dialog-bold-12"));
        clearTubesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllTubes();
            }
        });
        buttonRow.add(clearTubesButton);
        JButton clearActiveTubeButton = new JButton("Clear Active Tube");
        clearActiveTubeButton.setFont(Font.decode("Dialog-bold-12"));
        clearActiveTubeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(TubeRackPanel.activeTube != null) {
                    activeTube.clear();
                    activeTube.revalidate();
                }
            }
        });
        buttonRow.add(clearActiveTubeButton);
        JButton solveButton = new JButton("Solve Puzzle");
        solveButton.setFont(Font.decode("Dialog-bold-14"));
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ItemColor[][] theTableau = generateTableau();
                saveTableau(theTableau); // temp
                if(theTableau == null) {
                    System.out.println("The tableau is not fully defined; cannot solve yet.");
                } else {
                    saveTableau(theTableau);
                    LifoSort.solvePuzzle(theTableau);
                }
            }
        });
        JPanel solveButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        solveButtonWrapper.add(solveButton); // The wrapper will prevent the button from stretching.
        buttonRow.add(solveButtonWrapper);
        JButton helpButton = new JButton("Help");
        helpButton.setFont(Font.decode("Dialog-bold-12"));
        buttonRow.add(helpButton);
        southPanel.addRow(buttonRow, gridBagConstraints);
        southPanel.addRow(new Spacer(2, 10));

        return southPanel;
    }

    void clearAllTubes() {
        ColumnPanel centerPanel;
        if(layoutChoice.getText().startsWith("11")) {
            centerPanel = centerPanel11;
        } else {
            centerPanel = centerPanel14;
        }
        for(int i=0; i<centerPanel.getComponentCount(); i++) {
            JComponent tmpComponent = (JComponent) centerPanel.getComponent(i);
            if(tmpComponent instanceof JPanel) {
                for(int j=0; j<tmpComponent.getComponentCount(); j++) {
                    TubeComponent tubeComponent = (TubeComponent) tmpComponent.getComponent(j);
                    tubeComponent.clear();
                }
            }
        }
        revalidate();
    }

    private ItemColor[][] generateTableau() {
        int tubeCount;
        int tubeCapacity = 4;
        ItemColor[][] theTableau;

        ColumnPanel centerPanel;
        if(layoutChoice.getText().startsWith("11")) {
            tubeCount = 11;
            theTableau = new ItemColor[tubeCount][tubeCapacity];
            theTableau[9] = new ItemColor[4];
            theTableau[10] = new ItemColor[4];
            centerPanel = centerPanel11;
        } else {
            tubeCount = 14;
            theTableau = new ItemColor[tubeCount][tubeCapacity];
            theTableau[12] = new ItemColor[4];
            theTableau[13] = new ItemColor[4];
            centerPanel = centerPanel14;
        }

        int theIndex = 0; // The tubes are in two visual rows; we need to keep track of which one we're on.
        for(int i=0; i<centerPanel.getComponentCount(); i++) { // Expecting 3 items (rows), first and third being Panels;
            JComponent tmpComponent = (JComponent) centerPanel.getComponent(i);
            if(tmpComponent instanceof JPanel) {   // upperTubeRow or lowerTubeRow
                for(int j=0; j<tmpComponent.getComponentCount(); j++) {
                    TubeComponent tubeComponent = (TubeComponent) tmpComponent.getComponent(j);
                    ItemColor[] theContent = tubeComponent.getContentArray();
                    if(theContent == null) return null;
                    theTableau[theIndex++] = theContent;
                    if(theIndex >= tubeCount-2) break;  // I don't ever expect '>', but this rules out external forces.
                }
            }
        }
        return theTableau;
    }

    void rebuildPanels(JRadioButton source) {
        removeAll();  // Removing individual sections did not seem to work.
        // This works ok, but seems to be because all content are Panels or
        // child classes of JPanel.  When components were in WEST/EAST, they
        // became nulled out and could not be reused without recreation.  So,
        // wrapped the Spacers in JPanels.

        // Re-Add the panels to each sector of the BorderLayout of this Panel.
        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);

        if(source.getText().equals("11 Tubes")) {
            add(centerPanel11, BorderLayout.CENTER);
            add(southPanel11, BorderLayout.SOUTH);
        }
        else {
            add(centerPanel14, BorderLayout.CENTER);
            add(southPanel14, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();
    }

    private void saveTableau(ItemColor[][] theTableau) {
        String theDateTime = LocalDateTime.now().toString();
        System.out.println("");
        String currentDirectory;
        File file = new File(theDateTime);
        currentDirectory = file.getAbsolutePath();
        System.out.println("Current working directory : "+currentDirectory);
    }

}
