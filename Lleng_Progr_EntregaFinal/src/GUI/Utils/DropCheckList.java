package GUI.Utils;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/**
 *
 * @author dobleme
 */
public class DropCheckList extends javax.swing.JScrollPane {

  private JList list;

  public DropCheckList(JList list) {
    this.list = list;
    initComponents();
  }

  private void initComponents() {
    // Use a CheckListRenderer (see below) to renderer list cells
    list.setCellRenderer(new CheckListRenderer());
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Add a mouse listener to handle changing selection
    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent event) {
        JList list = (JList) event.getSource();
        // Get index of item clicked
        int index = list.locationToIndex(event.getPoint());
        CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
        // Toggle selected state
        item.setSelected(!item.isSelected());
        // Repaint cell
        list.repaint(list.getCellBounds(index, index));
      }
    });

    this.setViewportView(list);
  }

  public void reInit(JList list) {
    this.list = list;
    initComponents();
  }

  public JList getList() {
    return list;
  }
}

// Handles rendering cells in the list using a check box
class CheckListRenderer extends JCheckBox implements ListCellRenderer {

  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
    setEnabled(list.isEnabled());
    setSelected(((CheckListItem) value).isSelected());
    setFont(list.getFont());
    setBackground(list.getBackground());
    setForeground(list.getForeground());
    setText(value.toString());
    return this;
  }
}
