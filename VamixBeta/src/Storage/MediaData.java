package Storage;

import java.io.File;

import FunctionWorker.Converter;

public class MediaData {
	private static MediaData _instance = new MediaData();
	
	private static String _filePath;
	private static String _fileName;
	private static long _rawFileLength;
	private static long _rawFileSize;
	private static String _fileLength;
	private static String _fileSize;
	private static String _fileType;
	
	public static void mediaDataInit(File file, String type) {
		_filePath = file.getAbsolutePath();
		_fileName = file.getName();
		_rawFileLength = MediaPlayer.getMediaPlayerNC().getLength();
		_rawFileSize = file.length();
		_fileLength = Converter.getFormattedTime(MediaPlayer.getMediaPlayerNC().getLength()/1000);
		_fileSize = Converter.getFormatSize(file.length());
		_fileType = type;
	}
	
	public static String getFilePath() {
		return _filePath;
	}
	
	public static String getFileName() {
		return _fileName;
	}
	
	public static long getRawFileLength() {
		return _rawFileLength;
	}
	
	public static long getRawFileSize() {
		return _rawFileSize;
	}
	
	public static String getFileLength() {
		return _fileLength;
	}
	
	public static String getFileSize() {
		return _fileSize;
	}
	
	public static String getFileType() {
		return _fileType;
	}
	
	public static MediaData getMediaData() {
		return _instance;
	}
}
