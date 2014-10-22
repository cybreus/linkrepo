package beta;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Operation {
	private static EmbeddedMediaPlayer _media;
	private static String _type = "none";
	private static String mediaType = "";
	private static File _openedFile;
	private static File _saveLocation;
	private static File _audioFile;
	
	//method called for opening a media file to play
	public static File openMedia(SelectionPane pane) {
		_media = MediaPlayer.getMediaPlayerComponent().getMediaPlayer();
		JFileChooser mediaOpen = new JFileChooser();
		mediaOpen.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int response = mediaOpen.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			File mediaFile = mediaOpen.getSelectedFile();
			String type = checkMedia(mediaFile);
			if (type.equals("none")) {
				JOptionPane.showMessageDialog(null, "File is not a media file, please select another!");
			} else {
				_openedFile = mediaFile;
				String mediaPath = mediaFile.getAbsolutePath();
				_media.startMedia(mediaPath);
				_media.pause();
				_media.setRepeat(true);
				pane.setUpInputPane(mediaFile);
				return mediaFile;
			}
		}
		return null;
	}
	
	//method called for opening an audio file to use
	public static File openAudio() {
		JFileChooser audioOpen = new JFileChooser();
		audioOpen.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int response = audioOpen.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			File audioFile = audioOpen.getSelectedFile();
			String type = checkMedia(audioFile);
			if (type.equals("none") || type.equals("Video")) {
				JOptionPane.showMessageDialog(null, "File does not contain audio, please select another!");
			} else {
				_audioFile = audioFile;
				return audioFile;
			}
		}
		return null;
	}
	
	//method called to set output save file
	public static File setSaveMedia(SelectionPane pane) {
		if (_openedFile != null) {
			JFileChooser mediaSave = new JFileChooser();
			mediaSave.setDialogTitle("Specify an output file name in a directory...");
			int response = mediaSave.showSaveDialog(null);
			if (response == JFileChooser.APPROVE_OPTION) {
				File saveFile = mediaSave.getSelectedFile();	
				String dur = getFormattedTime(MediaPlayer.getMediaPlayerComponent()
						.getMediaPlayer().getLength()/1000);
				int index = _openedFile.getName().indexOf('.');
				mediaType = _openedFile.getName().substring(index);
				String fullFileName = saveFile.getAbsolutePath();
				_saveLocation = new File(fullFileName);
				if (pane != null) {
					//should change later, so that Operation keeps duration as a separate variable
					pane.setUpOutputPane(dur);
				}
		}
		} else {
			JOptionPane.showMessageDialog(null, "Open a file before setting up a save directory!");
		}
		return null;
	}
	
	//method for carrying out the extract audio function
	public static void extractAudio(final File f, final JLabel status) {
		SwingWorker<Integer,Void> extracter = new SwingWorker<Integer,Void>(){
		@Override
		protected Integer doInBackground() throws Exception {
			if (_type.equals("none") || _type.equals("Video")) {
				return 999;
			}
			status.setText("extracting...");
			String cmd;
			if (f == null) {
				cmd = "avconv -i " + _openedFile.getAbsolutePath() + " -vn -c:a libmp3lame"
						+ " -q:a 2 " + _saveLocation.getAbsolutePath() +".mp3";
			} else {
				cmd = "avconv -i " + _openedFile.getAbsolutePath() + " -vn -c:a libmp3lame"
						+ " -q:a 2 " + f.getAbsolutePath() +".mp3";
			}
			ProcessBuilder audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			Process audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			return exit;
		}

		@Override
		protected void done() {
			try {
				if (get() == 999) {
					JOptionPane.showMessageDialog(null, "Media contains no audio!");
				} else {
					status.setText("Done!");
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		};
		extracter.execute();
	}
	
	public static void removeAudio(final JLabel status) {
		SwingWorker<Integer,Void> remover = new SwingWorker<Integer,Void>() {

			@Override
			protected Integer doInBackground() throws Exception {
				if (_type.equals("none") || _type.equals("Video")) {
					return 999;
				}
				status.setText("removing...");
				String cmd;
				cmd = "avconv -i " + _openedFile.getAbsolutePath() + " -c:v copy -an " 
						+ _saveLocation.getAbsolutePath() + mediaType;
				ProcessBuilder audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process audioProcess = audioProcessBuilder.start();
				int exit = audioProcess.waitFor();
				return exit;
			}

			@Override
			protected void done() {
				try {
					if (get() == 999) {
						JOptionPane.showMessageDialog(null, "Media contains no audio!");
					} else {
						status.setText("Done!");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		};
		remover.execute();
	}
	
	public static void overlayAudio(final JLabel status) {
		SwingWorker<Integer,Void> overlayer = new SwingWorker<Integer,Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				if (_type.equals("none") || _type.equals("Video")) {
					return 999;
				}
				status.setText("overlaying...");
				String cmd;
				cmd = "avconv -i " + _openedFile.getAbsolutePath() + " -i " + _audioFile.getAbsolutePath() + 
						" -filter_complex [0:a][1:a]amix[out] -map \"[out]\" -map 0:v -c:v copy" + " -t " 
						+ (_media.getLength()/1000) + " -strict experimental " + _saveLocation.getAbsolutePath() + mediaType;
				ProcessBuilder audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process audioProcess = audioProcessBuilder.start();
				int exit = audioProcess.waitFor();
				return exit;
			}

			@Override
			protected void done() {
				try {
					if (get() == 999) {
						JOptionPane.showMessageDialog(null, "Media contains no audio!");
					} else {
						status.setText("Done!");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		};
		overlayer.execute();
	}
	
	public static void replaceAudio(final JLabel status) {
		SwingWorker<Integer,Void> replacer = new SwingWorker<Integer,Void>(){
			@Override
			protected Integer doInBackground() throws Exception {
				status.setText("replacing...");
				String cmd;
				cmd = "avconv -i " + _openedFile.getAbsolutePath() + " -i " + _audioFile.getAbsolutePath() +
						" -map 1:a -map 0:v -codec copy -t " + (_media.getLength()/1000) + " -strict experimental " +
						_saveLocation.getAbsolutePath() + mediaType;
				ProcessBuilder audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process audioProcess = audioProcessBuilder.start();
				int exit = audioProcess.waitFor();
				return exit;
			}

			@Override
			protected void done() {
				status.setText("Done!");
			}
			
			};
			replacer.execute();
	}
	
	public static void trimVideo(final JLabel status, final long startTime, final long endTime) {
		SwingWorker<Integer,Void> trimmer = new SwingWorker<Integer,Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				if (_type.equals("none") || _type.equals("Audio")) {
					return 999;
				}
				status.setText("trimming video...");
				String cmd;
				cmd = "avconv -i " + _openedFile.getAbsolutePath() + " -codec copy -ss " + startTime + " -t " 
						+ (endTime-startTime) + " " + _saveLocation.getAbsolutePath() + mediaType;
				ProcessBuilder videoProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process videoProcess = videoProcessBuilder.start();
				int exit = videoProcess.waitFor();
				return exit;
			}
			
			@Override
			protected void done() {
				try {
					if (get() == 999) {
						JOptionPane.showMessageDialog(null, "Media contains no video!");
					} else {
						status.setText("Done!");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		};
		trimmer.execute();
	}
	
	public static String checkMedia(File mediaFile) {
		String mediaPath = mediaFile.getAbsolutePath();
		String command = "avconv -i " + mediaPath;
		String prev = _type;
		_type = "none";
		
		try {
			ProcessBuilder fileCheckB = new ProcessBuilder("/bin/bash","-c",command);
			fileCheckB.redirectErrorStream(true);
			Process fileCheck = fileCheckB.start();
				
			InputStream processInput = fileCheck.getInputStream();
			BufferedReader input = new BufferedReader(new InputStreamReader(processInput));
			
			String line;
			while ((line = input.readLine()) != null) {
				if (line.contains("Audio:")) {
					if (_type.equals("Video")) {
						_type = "VideoWAudio";
					} else {
						_type = "Audio";
					}
				}
				if (line.contains("Video:") && line.contains("fps")) {
					if (_type.equals("Audio")) {
						_type = "VideoWAudio";
					} else {
						_type = "Video";
					}
				}
			}
		} catch (NullPointerException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error: Could not check file type!");		
		}
		
		String fin = _type;
		if (_type.equals("none")) {
			_type = prev;
		}
		return fin;
	}
	
	public static String getOpenedFile() {
		if (_openedFile != null) {
			return _openedFile.getAbsolutePath();
		} else {
			JOptionPane.showMessageDialog(null, "A media file must be opened before using this function!");
			return "";
		}
	}
	
	public static String getSaveFileString(String type) {
		if (_saveLocation != null) {
			String s = _saveLocation.getName();
			if (type.equals("A")) {
				return s + ".mp3";
			} else if (type.equals("V")) {
				return s + mediaType;
			}
		} else {
			return "";
		}
		return "";
	}
	
	public static String getFileType() {
		if (_type.equals("VideoWAudio")) {
			return "Video";
		} else if (_type.equals("Video")) {
			return "Video without Audio";
		} else if (_type.equals("Audio")) {
			return "Audio";
		} else {
			return "";
		}
	}
	
	public static String getFormattedTime(long time) {
		String formatTime = "";
		String hs,ms,ss;
		int hours = (int) time / 3600;
		int remainder = (int) time - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;
		if (hours < 10) {
			hs = "0" + hours;
		} else {
		    hs = "" + hours;
		}
		if (mins < 10) {
			ms = "0" + mins;
		} else {
			ms = "" + mins;
		}
		if (secs < 10) {
			ss = "0" + secs;
		} else {
			ss = "" + secs;
		}
		formatTime = hs + ":" + ms + ":" + ss;
		return formatTime;
	}
	
	public static long getUnformatTime(String format) {
		String[] times = format.split(":");
		long secForHours = Integer.parseInt(times[0])*3600;
		long secForMinutes = Integer.parseInt(times[1])*60;
		long secForSeconds = Integer.parseInt(times[2]);
		return (secForHours + secForMinutes + secForSeconds);
	}
	
	public static String getShortSize(long size) {
		String s = "";
		if (size > 1000000000) {
			s = (int)(size/1000000000) + "Gb";
		} else if (size > 1000000) {
			s = (int)(size/1000000) + "Mb";
		} else if (size > 1000) {
			s = (int)(size/1000) + "Kb";
		} else {
			s = size + "b";
		}
		return s;
	}
}
