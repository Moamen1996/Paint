package graphicalInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import colorManagement.ColorBundle;
import loader.ResourceLoader;

@SuppressWarnings("serial")
public class UpperPanelBar extends JPanel {

  private LeftPanel leftPanel;
  private DrawingArea drawingPad;
  private JButton[] controllerButton;
  private Box controlBox;
  private JSlider slider;
  private ColorBundle colorBundle;
  private JButton[] colButtons;
  private static final int MINORSLID = 3, MAJORSLIDE = 6, TICKVAL = 5;
  private static final int STARTSLIDE = 0, FINALSLIDE = 10, INITIALSLIDE = 2;
  private static final int ADD = 0, UNDO = 1, REDO = 2, SELECT = 3, RESIZE = 4, MOVE = 5, DELETE = 6;
  private static final int STROKE = 0, FILL = 1, CLEARCOLOR = 2;
  private static final int COLHEIGHT = 40, COLWIDTH = 40;
  private static final int NUMOFCONTROLBUTS = 7;

  public UpperPanelBar(DrawingArea drawingPad, LeftPanel leftPanel) {
    this.drawingPad = drawingPad;
    this.leftPanel = leftPanel;
    colorBundle = new ColorBundle();
    controllerButton = new JButton[NUMOFCONTROLBUTS];
    colButtons = new JButton[3];
    initColBut();
    initSlider();
    initControlBut();
    initControlBox();
  }

  private void initControlBut() {
    controllerButton[ADD] = makeControlButtons("dep/icons/blue-plus-icon.png", ADD);
    controllerButton[UNDO] = makeControlButtons("dep/icons/Undo-icon.png", UNDO);
    controllerButton[REDO] = makeControlButtons("dep/icons/Redo-icon.png", REDO);
    controllerButton[SELECT] = makeControlButtons("dep/icons/Cursor-Select-icon.png", SELECT);
    controllerButton[RESIZE] = makeControlButtons("dep/icons/resize.png", RESIZE);
    controllerButton[MOVE] = makeControlButtons("dep/icons/move.png", MOVE);
    controllerButton[DELETE] = makeControlButtons("dep/icons/delete.png", DELETE);
  }

  private JButton makeControlButtons(final String picPath, final int action) {
    JButton newBut = new JButton();
    newBut.setIcon(new ImageIcon(ResourceLoader.load(picPath)));
    newBut.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (action == ADD) {
          JFileChooser fileChooser = new JFileChooser();
          int returnVal = fileChooser.showOpenDialog(UpperPanelBar.this);
          boolean idx = false;
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            idx = drawingPad.loadClass(file);
            if (idx)
              leftPanel.shapeButtons[DrawingArea.CUSTOM].setVisible(true);
            else
              leftPanel.shapeButtons[DrawingArea.CUSTOM].setVisible(false);
          }
        } else if (action == UNDO) {
          drawingPad.undo();
        } else if (action == REDO) {
          drawingPad.redo();
        } else if (action == SELECT) {
          drawingPad.setState(DrawingArea.SELECT);
        } else if (action == RESIZE) {
          drawingPad.setState(DrawingArea.RESIZE);
        } else if (action == MOVE) {
          drawingPad.setState(DrawingArea.MOVE);
        } else if (action == DELETE) {
          drawingPad.removeShape();
        }
      }
    });
    return newBut;
  }

  private void initControlBox() {
    controlBox = Box.createHorizontalBox();
    controlBox.setVisible(true);
    for (int index = ADD; index <= DELETE; index++) {
      controlBox.add(controllerButton[index]);
    }
    add(controlBox);
    add(slider);
    for (int index = STROKE; index <= CLEARCOLOR; index++) {
      add(colButtons[index]);
    }
  }

  private void initSlider() {
    slider = new JSlider(JSlider.HORIZONTAL, STARTSLIDE, FINALSLIDE, INITIALSLIDE);
    slider.setMinorTickSpacing(MINORSLID);
    slider.setMajorTickSpacing(MAJORSLIDE);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    slider.setLabelTable(slider.createStandardLabels(TICKVAL));
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        colorBundle.setStrokeThickness(((JSlider) e.getSource()).getValue());
        drawingPad.setStrokeThickness(colorBundle.getStrokeThickness());
      }
    });
  }

  private void initColBut() {
    colButtons[STROKE] = new JButton();
    colButtons[FILL] = new JButton();
    colButtons[CLEARCOLOR] = new JButton();
    colButtons[STROKE].setBackground(colorBundle.getStrokeCol());
    colButtons[STROKE].setPreferredSize(new Dimension(COLWIDTH, COLHEIGHT));
    colButtons[FILL].setBackground(Color.WHITE);
    colButtons[FILL].setPreferredSize(new Dimension(COLWIDTH, COLHEIGHT));
    colButtons[CLEARCOLOR].setPreferredSize(new Dimension(COLWIDTH, COLHEIGHT));
    colButtons[CLEARCOLOR].setIcon(new ImageIcon(ResourceLoader.load("dep/icons/noFill.png")));
    colButtons[STROKE].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Color curStrokeCol = JColorChooser.showDialog(null, "Select color", colorBundle.getStrokeCol());
        if (curStrokeCol != null) {
          colorBundle.setStrokeCol(curStrokeCol);
          drawingPad.setStrokeColor(colorBundle.getStrokeCol());
          colButtons[STROKE].setBackground(colorBundle.getStrokeCol());
        }
      }
    });
    colButtons[FILL].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Color curFill = JColorChooser.showDialog(null, "Select color", colorBundle.getFillCol());
        if (curFill != null) {
          colorBundle.setFillCol(curFill);
          colorBundle.setHasFill(true);
          drawingPad.setFillColor(colorBundle.getFillCol());
          colButtons[FILL].setBackground(colorBundle.getFillCol());
          colButtons[CLEARCOLOR].setIcon(new ImageIcon(ResourceLoader.load(("dep/icons/rainbow.png"))));
        }
      }
    });
    colButtons[CLEARCOLOR].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        colorBundle.setHasFill(false);
        drawingPad.removeFill();
        colButtons[CLEARCOLOR].setIcon(new ImageIcon(ResourceLoader.load(("dep/icons/noFill.png"))));
      }
    });
  }
}