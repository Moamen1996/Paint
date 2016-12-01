package graphicalInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import loader.ResourceLoader;

@SuppressWarnings("serial")
public class LeftPanel extends JPanel {

  private DrawingArea drawingPad;
  private static final int NUMOFSHAPEBUT = 6;
  public JButton[] shapeButtons;
  private Box shapeBox;
  
  public LeftPanel(DrawingArea drawingPad) {
    this.drawingPad = drawingPad;
    shapeButtons = new JButton[NUMOFSHAPEBUT];
    initShapeButtons();
    addToPanel();
  }

  private void initShapeButtons() {
    String path = "dep/icons/icon";
    for (int i = 0; i < NUMOFSHAPEBUT; i++) {
      shapeButtons[i] = new JButton();
      if (i >= 5) {
        shapeButtons[i].setVisible(false);
      }
      if (i < 6) {
        Icon icon = new ImageIcon(ResourceLoader.load(path + i + ".png"));
        shapeButtons[i].setIcon(icon);
      }
      final int action = i;
      shapeButtons[i].addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          drawingPad.setState(DrawingArea.ADD);
          drawingPad.setShape(action);
        }
      });
    }
  }
  
  private void addToPanel() {
    shapeBox = Box.createVerticalBox();
      for (int i = 0; i < NUMOFSHAPEBUT; i++) {
        shapeBox.add(shapeButtons[i]);
      }
      add(shapeBox);
  }

}