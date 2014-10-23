package FunctionWorker;

public class Converter {
	
	/**
	 * Method in the Converter class, which converts raw time (long) into a formatted time
	 * of hh:mm:ss, where hh is hours, mm is minutes and ss is seconds
	 * @param time
	 * @return formatTime
	 */
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
	
	/**
	 * Method in the Converter class, which converts formatted time into raw time (long)
	 * @param format
	 * @return time
	 */
	public static long getUnformatTime(String format) {
		String[] times = format.split(":");
		long secForHours = Integer.parseInt(times[0])*3600;
		long secForMinutes = Integer.parseInt(times[1])*60;
		long secForSeconds = Integer.parseInt(times[2]);
		return (secForHours + secForMinutes + secForSeconds);
	}
	
	/**
	 * Method in the Converter class, which converts raw byte size (long) into an appropriately
	 * shortened version of size (either b,Kb,Mb,Gb)
	 * @param size
	 * @return formatSize
	 */
	public static String getFormatSize(long size) {
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
