/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 5: BufferedIO
 * Name: Daniel Kaehn
 * Created: 4/6/2019
 */
package msoe.kaehnd.lab5;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The class implements a buffered output stream. By setting up such an output stream,
 * an application can write bytes to the underlying output stream without necessarily
 * causing a call to the underlying system for each byte written.
 */
public class BufferedOutputStream implements AutoCloseable {


    private OutputStream outputStream;

    private byte[] buf;
    private int position;
    private int bitOutCount;

    private static final int DEFAULT_BUFFER_SIZE = 32;


    /**
     * Creates a new buffered output stream to write data to
     * the specified underlying output stream with the specified buffer size.
     * @param outputStream OutputStream connected to desired output
     * @param i size of buffer array
     */
    public BufferedOutputStream(OutputStream outputStream, int i) {
        this.outputStream = outputStream;
        buf = new byte[i];
    }

    /**
     * Creates a new buffered output stream to write
     * data to the specified underlying output stream.
     * @param outputStream OutputStream connected to desired output
     */
    public BufferedOutputStream(OutputStream outputStream) {
        this(outputStream, DEFAULT_BUFFER_SIZE);
    }


    /**
     * Writes the specified byte to this buffered output stream.
     * @param i byte
     * @throws IOException thrown when buffer flush fails
     * @throws IllegalStateException thrown when called before writeBit() sequence is completed
     */
    public void write(int i) throws IOException, IllegalStateException{
        final int byteMinValue = -128;
        final int byteMaxValue = 127;
        if (i < byteMinValue || i > byteMaxValue) {
            throw new IllegalArgumentException("Byte written must be an int " +
                    "value between -128 and 127");
        }
        checkIllegalState();
        emptyBufferIfFull();
        buf[position++] = (byte) i;
    }

    /**
     * Writes the specified byte array to this buffered output stream.
     * @param bytes byte array containing bytes to be written
     * @throws IOException thrown when buffer flush fails
     * @throws IllegalStateException thrown when called before writeBit() sequence is completed
     */
    public void write(byte[] bytes) throws IOException, IllegalStateException{
        checkIllegalState();
        if (bytes.length < buf.length) {
            for (byte b : bytes) {
                write(b);
            }
        } else {
            outputStream.write(bytes);
        }
    }

    /**
     * Dumps all contents of buffer into the OutputStream
     * @throws IOException thrown when buffer flush fails
     * @throws IllegalStateException thrown when called before writeBit() sequence is completed
     */
    public void flush() throws IOException, IllegalStateException{
        checkIllegalState();
        for (int i = 0; i < position; i++) {
            outputStream.write(buf[i]);
        }
        position = 0;
    }

    /**
     * Writes a single bit to the buffer and eventually the OutputStream.
     * Must be called in multiples of 8 before other methods are functional.
     * @param bit binary 1 or 0
     * @throws IOException thrown when buffer flush fails
     */
    public void writeBit(int bit) throws IOException{

        final int byteSize = 8;
        if (bit != 1 && bit != 0) {
            throw new IllegalArgumentException("Input " + bit + " invalid, expecting 1 or 0");
        }
        emptyBufferIfFull();
        bitOutCount++;
        if (bitOutCount == 1) {
            buf[position] = (byte) (bit << byteSize - bitOutCount);
        } else {
            buf[position] = (byte)(bit << byteSize - bitOutCount | buf[position]);
        }
        if (bitOutCount == byteSize) {
            bitOutCount = 0;
            position++;
        }
    }

    private void checkIllegalState() {
        if (bitOutCount != 0) {
            throw new IllegalStateException("write() or flush() cannot be called until " +
                    "writeBit() sequence is completed");
        }
    }

    private void emptyBufferIfFull() throws IOException {
        if (position == buf.length) {
            outputStream.write(buf);
            position = 0;
        }
    }

    /**
     * Closes OutputStream
     * @throws IOException thrown when closing fails
     */
    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}