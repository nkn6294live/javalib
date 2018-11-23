package nkn6294.java.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class ClientThread implements Runnable {
	protected ClientThread(Socket socket) {
		this.socket = socket;
		this.socketInfo = "[" + this.socket.getInetAddress() + ":"
				+ this.socket.getPort() + "]";
		this.isStart = false;
		this.isStop = true;
		this.isError = false;
		try {
			this.dataInputStream = new DataInputStream(
					this.socket.getInputStream());
			this.networkReader = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream(), "UTF-8"));
			this.dataOutputStream = new DataOutputStream(
					this.socket.getOutputStream());
			this.networkWriter = new BufferedWriter(new OutputStreamWriter(
					this.socket.getOutputStream(), "UTF-8"));
		} catch (IOException e) {
			isError = true;
		}
	}

	public Socket getSocket() {
		return this.socket;
	}

	public boolean getErrorStatus() {
		return isError;
	}

	public boolean isStarted() {
		return isStart;
	}

	public boolean isStopted() {
		return isStop;
	}

	public abstract void exec() throws Exception;

	public abstract void freeResource() throws Exception;

	@Override
	public void run() {
		if (isError) {
			return;
		}
		try {
			exec();
			closeResource();
		} catch (Exception e) {
			isError = true;
		} finally {
			closeResource();
		}
	}

	protected void closeResource() {
		try {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (dataOutputStream != null) {
				dataOutputStream.close();
			}
			freeResource();
		} catch (Exception ex) {
			isError = true;
		}

	}

	protected void sendResponse(String response) {
		try {
			this.networkWriter.write(response + "\n");
			this.networkWriter.flush();
			System.out.println("[RESPONSE]" + socketInfo + ":" + response);
		} catch (IOException e) {
			printExceptionInfo(e);
		}
	}

	protected String nextRequest() {
		try {
			return this.networkReader.readLine();
		} catch (IOException ex) {
			printExceptionInfo(ex);
			return null;
		}
	}

	protected void printExceptionInfo(Exception e) {
		System.out.println(e.getClass().getName() + ":" + e.getMessage());
	}

	protected Socket socket;
	protected String socketInfo;
	protected DataInputStream dataInputStream;
	protected DataOutputStream dataOutputStream;
	protected BufferedReader networkReader;
	protected BufferedWriter networkWriter;
	protected boolean isStart;
	protected boolean isStop;
	protected boolean isError;
}
