package FunctionWorker.SwingWorkers;

import java.util.List;

import javax.swing.SwingWorker;


import Storage.MediaPlayer;
import UI.PlaybackControl.VideoScreen;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class MediaProgressChecker extends SwingWorker<Void,Void>{
	private EmbeddedMediaPlayer media;
	private VideoScreen pP;
	
	//constructor of swing worker takes in Main panel as input to invoke update on
	public MediaProgressChecker(VideoScreen parentPanel) {
		pP = parentPanel;
		media = MediaPlayer.getMediaPlayerComponent().getMediaPlayer();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		long length = media.getLength();
		//after a media is opened, continuously checks for media progress
		//when video is finish and not playable, restart
		while (true) {
			Thread.sleep(50);
			if (media.isPlayable()) {
				publish();
				if (media.isPlayable() == false && media.getTime() > length) {
					break;
				}
			}
		}
		return null;
	}

	@Override
	protected void process(List<Void> chunks) {
		//update progress bar UI, every publish()
		pP.updateMediaProgress();
	}

}