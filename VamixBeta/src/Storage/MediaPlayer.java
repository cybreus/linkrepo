package Storage;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;

public class MediaPlayer extends EmbeddedMediaPlayerComponent {

	private static EmbeddedMediaPlayerComponent mP = new MediaPlayer();
	
	private MediaPlayer() {
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	}
	
	public static EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		return mP;
	}
	
	public static EmbeddedMediaPlayer getMediaPlayerNC() {
		return mP.getMediaPlayer();
	}
}
