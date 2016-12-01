package graphicalInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import colorManagement.ColorBundle;

@SuppressWarnings("serial")
public class GUIPanel extends JFrame {

	public static DrawingArea drawingPad;
	private LeftPanel leftPanel;
	private UpperPanelBar upperPanel;
	private JMenuBar menuBar;
	private JMenu[] menuOptions;
	private static final int FILE = 0;
	private JMenuItem[] menuItems;
	private JMenuItem[] saveOptions;
	private static final int NEW = 0, SAVE = 1, LOAD = 2;
	private static final int SZ = 2;
	private ColorBundle colorBundle;
	private static final int windowMaxLength = 1000, windowMaxHeight = 700;
	private static final int XML = 0, JSON = 1;

	public GUIPanel() {
		colorBundle = new ColorBundle();
		setTitle("Paint Vectors");
		setSize(windowMaxLength, windowMaxHeight);
		setResizable(false);
		setBackground(Color.GRAY);
		drawingPad = new DrawingArea(colorBundle);
		menuBar = new JMenuBar();
		menuOptions = new JMenu[SZ];
		leftPanel = new LeftPanel(drawingPad);
		upperPanel = new UpperPanelBar(drawingPad, leftPanel);
		add(leftPanel, BorderLayout.WEST);
		add(upperPanel, BorderLayout.NORTH);
		initMenuBar();
		initSaveMenu();
		setJMenuBar(menuBar);
		add(drawingPad);
		drawingPad.repaint();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void initMenuBar() {
		menuOptions[FILE] = new JMenu("File");
		menuItems = new JMenuItem[3];
		menuItems[NEW] = new JMenuItem("New");
		menuItems[NEW].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingPad.newPaint();
			}
		});
		menuItems[SAVE] = new JMenu("Save");
		menuItems[LOAD] = new JMenuItem("Load");
		menuItems[LOAD].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(GUIPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						drawingPad.load(file);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(GUIPanel.drawingPad,
								"Either the file is Corrupted or you must load custom Libraries.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		menuOptions[FILE].add(menuItems[NEW]);
		menuOptions[FILE].add(menuItems[SAVE]);
		menuOptions[FILE].add(menuItems[LOAD]);
		menuBar.add(menuOptions[FILE]);
	}

	private void initSaveMenu() {
		saveOptions = new JMenuItem[2];
		saveOptions[XML] = new JMenuItem("Xml");
		saveOptions[JSON] = new JMenuItem("JSON");
		saveOptions[XML].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(GUIPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						drawingPad.saveXML(file);
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						JOptionPane.showMessageDialog(GUIPanel.drawingPad,
								"Please make sure no other program is trying to access this file", "Error",
								JOptionPane.ERROR_MESSAGE);
						drawingPad.newPaint();
					}
				}
			}
		});
		saveOptions[JSON].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(GUIPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						drawingPad.saveJSON(file);
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						JOptionPane.showMessageDialog(GUIPanel.drawingPad,
								"Please make sure no other program is trying to access this file", "Error",
								JOptionPane.ERROR_MESSAGE);
						drawingPad.newPaint();
					}
				}
			}
		});
		menuItems[SAVE].add(saveOptions[XML]);
		menuItems[SAVE].add(saveOptions[JSON]);

	}

	public static void main(String[] args) {
		new GUIPanel();
	}
}