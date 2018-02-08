package nkn6294.java.lib.io;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BlockInputStream {

    public static byte NEW_LINE_BYTE = (byte) '\n';

    public BlockInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.delimeterByte = NEW_LINE_BYTE;
        this.convertData = new DefaultConvertData();
    }

    public BlockInputStream(InputStream inputStream, byte delimeter) {
        this.inputStream = inputStream;
        this.delimeterByte = delimeter;
        this.convertData = new DefaultConvertData();
    }

    public BlockInputStream(InputStream inputStream, byte delimeter, IConvertData convertData) {
        this.inputStream = inputStream;
        this.delimeterByte = delimeter;
        if (convertData == null) {
            this.convertData = new DefaultConvertData();
        } else {
            this.convertData = convertData;
        }
    }

    public byte getDelimeter() {
        return this.delimeterByte;
    }

    public void setDelimeter(byte delimeter) {
        this.delimeterByte = delimeter;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public byte[] readData() throws IOException {
        byte[] buffer = new byte[8 * 1024];
        ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
        int count = inputStream.read(buffer);
        while (count > 0) {
            bufferArray.write(buffer, 0, count);
            try {
                count = inputStream.read(buffer);
            } catch (IOException ex) {
                break;
            }
        }
        bufferArray.flush();
        byte[] data = bufferArray.toByteArray();
        return data;
    }

    public byte[] readOriginalNextBlock(byte endBlockByte) throws IOException {
        ByteArrayOutputStream bufferData = new ByteArrayOutputStream();
        int count = inputStream.read();
        if (count < 0) {
            return null;
        }
        while (count >= 0) {
            if (count == endBlockByte) {
                break;
            }
            bufferData.write(count);
            count = inputStream.read();
        }
        bufferData.flush();
        byte[] data = bufferData.toByteArray();
        return data;
    }

    public byte[] readOriginalNextBlock() throws IOException {
        return this.readOriginalNextBlock(this.delimeterByte);
    }

    public byte[] readNextBlock(byte endBlockByte) throws IOException {
        byte[] data = readOriginalNextBlock(endBlockByte);
        return byte2Bytes(data, endBlockByte);
    }

    public byte[] readNextBlock() throws IOException {
        byte[] data = readOriginalNextBlock(delimeterByte);
        return byte2Bytes(data, delimeterByte);
    }

    public String readNextBlockString(byte endBlockByte) throws IOException {
        byte[] data = readOriginalNextBlock(endBlockByte);
        return bytes2String(data, endBlockByte);
    }

    public String readNextBlockString() throws IOException {
        byte[] data = readOriginalNextBlock(this.delimeterByte);
        return bytes2String(data, this.delimeterByte);
    }

    public byte[] readBlockLine() throws IOException {
        byte[] data = readOriginalNextBlock(NEW_LINE_BYTE);
        return byte2Bytes(data, NEW_LINE_BYTE);
    }

    public String readLine() throws IOException {
        byte[] data = this.readOriginalNextBlock(NEW_LINE_BYTE);
        return bytes2String(data, NEW_LINE_BYTE);
    }

    protected IConvertData getConvertData() {
        return this.convertData;
    }

    public void close() throws Exception {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
    }

    protected byte[] byte2Bytes(byte[] encryptData, byte delimeter) {
        byte[] content = null;
        if (encryptData == null) {
            return null;
        } else if (encryptData.length != 0) {
            content = convertData.decryptBytes(encryptData);
            if (content != null) {
                return content;
            }
        }
        try {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            if(encryptData.length == 0) {
                content = null;
            } else {
                data.write(encryptData);
            }
            byte[] nextBlock = this.readOriginalNextBlock(delimeter);
            while (content == null && nextBlock != null) {
                data.write(delimeter);
                if(nextBlock.length == 0) {
                    data.write(delimeter);
                } else {
                    data.write(nextBlock);
                }
                content = convertData.decryptBytes(data.toByteArray());
                if(content != null) {
                    break;
                }
                nextBlock = this.readOriginalNextBlock(delimeter);
            }
        } catch (IOException ex) {
        }
        return content;
    }

    protected String bytes2String(byte[] encryptData, byte delimeter) {
        String content = null;
        if (encryptData == null) {
            return null;
        } else if (encryptData.length != 0) {
            content = convertData.decrypt(encryptData);
            if (content != null) {
                return content;
            }
        }
        try {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            if(encryptData.length != 0) {
                data.write(encryptData);
            }
            byte[] nextBlock = this.readOriginalNextBlock(delimeter);
            while (content == null && nextBlock != null) {
                data.write(delimeter);
                if (nextBlock.length != 0) {
                    data.write(nextBlock.length);
                } else {
                    data.write(delimeter);
                }
                content = convertData.decrypt(data.toByteArray());
                if(content != null) {
                    break;
                }
                nextBlock = this.readOriginalNextBlock(delimeter);
            }
        } catch (IOException ex) {
        }
        return content;
    }

    protected byte delimeterByte;
    protected final InputStream inputStream;
    protected IConvertData convertData;

}
