package uk.co.sunbyte.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Reads an image out of an {@link InputStream} backed by a TCP/IP connection, based on a 
 * convention over the format used. The convention is:
 * 
 * The sender always sends some sort of header first, so that we know it intends to send an
 * actual picture (it might send other sort of data).
 * The header is:
 * sending_data<blank>no_of_bytes<blank>
 * 
 * Following this header, we have the actual byte[] array; the length of the array needs to be exactly
 * the length specified in the no_of_bytes field; any deviation will result in an error being thrown to
 * the user.
 * 
 * The encoding of the stream is expected to be UTF-8
 */
public class ImageReader {
	
	/**
	 * The actual text which works as a header for us, which we look for in order
	 * to be sure we're receiving what we expect to receive.
	 */
	public static final String PICTURE_SENDING_IDENTIFIER = "sending_data";
	
	/**
	 * Expect separate fields in the header using this char.
	 */
	public static final char HEADER_FIELDS_SEPARATOR = ' ';
	
	private InputStream inputStream;
	private byte[] data;
	
	public ImageReader(InputStream inputStream) {
		Objects.requireNonNull(inputStream, "inputStream cannot be null");
		
		this.inputStream = inputStream;
	}
	
	/**
	 * Main entry point; invoke this function when you wish to begin reading 
	 * on the given stream.
	 */
	public void read() throws IOException {
		BufferedInputStream inFromClient = new BufferedInputStream(inputStream);
		
		String pictureSendingIdentifier = readFromClientUntilChar(inFromClient, HEADER_FIELDS_SEPARATOR);
		if (!PICTURE_SENDING_IDENTIFIER.equals(pictureSendingIdentifier)) {
			throw new IOException("Expected to receive picture sending identifier \"" + PICTURE_SENDING_IDENTIFIER + "\" but got \"" + pictureSendingIdentifier + "\"");
		}
		
		// read and parse the number identifying the size of the picture.
		String sizeAsString = readFromClientUntilChar(inFromClient, HEADER_FIELDS_SEPARATOR);
		if (sizeAsString == null || sizeAsString.length() == 0) {
			throw new IOException("Expected to receive the number of bytes but got empty string.");
		}
		
		int size =-1;
		try {
			size = Integer.parseInt(sizeAsString);
		} catch (NumberFormatException nfe) {
			throw new IOException("The field describing the number of bytes the image has needs to be a valid integer but was \"" + sizeAsString + "\"");
		}
		
		//now read the actual data
		data = new byte[size]; // allocate space for image
		readFromClient(inFromClient, size);
	}

	/**
	 * Call this function when you want to retrieve the actual image data
	 * that was read.
	 * Don't call this function before a read() invocation.
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Reads using the given {@code inputStream} until the specified char {@code c} is found in the stream.
	 */
	private static String readFromClientUntilChar(BufferedInputStream inputStream, char boundary) throws IOException {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		while ((c = inputStream.read()) >= 0) {
			if ((char)c == boundary) {
				break;
			}
			sb.append((char)c);
			//no need to scan until the end of message; we're reading until boundary char is found.
		}
		return sb.toString();
	}
	
	/**
	 * Reads from the given {@code BufferedReader}, exactly {@code size} bytes.
	 * Please note this function assumes everything has been validated and the data buffer allocated.
	 * Please also note this function reads everything into the {@link #data} variable.
	 */
	private void readFromClient(BufferedInputStream inputStream, int size) throws IOException {
		int chunkSize = 1024 * 10; //read at most 10kb at once.
		if (chunkSize > size) {
			//Special case; if the message is larger than our allocated buffer,
			//then we don't want to read past the end of the buffer (or an ArrayIndexOutOfBoundsException occurs)
			chunkSize = size; 
		}
		
		int bytesRead = -1, bytesReadSoFar = 0;
		while ((bytesRead = inputStream.read(data, bytesReadSoFar, chunkSize)) > 0) {
			bytesReadSoFar += bytesRead;
			if (size - bytesReadSoFar < chunkSize) {
				chunkSize = (size - bytesReadSoFar); //some sort of sanity check; shouldn't be necessary but just in case.
			}
		}
	}
}
