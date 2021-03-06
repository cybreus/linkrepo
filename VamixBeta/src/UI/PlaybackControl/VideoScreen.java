package UI.PlaybackControl;

import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JSlider;

import FunctionWorker.Operation;
import FunctionWorker.SwingWorkers.MediaProgressChecker;
import Storage.MediaPlayer;

public class VideoScreen extends JPanel{
	private EmbeddedMediaPlayerComponent mP;
	private JLabel _videoTime;
	private JSlider _progressSlider;
	private MediaProgressChecker _checker;
	
	public VideoScreen() {
		setSize(535,260);
		setLayout(null);
		
		mP = MediaPlayer.getMediaPlayerComponent();
		mP.setBounds(0,0,535,245);
		add(mP);
		
		JPanel videoDetailPanel = new JPanel();
		videoDetailPanel.setBackground(Color.BLACK);
		videoDetailPanel.setBounds(0, 245, 535, 15);
		add(videoDetailPanel);
		videoDetailPanel.setLayout(null);
		
		_videoTime = new JLabel("00:00:00");
		_videoTime.setHorizontalAlignment(SwingConstants.CENTER);
		_videoTime.setForeground(Color.WHITE);
		_videoTime.setBounds(0, 0, 70, 15);
		videoDetailPanel.add(_videoTime);
		
		_progressSlider = new JSlider();
		_progressSlider.setEnabled(false);
		_progressSlider.setMaximum(76000);
		_progressSlider.setValue(0);
		_progressSlider.setBackground(Color.BLACK);
		_progressSlider.setBounds(70, 0, 450, 15);
		videoDetailPanel.add(_progressSlider);
		
		//to be moved
		_checker = new MediaProgressChecker(this);
		_checker.execute();
	}

	//method used by video progress checker to update progress bar of video
	public void updateMediaProgress() {
		_progressSlider.setValue((int)mP.getMediaPlayer().getTime());
		long time = mP.getMediaPlayer().getTime()/1000;
		String formattedTime = Operation.getFormattedTime(time);
		_videoTime.setText(formattedTime);
	}
	
	public void setEnabled() {
		_progressSlider.setMaximum((int)mP.getMediaPlayer().getLength());
		_progressSlider.setEnabled(true);
	}
}
