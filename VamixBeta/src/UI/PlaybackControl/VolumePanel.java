package UI.PlaybackControl;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;

import resources.ResourceLoader;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import Storage.MediaPlayer;
import UI.UIhelper;

public class VolumePanel extends JPanel{
	private EmbeddedMediaPlayer mediaPlayer;
	private JSlider _slider;
	private JButton _muteButton;
	
	private boolean _isMuted = false;
	
	public VolumePanel() {
		setSize(150,35);
		setLayout(null);
		
		mediaPlayer = MediaPlayer.getMediaPlayerComponent().getMediaPlayer();
		
		_muteButton = new JButton();
		UIhelper.setButtonIcon(_muteButton, "notMuteButton.png");
		_muteButton.setEnabled(false);
		_muteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.mute();
				if (_isMuted) {
					UIhelper.setButtonIcon(_muteButton, "notMuteButton.png");
					_isMuted = false;
				} else {
					UIhelper.setButtonIcon(_muteButton, "muteButton.png");
					_isMuted = true;
				}
			}
		});
		_muteButton.setBounds(10, 0, 35, 35);
		add(_muteButton);
		
		_slider = new JSlider();
		_slider.setEnabled(false);
		_slider.setValue(60);
		_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int vol = (int)(_slider.getValue() * 1.5);
				mediaPlayer.setVolume(vol);
			}
		});
		_slider.setSnapToTicks(true);
		_slider.setMajorTickSpacing(10);
		_slider.setBounds(45, 10, 105, 15);
		add(_slider);
	}

	public void setEnabled() {
		_slider.setEnabled(true);
		_muteButton.setEnabled(true);
	}
}
