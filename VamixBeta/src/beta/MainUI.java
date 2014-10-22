package beta;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainUI extends JFrame{
	private Dimension _screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	private int _screenH = (int)_screenDim.getHeight();
	private int _screenW = (int)_screenDim.getWidth();
	
	private SelectionPane selection;
	private ControlPanel control;
	private FunctionPanel functions;

	public MainUI() {
		setResizable(false);
		setSize(800,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(null);
		
		selection = new SelectionPane(this);
		selection.setLocation(5,5);
		getContentPane().add(selection);
		
		control = new ControlPanel();
		control.setLocation(250,5);
		getContentPane().add(control);
		
		functions = new FunctionPanel();
		functions.setLocation(5, 310);
		getContentPane().add(functions);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Dialog", Font.BOLD, 12));
		menuBar.setBackground(Color.DARK_GRAY);
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setForeground(Color.WHITE);
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File media = Operation.openMedia(selection);
				if (media != null) {
					setForMediaOpen(media);
				}
			}
		});
		mntmOpen.setForeground(Color.DARK_GRAY);
		mntmOpen.setBackground(Color.WHITE);
		mnFile.add(mntmOpen);
		
		JMenu menu = new JMenu("");
		menu.setEnabled(false);
		menuBar.add(menu);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setForeground(Color.WHITE);
		menuBar.add(mnEdit);
		
		JMenu menu_1 = new JMenu("");
		menu_1.setEnabled(false);
		menuBar.add(menu_1);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setForeground(Color.WHITE);
		menuBar.add(mnHelp);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainUI().setVisible(true);
			}
		});
	}
	
	public void setForMediaOpen(File mediaFile) {
		control.setForMediaOpen(mediaFile);
	}

	public void setCard(String string) {
		if (string.equals("Audio1")) {
			functions.switchToA1();
		} else if (string.equals("Audio2")) {
			functions.switchToA2();
		} else if (string.equals("Audio3")) {
			functions.switchToA3();
		} else if (string.equals("Audio4")) {
			functions.switchToA4();
		} else if (string.equals("Video3")) {
			functions.switchToV3();
		} else if (string.equals("DL")) {
			functions.switchToDL();
		} else {
			functions.switchToDefault();
		}
	}
}
