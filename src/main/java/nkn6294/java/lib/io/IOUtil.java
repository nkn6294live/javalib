package nkn6294.java.lib.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public final class IOUtil{
    
	public enum FreeResourceType{
        FREE_INPUT,
        FREE_OUTPUT,
        FREE_ALL,
        NONE
    }

    static{
        DEFAULT_COPY_ACTION = new MonitorValue(){
            @Override
            public void Action(long value){
            }
        };
    }

    public static int DEFAULT_BUFFER_SIZE_ARRAY = 8192;
    public static FreeResourceType DEFAULT_FREE_RESOURCE = FreeResourceType.FREE_ALL;
    public static long MAX_SIZE_RESOURCE = 419430400; // 400 * 1024 * 1024 // 400M
    public static String DEFAULT_ENCODING = "UTF-8";
    public final static MonitorValue DEFAULT_COPY_ACTION;

    /***
     * Copy dữ liệu từ <i>input</i> tới <i>output</i> với các tùy chọn.
     *
     * @param input        <i>Dữ liệu đầu vào.</i>
     * @param output       <i>Dữ liệu đầu ra. </i>
     * @param bufferSize   <i>Độ lớn của mảng bufferSize. Nếu nhỏ hơn hoặc bằng 0 sử dụng bufferSize mặc định.</i>
     * @param freeResource <i>Kiểu giải phóng tài nguyên: không giải phóng, giải phóng đầu vào, giải
     *                     phóng đầu ra, giải phóng cả hai. Sau khi tiến hành copy.</i>
     * @param copyAction   <i>Đối tượng xử lý độ lớn dữ liệu đã được copy khi đọc xong một block dữ liệu
     *                     (phụ thuộc vào <b>bufferSize</b>). Viết đè phương thức <u>Action(long value)</u> để xử lý.</i>
     * @return Trạng thái copy dữ liệu. <i>True</i> nếu thành công, <i>False</i> nếu thất bại.
     * @throws IOException Xảy ra khi có vấn đề trong việc đọc và ghi dữ liệu.
     */
    public static boolean Copy(InputStream input, OutputStream output, int bufferSize, FreeResourceType freeResource, MonitorValue copyAction) throws IOException{
        if(input == null || output == null){
            return false;
        }
        if(bufferSize <= 0){
            bufferSize = DEFAULT_BUFFER_SIZE_ARRAY;
        }
        BufferedInputStream bufferInput = null;
        BufferedOutputStream bufferOutput = null;
        try{
            bufferInput = new BufferedInputStream(input);
            bufferOutput = new BufferedOutputStream(output);
            byte[] arr = new byte[bufferSize];
            int count = bufferInput.read(arr);//, 0, bufferSize);
            long length = 0;
            while(count != -1){
                length += count;
                if(length > MAX_SIZE_RESOURCE){
                    throw new Exception("Input size too big!");
                }
                if(copyAction != null){
                    copyAction.Action(length);
                }
                bufferOutput.write(arr, 0, count);
                count = bufferInput.read(arr);
            }
            return true;
        }catch(Exception e){
//			Log.e(e.getClass().getName(), e.getMessage());
            return false;
        }finally{
            if(freeResource != FreeResourceType.NONE){
                if(freeResource == FreeResourceType.FREE_ALL ||
                        freeResource == FreeResourceType.FREE_INPUT){
                    if(bufferInput != null){
                        bufferInput.close();
                    }
                    if(input != null){
                        input.close();
                    }
                }
                if(freeResource == FreeResourceType.FREE_ALL ||
                        freeResource == FreeResourceType.FREE_OUTPUT){
                    if(bufferOutput != null){
                        bufferOutput.close();
                    }
                    if(output != null){
                        output.close();
                    }
                }
            }
        }
    }

    public static boolean Copy(InputStream input, OutputStream output, FreeResourceType freeResource, MonitorValue copyAction) throws IOException{
        return Copy(input, output, DEFAULT_BUFFER_SIZE_ARRAY, freeResource, copyAction);
    }

    public static boolean Copy(InputStream input, OutputStream output, FreeResourceType freeResource) throws IOException{
        return Copy(input, output, DEFAULT_BUFFER_SIZE_ARRAY, freeResource, DEFAULT_COPY_ACTION);
    }

    public static boolean Copy(InputStream input, OutputStream output) throws IOException{
        return Copy(input, output, DEFAULT_BUFFER_SIZE_ARRAY, DEFAULT_FREE_RESOURCE, DEFAULT_COPY_ACTION);
    }

    /***
     * Copy dữ liệu từ <i>input</i> tới <i>output</i> với các tùy chọn.
     *
     * @param freeResource <i>Trạng thái giải phóng dữ liệu. True giải phóng đầu vào và ra. False không giải phóng.</i>
     * @return Trạng thái copy dữ liệu. <i>True</i> nếu thành công, <i>False</i> nếu thất bại.
     * @throws IOException Xảy ra khi có vấn đề trong việc đọc và ghi dữ liệu.
     */
    public static boolean Copy(InputStream input, OutputStream output, boolean freeResource) throws IOException{
        FreeResourceType freeResourceType = freeResource ? FreeResourceType.FREE_ALL : FreeResourceType.NONE;
        return Copy(input, output, DEFAULT_BUFFER_SIZE_ARRAY, freeResourceType, DEFAULT_COPY_ACTION);
    }

	public static byte[] getData(InputStream inputStream, int contentLength) throws IOException, NullPointerException {
		if (inputStream == null) {
			throw new NullPointerException();
		} 
		if(contentLength <= 0) {
			return new byte[] {};
		}
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		byte[] buffer = new byte[1 << 10];
		int count = 0;
		long remain = contentLength;
		while (remain >= buffer.length && (count = inputStream.read(buffer, 0, buffer.length)) > 0) {
			remain -= count;
			byteOutput.write(buffer, 0, count);
		}
		while (remain > 0 && (count = inputStream.read(buffer, 0, (int) remain)) > 0) {
			remain -= count;
			byteOutput.write(buffer, 0, count);
		}
		return byteOutput.toByteArray();
	}
    public static boolean Copy(Reader reader, Writer writer) throws IOException{
    	//TODO copy reader to writer.
        return false;
    }

    public static String LengthToSize(long lengthLong){
        String suffix = "B";
        double length = (double) lengthLong;
        if(length >= 1024){
            length /= 1024;
            suffix = "K";
        }else{
            return String.format("%.0f%s", length, suffix);
        }
        if(length >= 1024){
            length /= 1024;
            suffix = "M";
        }else{
            return String.format("%.0f%s", length, suffix);
        }
        if(length >= 1024){
            length /= 1024;
            suffix = "G";
        }else{
            return String.format("%.02f%s", length, suffix);
        }
        if(length >= 1024){
            length /= 1024;
            suffix = "T";
            return String.format("%.02f%s", length, suffix);
        }else{
            return String.format("%.02f%s", length, suffix);
        }
    }


}

