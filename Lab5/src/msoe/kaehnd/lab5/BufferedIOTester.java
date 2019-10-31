/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 5: BufferedIO
 * Name: Daniel Kaehn
 * Created: 4/6/2019
 */
package msoe.kaehnd.lab5;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


/**
 * Simple program that demonstrates how to read/write from
 * Buffered Input/Output Streams.
 */
public class BufferedIOTester {

    public static void main(String[] args) {
        try {
            System.out.println("\nTesting BufferdInputStream:\n");

            System.out.println("Testing read(byte[]) with empty InputStream");
            testReadToArray(byteArray(0, 0));



            System.out.println("Testing read() and read(" +
                    "byte[]) with array with multiple of 8 elements");
            testRead(byteArray( 0, 128));
            testReadToArray(byteArray(0, 128));



            System.out.println("Testing readBits() and readBits(byte[])" +
                    " with array with non-multiple of 8 elements");

            testRead(byteArray(0, 126));
            testReadToArray(byteArray(0, 126));

            testReadToArrayIllegalState(byteArray(0, 2));

            System.out.println("Test readBit() with all positive bytes");
            testReadBit(byteArray(0, 128));
            System.out.println("Test readBit() with negative and positive bytes");
            testReadBit(byteArray(0, 128));


            System.out.println("\n\nTesting BufferedOutputStream:\n");

            System.out.println("Testing write() and write(byte[])" +
                    " with array with multiple of 8 elements");
            testWrite(byteArray(0, 128));
            testWriteArray(byteArray(0, 128));

            System.out.println("Testing write() and write(byte[])" +
                    " with array with non-multiple of 8 elements");
            testWrite(byteArray(0, 125));
            testWriteArray(byteArray(0, 125));


            System.out.println("Testing write() and write(byte[]) with array of length" +
                    " less than default buffer");
            testWrite(byteArray(0, 10));
            testWriteArray(byteArray(0, 10));


            System.out.println("Testing writeBit() with array with multiple of 8 elements");
            testWriteBit(byteArray(0, 128));
            System.out.println("Testing writeBit() with array with non-multiple of 8 elements");
            testWriteBit(byteArray(0, 125));
        } catch (IOException e) {
            System.err.println("I/O Error");
        }
    }

    //Generates a sequential byte array inclusive of a and exclusive of b
    private static byte[] byteArray(int a, int b) {
        int size = Math.abs(a) + Math.abs(b);
        byte[] toReturn = new byte[size];
        for (int i = 0; i < Math.abs(a) + Math.abs(b); i++) {
            int toAdd = a + i;
            toReturn[i] = (byte)toAdd;
        }
        return toReturn;
    }

