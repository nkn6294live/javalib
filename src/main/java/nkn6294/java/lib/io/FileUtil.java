package nkn6294.java.lib.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileUtil {
	
	public static void simpleJoinFile(String inputFolderPath, String outputPath, int maxIndex) throws Exception {
		File inputFolder = new File(inputFolderPath);
		File outFile = new File(outputPath);
		FileOutputStream output=  new FileOutputStream(outFile);
		byte[] buffer = new byte[1 << 11];
		int count = 0;
		for (int index = 0; index <= maxIndex; index++) {
			FileInputStream input = new FileInputStream(String.format("%s/%s.part%d", inputFolder.getAbsolutePath(), outFile.getName(), index));
			while ((count = input.read(buffer)) > 0) {
				output.write(buffer, 0, count);
			}
			input.close();
		}
		output.close();
	}
		
	public static void joinFile(String inputFolder, String outputFile) throws Exception {
		File folder = new File(inputFolder);
		if (!folder.exists()) {
			throw new IllegalArgumentException("Input folder not exited!");
		};
		File file = new File(outputFile);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		ArrayList<File> files = new ArrayList<>();
		int max = -1;
		for (File f : folder.listFiles()) {
			String filePath = f.getAbsolutePath();
			int pos = filePath.lastIndexOf("part");
			if (pos < 0 || pos == filePath.length() - 4) {
				continue;
			} else {
				try {
					int number = Integer.parseInt(filePath.substring(pos + 4));
					if (number > max) {
						for (int index = max + 1; index < number; index++) {
							files.add(null);
						}
						files.add(f);
						max = number;
					} else {
						files.add(number, file);
					}
				} catch(Exception ex) {
					continue;
				}
			}
		}
		FileOutputStream output = new FileOutputStream(file);
		byte[] buffer = new byte[1 << 11];
		for (int index = 0; index <= max; index++) {
			FileInputStream input = new FileInputStream(files.get(index));
			int c = 0;
			while ((c = input.read(buffer, 0, buffer.length)) > 0) {
				output.write(buffer, 0, c);
			}
			input.close();
		}
		output.close();
	}
	
	public static void simpleSpitFile(String filePath, String outputFolder, long maxSizePart) throws Exception {
		File file = new File(filePath);
		File folder = new File(outputFolder);
		if (!(file.exists() || folder.exists() || maxSizePart <= 0)) {
			throw new IllegalArgumentException("Input file, folder not exited or sizePart <= 0");
		}
		final FileInputStream fileInputStream = new FileInputStream(filePath);
		long length = 0;
		int index = 0 ;
		do {
			String newFile = String.format("%s\\%s.part%d", folder.getAbsoluteFile(), file.getName(), index++);
			FileOutputStream output = new FileOutputStream(newFile);
			length = Copy(fileInputStream, output, maxSizePart);
			output.close();
		} while(length == maxSizePart);
		fileInputStream.close();
	}
	/***
	 * Copy từ input tới output với số lượng byte cụ thể.
	 * 
	 * @param input
	 *            Đầu vào dữ liệu
	 * @param output
	 *            Đầu ra dữ liệu.
	 * @param maxSize
	 *            Số lượng byte cần copy.
	 * @return Số lượng byte đọc được (<= maxSize), 0 nếu kết thúc input, 0 nếu
	 *         maxSize <= 0
	 * @throws IOException
	 *             : nếu input hoặc output đã đóng.
	 */
	public static long Copy(InputStream input, OutputStream output, long maxSize)
			throws IOException {
		if (maxSize <= 0) {
			return 0;
		}
		long remainLength = maxSize;
		long sum = 0;
		int lengthBuffer = 1 << 14; // 16K
		int count;
		byte[] buffer = new byte[lengthBuffer];
		try {
			while ((count = input.read(buffer, 0, remainLength >= lengthBuffer ? lengthBuffer : (int) remainLength)) > 0) {
				output.write(buffer, 0, count);
				sum += count;
				remainLength -= sum;
			}
			output.flush();
		} catch (IOException ex) {
			if (sum == 0) {
				throw ex;
			}
			return sum;
		}
		return sum;
	}

	/***
	 * Copy Block từ input tới output. Đọc ghi đồng thời. Số byte đọc được đều
	 * ghi ngay lập tức vào output.
	 * 
	 * @param input 		: Đầu vào dữ liệu.
	 * @param output 		: Đầu ra dữ liệu.
	 * @param maxSize		: số lượng byte cần copy.
	 * @return 				: copy thành công hay không.
	 * @throws IOException	: quá trình đọc, ghi dữ liệu lỗi.
	 */
	public static boolean CopyBlockSync(InputStream input, OutputStream output,
			long maxSize) throws IOException {
		if (maxSize <= 0) {
			return false;
		}
		long remainLength = maxSize;
		long sum = 0;
		int lengthBuffer = 1 << 14; // 16K
		int count;
		byte[] buffer = new byte[lengthBuffer];
		try {
			while ((count = input.read(buffer, 0,
					remainLength >= lengthBuffer ? lengthBuffer : (int) remainLength)) > 0) {
				output.write(buffer, 0, count);
				output.flush();
				sum += count;
				remainLength -= sum;
			}
		} catch (IOException ex) {
			return false;
		}
		return true;
	}
	/***
	 * Copy Block từ input tới output. Khi đã đọc đủ dữ liệu mới ghi vào output.
	 * Sử dụng với maxSize nhỏ (Int32) sử dụng buffer trên bộ nhớ trong.
	 * 
	 * @param input			: Đầu vào dữ liệu.
	 * @param output 		: Đầu ra dữ liệu.
	 * @param maxSize		: số lượng byte cần copy.
	 * @return 				: copy thành công hay không.
	 * @throws IOException	: quá trình đọc, ghi dữ liệu lỗi.
	 */
	public static boolean CopyBlockASync(InputStream input,
			OutputStream output, int maxSize) throws IOException {
		try {
			ByteArrayOutputStream templateOutput = new ByteArrayOutputStream();
			if (!CopyBlockASync(input, templateOutput, maxSize)) {
				return false;
			}
			ByteArrayInputStream templateInputStream = new ByteArrayInputStream(templateOutput.toByteArray());
			return CopyBlockSync(templateInputStream, output, maxSize);
		} catch (IOException ex) {
			throw ex;
		} catch (Exception ex) {
			return false;
		}
	}
	/***
	 * Copy Block từ input tới output. Khi đã đọc đủ dữ liệu mới ghi vào output.
	 * Sử dụng với maxSize lớn (Long) và lưu tạm trong template file.
	 * @param input			: Đầu vào dữ liệu.
	 * @param output 		: Đầu ra dữ liệu.
	 * @param maxSize		: số lượng byte cần copy.
	 * @return 				: copy thành công hay không.
	 * @throws IOException	: quá trình đọc, ghi dữ liệu lỗi.
	 */
	public static boolean CopyBlockASync(InputStream input,
			OutputStream output, long maxSize) throws IOException {
		File file = File.createTempFile("_", "tmp");
		BufferedOutputStream templateOutput = null;
		BufferedInputStream templateInputStream = null;
		try {
			templateOutput = new BufferedOutputStream(new FileOutputStream(file));
			if (!CopyBlockASync(input, templateOutput, maxSize)) {
				return false;
			}
			templateOutput.close();
			templateOutput = null;
			templateInputStream = new BufferedInputStream(new FileInputStream(file));
			return CopyBlockSync(templateInputStream, output, maxSize);
		} catch (IOException ex) {
			throw ex;
		} catch (Exception ex) {
			return false;
		} finally {
			if (templateOutput != null) {
				templateOutput.close();
			}
			if (templateInputStream != null) {
				templateInputStream.close();
			}
			file.delete();
		}
	}

	public static byte[] readFile(String filePath, int maxLengthFile)
			throws Exception {
		byte[] buffer = new byte[maxLengthFile];
		FileInputStream fileInputStream = new FileInputStream(filePath);
		int count = fileInputStream.read(buffer);
		if (count > 0) {
			byte[] result = new byte[count];
			System.arraycopy(buffer, 0, result, 0, count);
			fileInputStream.close();
			return result;
		}
		fileInputStream.close();
		return null;
	}

	public static void writeFile(String filePath, byte[] data) throws Exception {
		FileOutputStream fileOutput = new FileOutputStream(filePath);
		fileOutput.write(data);
		fileOutput.flush();
		fileOutput.close();
	}

	
}
