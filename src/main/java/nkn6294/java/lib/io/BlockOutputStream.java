package nkn6294.java.lib.io;

import java.io.IOException;
import java.io.OutputStream;

public class BlockOutputStream {

    public static byte NEW_LINE_BYTE = (byte) '\n';
    public BlockOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.delimeterByte = NEW_LINE_BYTE;
        this.convertData = new DefaultConvertData();
    }
    public BlockOutputStream(OutputStream outputStream, byte delimeterByte) {
        this.outputStream = outputStream;
        this.delimeterByte = delimeterByte;
        this.convertData = new DefaultConvertData();
    }
    public BlockOutputStream(OutputStream outputStream, byte delimeterByte, IConvertData convertData) {
        this.outputStream = outputStream;
        this.delimeterByte = delimeterByte;
        this.convertData = convertData == null ? new DefaultConvertData() : convertData;
    }

    public byte getDelimeterByte() {
        return delimeterByte;
    }
    public OutputStream getOutputStream() {
        return outputStream;
    }
    public void writeData(byte[] data) throws IOException {
        this.outputStream.write(data);
        this.outputStream.flush();
    }
    public void writeOriginalBlockData(byte[] data, byte delimeterByte) throws IOException {
        this.outputStream.write(data);
        this.outputStream.write(delimeterByte);
        this.outputStream.flush();
    }
    public void writeOriginalBlockData(byte[] data) throws IOException {
        this.writeOriginalBlockData(data, this.delimeterByte);
    }

    public void writeBlockData(byte[] data, byte delimeterByte) throws IOException {
        this.outputStream.write(convertData.encryptBytes(data));
        this.outputStream.write(delimeterByte);
        this.outputStream.flush();
    }
    public void writeBlockData(byte[] data) throws IOException {
        this.writeBlockData(data, this.delimeterByte);
    }
    public void writeBlockString(String str, byte delimeterByte) throws IOException {
        this.outputStream.write(convertData.encrypt(str));
        this.outputStream.write(delimeterByte);
        this.outputStream.flush();
    }
    public void writeBlockString(String str) throws IOException {
        writeBlockString(str, this.delimeterByte);
    }
    public void writeBlockLine(byte[] data) throws IOException {
        this.writeOriginalBlockData(convertData.encryptBytes(data), NEW_LINE_BYTE);
    }
    public void writeLine(String string) throws IOException {
        byte[] data = convertData.encrypt(string);
        this.outputStream.write(data);
        this.outputStream.write(NEW_LINE_BYTE);
        this.outputStream.flush();
    }

    public void close() throws Exception {
        if (this.outputStream != null) {
            this.outputStream.close();
        }
    }

    protected byte delimeterByte;
    protected final OutputStream outputStream;
    protected IConvertData convertData;
}
