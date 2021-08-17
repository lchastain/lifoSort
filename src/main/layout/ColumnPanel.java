package layout;

import javax.swing.*;
import java.awt.*;

public class ColumnPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    GridBagConstraints gridBagConstraints; // Default constraints for added components.
    int rowNumber;

    public ColumnPanel() {
        super(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.gridy = rowNumber = 0;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        gridBagConstraints.gridy = rowNumber = 0;
    }

    @Override
    public void add(Component component, Object theConstraints) {
        addRow(component, (GridBagConstraints) theConstraints);
    }

    public void addRow(Component component) {
        gridBagConstraints.gridy = rowNumber++;
        super.add(component, gridBagConstraints);
    }

    public void addRow(Component component, GridBagConstraints alternateConstraints) {
        alternateConstraints.gridy = rowNumber++;
        super.add(component, alternateConstraints);
    }

    // This gives calling contexts a way to get the 'defaults' for this particular
    // panel, so they can make only the mods they need before using them to add
    // a component with slightly different constraints than the ones that they
    // would otherwise default to.
    public GridBagConstraints getDefaultConstraints() {
        GridBagConstraints defaultConstraints = new GridBagConstraints();
        defaultConstraints.weightx = 1;
        defaultConstraints.anchor = GridBagConstraints.LINE_START;
        defaultConstraints.gridwidth = GridBagConstraints.REMAINDER;

        return defaultConstraints;
    }

}
