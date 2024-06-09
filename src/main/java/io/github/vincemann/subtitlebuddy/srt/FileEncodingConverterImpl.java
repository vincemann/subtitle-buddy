package io.github.vincemann.subtitlebuddy.srt;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.charset.Charset;

@Log4j2
public class FileEncodingConverterImpl implements FileEncodingConverter{

    /**
     * Reads a file, detects its encoding, converts it to the target encoding, and returns a FileInputStream.
     *
     * @param target the target charset encoding (e.g., StandardCharsets.UTF_8)
     * @param file the file to be read and converted
     * @return a FileInputStream containing the content in the target encoding
     * @throws IOException if an I/O error occurs reading from the file
     */
    public FileInputStream loadWithEncoding(Charset target, File file) throws IOException {
        // Read the file content as bytes
        byte[] fileBytes = readFile(file);

        // Detect the encoding of the file
        CharsetDetector detector = new CharsetDetector();
        detector.setText(fileBytes);
        CharsetMatch match = detector.detect();

        // If encoding is detected, convert to the target encoding
        if (match != null) {
            String originalEncoding = match.getName();
            log.debug("Detected encoding: " + originalEncoding);
            log.debug("converting to: " + target);
            String content = new String(fileBytes, match.getName());
            byte[] targetBytes = content.getBytes(target);

            // Create a ByteArrayInputStream from the converted bytes
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(targetBytes);
            return new FileInputStreamWrapper(byteArrayInputStream);
        } else {
            throw new IOException("Could not detect the encoding of the file.");
        }
    }

    /**
     * Reads the file content as a byte array.
     *
     * @param file the file to be read
     * @return a byte array containing the file content
     * @throws IOException if an I/O error occurs reading from the file
     */
    private static byte[] readFile(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

    /**
     * A simple wrapper for ByteArrayInputStream to work with FileInputStream interface.
     */
    private static class FileInputStreamWrapper extends FileInputStream {
        private final ByteArrayInputStream byteArrayInputStream;

        public FileInputStreamWrapper(ByteArrayInputStream byteArrayInputStream) throws FileNotFoundException {
            super(FileDescriptor.in);
            this.byteArrayInputStream = byteArrayInputStream;
        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return byteArrayInputStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return byteArrayInputStream.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return byteArrayInputStream.skip(n);
        }

        @Override
        public int available() throws IOException {
            return byteArrayInputStream.available();
        }

        @Override
        public void close() throws IOException {
            byteArrayInputStream.close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            byteArrayInputStream.mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            byteArrayInputStream.reset();
        }

        @Override
        public boolean markSupported() {
            return byteArrayInputStream.markSupported();
        }
    }

}

