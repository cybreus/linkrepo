package beta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * This is a class which extend JFrame. It contains text-fields, 
 * buttons that leads user to file navigator, ComboBoxes,
 * everything that user needs to create a title or credit page 
 * which will be joined to a main video.
 * 
 * @author Namjun Park (npar350) Andy Choi (mcho588)
 *
 */

public class CreateTitleCreditFrame extends JFrame implements ActionListener {

	//declare variables/components
	private String _selectedVidPath;
	private JTextArea _textArea;
	private JTextField _textField1, _textField2;
	private JLabel _label1, _label2, _label3, _labelFont, _labelColour, _labelSize;
	private JComboBox<String> _font, _textSize, _colour;
	private JButton _generateButton;
	private JButton _browseButton1, _browseButton2;
	private JScrollPane _scrollBar;
	private String _frameTitle;
	String iconPath = "./icons";
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public CreateTitleCreditFrame(String mediaPath, String frameTitle){
		
		super(frameTitle);
		
		_selectedVidPath = mediaPath;
		_frameTitle = frameTitle;
		
		//create image icon for browse file button
		ImageIcon openFile = new ImageIcon(iconPath + "/open_button.gif");
		//settings for browse button1 - music
		_browseButton1 = new JButton();
		_browseButton1.setIcon(openFile);
		_browseButton1.setOpaque(false);
		_browseButton1.setContentAreaFilled(false);
		_browseButton1.setBorderPainted(false);
		_browseButton1.setFocusPainted(false);
		//settings for browse button2 - image
		_browseButton2 = new JButton();
		_browseButton2.setIcon(openFile);
		_browseButton2.setOpaque(false);
		_browseButton2.setContentAreaFilled(false);
		_browseButton2.setBorderPainted(false);
		_browseButton2.setFocusPainted(false);
		
		
		//setup JComboBox(s)
		String[] fonts = {"FreeMono.ttf", "Kinnari.ttf", "Purisa-Oblique.ttf", "TakaoPGothic.ttf", "TlwgTypist-Bold.ttf", "Ubuntu-M.ttf"};
		String[] sizes = { "10" , "20", "30", "40", "50", "60" };
		String[] colours = { "red", "blue", "white", "black" };
		_font = new JComboBox<String>(fonts);
		_textSize = new JComboBox<String>(sizes);
		_colour = new JComboBox<String>(colours);
		
		setBackground(Color.LIGHT_GRAY);
		setSize(600,380);
		setLayout(null);
		setLocation((_screenWidth-600)/2,(_screenHeight-350)/2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//add components to this frame
		this.add(_label1 = new JLabel("Enter text"));
		_label1.setBounds(25, 10, 200, 20);
		//set up text area
		this.add(_textArea = new JTextArea());
		_textArea.setBounds(25, 30, 550, 100);
		_textArea.setDocument(new TextManager(220));
		_textArea.setLineWrap(true);
		_textArea.setWrapStyleWord(true);
		Logger.getInstance().pullText(_textArea);
		
		//make scroll pane
		this.add(_scrollBar = new JScrollPane(_textArea));
		_scrollBar.setBounds(25, 30, 550, 100);
		
		this.add(_font);
		_font.setBounds(65, 140, 150, 30);
		_font.setSelectedIndex(Logger.getInstance().pullFontIndex());
		this.add(_labelFont = new JLabel("font"));
		_labelFont.setBounds(30, 140, 60, 30);
		
		this.add(_textSize);
		_textSize.setBounds(280, 140, 100, 30);
		_textSize.setSelectedIndex(Logger.getInstance().pullSizeIndex());
		this.add(_labelSize = new JLabel("Size"));
		_labelSize.setBounds(245, 140, 60, 30);
		
		this.add(_colour);
		_colour.setBounds(470, 140, 100, 30);
		_colour.setSelectedIndex(Logger.getInstance().pullColourIndex());
		this.add(_labelColour = new JLabel("Colour"));
		_labelColour.setBounds(420, 140, 60, 30);
		
		this.add(_label2 = new JLabel("Select music"));
		_label2.setBounds(25, 190, 200, 20);
		
		this.add(_textField1 = new JTextField());
		_textField1.setBounds(25, 210, 500, 20);
		_textField1.setText(Logger.getInstance().pullMusicPath());
		_textField1.setEditable(false);
		
		this.add(_browseButton1);
		_browseButton1.setBounds(540 , 210, 30, 30);
		_browseButton1.addActionListener(this);
		
		this.add(_label3 = new JLabel("select background picture"));
		_label3.setBounds(25, 230, 200,20 );
		
		this.add(_textField2 = new JTextField());
		_textField2.setBounds(25,250,500,20);
		_textField2.setText(Logger.getInstance().pullImagePath());
		_textField2.setEditable(false);
		
		this.add(_browseButton2);
		_browseButton2.setBounds(540 , 250 , 30, 30);
		_browseButton2.addActionListener(this);
		
		this.add(_generateButton = new JButton("Generate!"));
		_generateButton.setBounds(200, 300, 200,50);
		_generateButton.addActionListener(this);
		
		//display this frame
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _browseButton1){
			//open Jfilechooser if browse button clicked
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//store result of filechooser
			int result = fileChooser.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION){
				//the selected file is the video and path is retrieved
				File _mediaFile;
				_mediaFile = fileChooser.getSelectedFile();
			
				//bash command to 'grep' to verify file as audio
				String audCmd = "file " + _mediaFile.getAbsolutePath() + " | grep -i audio";
				ProcessBuilder audCheckBuilder = new ProcessBuilder("/bin/bash","-c",audCmd);
				try {
					//process run
					Process audCheck = audCheckBuilder.start();
					int audTerm = audCheck.waitFor();
					//a correct termination indicates it is a media file
					if (audTerm == 0) {
						//enter this if statement means its audio.
						_textField1.setText(_mediaFile.getAbsolutePath());
					} else {
						JOptionPane.showMessageDialog(this, "File is not an audio type!");
					}
				} catch (Exception ex) {
					//if exception occurs nothing extra happens
				}
			}
			
		} else if (e.getSource() == _browseButton2){
			//open Jfilechooser if browse button clicked
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//store result of filechooser
			int result = fileChooser.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION){
				//the selected file is the video and path is retrieved
				File _mediaFile;
				_mediaFile = fileChooser.getSelectedFile();
			
				//bash command to 'grep' to verify file as audio
				String imgCmd = "file " + _mediaFile.getAbsolutePath() + " | grep -i image";
				ProcessBuilder imgCheckBuilder = new ProcessBuilder("/bin/bash","-c", imgCmd);
				try {
					//process run
					Process imgCheck = imgCheckBuilder.start();
					int imgTerm = imgCheck.waitFor();
					//a correct termination indicates it is a media file
					if (imgTerm == 0) {
						//enter this if statement means its audio.
						_textField2.setText(_mediaFile.getAbsolutePath());
					} else {
						JOptionPane.showMessageDialog(this, "File is not an image type!");
					}
				} catch (Exception ex) {
					//if exception occurs nothing extra happens
				}
			}
		} else if (e.getSource() == _generateButton){
			//generate title page, or credit page depending on title of frame.
			//check that all fields are not blank.
			if (_textArea.getText().length() == 0 || _textField1.getText().length() == 0 || _textField2.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "There are blank fields! Make sure text, music file, image file are specifed");
			} else {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select directory to save video to");
				int result = fileChooser.showSaveDialog(this);
				if (result == JFileChooser.APPROVE_OPTION){	
					//store path of directory to save it to
					String savePath = fileChooser.getCurrentDirectory().getAbsolutePath();
					String outputPathName = fileChooser.getSelectedFile().toString();
					
					File out = new File(outputPathName);
					boolean cancelled = false;
					//if output name specified exists loop until user selects to overwrite or exits filechooser
					while (out.exists()){
						int result2 = JOptionPane.showConfirmDialog(null, "file already exists. Would you like to overwrite?", "", 0);
						if (result2 == JOptionPane.YES_OPTION){
							break;
						} else {
							int result3 = fileChooser.showSaveDialog(this);
							if (result3 == JFileChooser.APPROVE_OPTION){	
								//store path of directory to save it to
								outputPathName = fileChooser.getSelectedFile().toString();								
								out = new File(outputPathName);
							} else if (result3 == JFileChooser.CANCEL_OPTION){
								cancelled = true;
								break;
							}
						}
					}
					//only initialise and execute swingworker is filechooser has exited without being deliberately exited/cancelled
					if (cancelled == false){
						if (_frameTitle.equals("Create Title page(s)")){
							//pass on true in the constructor to indicate title page generation
							TitleCreditGenerator generator = new TitleCreditGenerator(true, _textArea.getText(), _textField1.getText(), _textField2.getText(), _selectedVidPath, savePath, outputPathName, _font, _textSize, _colour);
							generator.execute();
						} else if (_frameTitle.equals("Create Credit page(s)")){
							//pass on false in the constructor to indicate credit page generation
							TitleCreditGenerator generator = new TitleCreditGenerator(true, _textArea.getText(), _textField1.getText(), _textField2.getText(), _selectedVidPath, savePath, outputPathName, _font, _textSize, _colour);
							generator.execute();
						}
					}
				}
			}
		} 
		
	}

}