    private static void testRead(byte[] bytes) throws IOException {
        System.out.println("Testing read()...");
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
                 BufferedInputStream in = new BufferedInputStream(bin)) {
            // Read bytes from input stream
            byte[] bytesRead = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                int number = in.read();
                if (number == -1) {
                    System.out.println("Error: read too many bytes");
                }
                bytesRead[i] = (byte) number;
            }
            // Confirm that bytes read match what was in the input stream
            if (0 != Arrays.compare(bytesRead, bytes)) {
                System.out.println("Error: bytes read don't match");
            }
        }
        System.out.println("Test Successful\n");
    }

    private static void testReadToArray(byte[] bytes) throws IOException {
        boolean success = true;
        System.out.println("Testing read(byte[])....");
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
                 BufferedInputStream in = new BufferedInputStream(bin)) {
            byte[] bytesRead = new byte[8];
            int code = 0;
            int i = 0;
            while (code != -1) {
                // Read bytes from input stream
                code = in.read(bytesRead);
                // Confirm that bytes read match what was in the input stream

                for (int j = 0; j < code; j++) {
                    if (bytesRead[j] != bytes[i + j]) {
                        success = false;
                        System.out.println("Error: bytes read don't match");
                    }
                }
                i += code;
            }
        }
        if (success) {
            System.out.println("Test Successful\n");
        }
    }

    private static void testReadToArrayIllegalState(byte[] bytes) throws IOException{
        System.out.println("Testing read(byte[]) IllegalState....");
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
                 BufferedInputStream in = new BufferedInputStream(bin)) {
            in.readBit();
            in.read();

        } catch (IllegalStateException e) {
            System.out.println("An Illegal State Was Thrown\nTest Successful\n");
        }
    }

    private static void testReadBit(byte[] bytes) throws IOException {
        System.out.println("Testing readBit()...");
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
                 BufferedInputStream in = new BufferedInputStream(bin)) {
            byte[] bytesRead = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                //Read 8 bits and store as byte
                bytesRead[i] = (byte) (in.readBit() | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 1 | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 2 | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 3 | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 4 | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 5 | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 6 | bytesRead[i]);
                bytesRead[i] = (byte) (in.readBit() << 7 | bytesRead[i]);
            }
            //Confirm that bytes read match what was in input stream
            if (0 != Arrays.compare(bytesRead, bytes)) {
                System.out.println("Error: bytes read don't match");
            } else {
                System.out.println("Test Successful\n");
            }
        }
    }

    private static void testWrite(byte[] bytes) throws IOException {
        System.out.println("Testing write()...");
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream(bytes.length);
                 BufferedOutputStream out = new BufferedOutputStream(bout)) {
            // Write bytes to output stream

            out.write(bytes);
            out.flush();
            // Get bytes out of the output stream byte array
            byte[] bytesWritten = bout.toByteArray();

            // Confirm that bytes in the output stream match what was written
            if (0 != Arrays.compare(bytesWritten, bytes)) {
                System.out.println("Error: bytes written don't match");
            } else {
                System.out.println("Test Successful\n");
            }
        }
    }

    private static void testWriteArray(byte[] bytes) throws IOException {
        System.out.println("Testing write(byte[])...");
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream(bytes.length);
                 BufferedOutputStream out = new BufferedOutputStream(bout)) {

            //Extend length of given array by copying four times
            byte[] duplicatedArray = new byte[bytes.length * 4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < bytes.length; j++) {
                    duplicatedArray[i * bytes.length + j] = bytes[j];
                }
            }
            // Write bytes to output stream four times
            out.write(bytes);
            out.write(bytes);
            out.write(bytes);
            out.write(bytes);
            out.flush();

            // Get bytes out of the output stream byte array
            byte[] bytesWritten = bout.toByteArray();

            // Confirm that bytes in the output stream match what was written four times
            if (0 != Arrays.compare(bytesWritten, duplicatedArray)) {
                System.out.println("Error: bytes written don't match");
            } else {
                System.out.println("Test Successful\n");
            }
        }
    }

    private static void testWriteBit(byte[] bytes) throws IOException{
        System.out.println("Testing writeBit(int)...");
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream(bytes.length);
                 BufferedOutputStream out = new BufferedOutputStream(bout)) {
            for (byte value: bytes) {
                //Convert byte to 8 bits and write each individually
                out.writeBit(value>> 7 & 0b00000001);
                out.writeBit(value>> 6 & 0b00000001);
                out.writeBit(value>> 5 & 0b00000001);
                out.writeBit(value>> 4 & 0b00000001);
                out.writeBit(value>> 3 & 0b00000001);
                out.writeBit(value>> 2 & 0b00000001);
                out.writeBit(value>> 1 & 0b00000001);
                out.writeBit(value & 0b00000001);
            }
            out.flush();

            //Get bytes out of the output stream byte array
            byte[] bytesWritten = bout.toByteArray();

            //Confirm that bytes in the output stream match the bits that were written
            if (0 != Arrays.compare(bytesWritten, bytes)) {
                System.out.println("Error: bytes written don't match");
            } else {
                System.out.println("Test Successful\n");
            }
        }
    }
}

/*
 * Partner: David Schulz
 *
 * - Test when given InputStream with a multiple of 8 number of elements
 *          Tested by passing the BufferedInputStream a ByteArrayInputStream
 *          with an array with 128 elements
 * - Test when given InputStream with a non-multiple of 8 multiple of elements
 *          Tested by passing the BufferedInputStream a ByteArrayInputStream
 *          with an array of 126 elements
 * - Test when given InputStream with no elements
 *          Tested by passing the BufferedInputStream a ByteArrayInputStream
 *          with an array of 0 elements
 * - Test when readBit() is called a non-multiple of 8 number of times
 *      and then read(byte[]) is called
 *          Tested by calling readBit() twice then calling read(byte[])
 *           and catching the exception
 */