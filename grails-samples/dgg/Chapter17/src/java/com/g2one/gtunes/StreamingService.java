package com.g2one.gtunes;

import java.io.*;
import java.nio.channels.*;
import org.apache.commons.logging.*;

public class StreamingService 
{
	private static final int BUFFER_SIZE = 2048;
	private static final Log LOG = LogFactory.getLog(StreamingService.class);
	
	/**
	 * Streams the given song to the given response
	 */
	public void streamSong(Song song, OutputStream out) {
		if(song != null) {
			File file = new File(song.getFile());					
			FileInputStream input = null;
			try {
				input = new FileInputStream(file);
				FileChannel in = input.getChannel();
				in.transferTo(0,in.size(), Channels.newChannel(out));
				out.flush();
			}
			catch(Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			finally {
				try {
					input.close();					
				}
				catch(IOException e) {
					// ignore
				}
			}
		}
	}
}
