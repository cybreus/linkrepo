package UI;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import resources.ResourceLoader;

public class UIhelper {
	
	public static void setButtonIcon(JButton button, String fileName) {
		button.setIcon(new ImageIcon(ResourceLoader.getImage(fileName)));
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
	}
}
