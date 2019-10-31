/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 5: BufferedIO
 * Name: Daniel Kaehn
 * Created: 4/6/2019
 */
package msoe.kaehnd.lab5;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream that buffers input so that fewer bulk calls are made to the passed in InputStream
 */
public class BufferedInputStream implements AutoCloseable {

    private static final int DEFAULT_BUFFER_SIZE = 32;

    private InputStream inputStream;

    private byte[] buf;

    private int position;
    private int currentSize;
    private int bitInCount;

    /**
     * Constructs a BufferedInputStream with an InputStream and
     * @param in InputStream connected to desired input
     * @param i size of internal buffer
     */
    public BufferedInputStream(InputStream in, int i) {
        this.inputStream = in;
        this.buf = new byte[i];
    }

    /**
     * Constructs a BufferedInputStream with an InputStream and default buffer size
     * @param in InputStream connected to desired input
     */
    public BufferedInputStream(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Reads the next byte from the buffer, and refills buffer if necessary
     * @return a byte as an integer ranged 0-127
     * @throws IOException thrown when buffer filling fails
     * @throws IllegalStateException thrown when called and readByte() sequence has not completed
     */
    public int read() throws IOException, IllegalStateException {
        checkIllegalState();
        if (!refillBufferIfEmpty() || currentSize == 0) {
            return -1;
        }
        currentSize--;
        return buf[position++];
    }

    /**
     * Reads the next bytes from the buffer to specified array
     * until array is full or stream is empty
     * @param b byte array bytes are read into
     * @return number of bytes successfully read or -1 when stream is empty
     * @throws IOException thrown when the buffer filling fails
     * @throws IllegalStateException thrown when called and readByte() sequence has not completed
     */
    public int read(byte[] b) throws IOException, IllegalStateException {
        checkIllegalState();
        int numAdded = 0;
        int readByte = 0;
        for (int i = 0; readByte != -1 && i < b.length; i++) {
            readByte = read();
            if (readByte != -1) {
                b[i] = (byte) readByte;
                numAdded++;
            }
        }
        return numAdded == 0 ? -1 : numAdded;
    }


    /**
     * Reads next bit from buffer. Must be called in groups of 8 for other methods to function
     * @return integer 0 or 1
     * @throws IOException thrown when buffer refilling fails
     */
    public int readBit() throws IOException{
        final int byteSize = 8;
        if (!refillBufferIfEmpty() || currentSize == 0) {
            return -1;
        }
        byte value = buf[position];
        int valueToReturn = (value >> bitInCount) & 0b00000001;
        bitInCount++;
        if (bitInCount == byteSize) {
            bitInCount = 0;
            position++;
            currentSize--;
        }

        return valueToReturn;
    }

    private boolean refillBufferIfEmpty() throws IOException{
        if ((position == buf.length || position == 0) && bitInCount == 0) {
            int numRead = inputStream.read(buf);
            if (numRead == -1) {
                return false;
            }
            currentSize = numRead;
            position = 0;
        }
        return true;
    }

    private void checkIllegalState() {
        if (bitInCount != 0) {
            throw new IllegalStateException("read() cannot be called until" +
                    " readBit() sequence is completed");
        }
    }

    /**
     * Closes passed InputStream
     * @throws IOException thrown when closing fails
     */
    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}