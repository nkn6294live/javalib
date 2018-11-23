package nkn6294.java.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class TCPServer extends Thread {
	public final static int DEFAULT_SERVER_PORT = 9999;

	public TCPServer() {
		this(DEFAULT_SERVER_PORT);
	}

	public TCPServer(int port) {
		this.port = port;
		serverSocket = null;
		this.name = this.getClass().getSimpleName();
		this.description = this.name;
	}
	
	public TCPServer(int port, String serverName, String description) {
		this.port = port;
		serverSocket = null;
		this.name = serverName;
		this.description = description;
	}
	
	public int getPort() {
		return port;
	}
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println(String.format("Server %1$s[%2$s] started at port %3$s...", this.name, this.description, this.port));
			while (!isInterrupted()) {
				Socket socket = serverSocket.accept();
				Runnable clientThread = CreateClientThread(socket);
				if (clientThread != null) {
					new Thread(clientThread).start();
				}
			}
		} catch (IOException e) {
			System.out.println("Server is stopped: " + e.getMessage());
		}
	}
	
	
	
	public String getServerName() {
		return name;
	}

	public void setServerName(String name) {
		this.name = name;
	}

	public String getServerDescription() {
		return description;
	}

	public void setServerDescription(String description) {
		this.description = description;
	}

	public abstract Runnable CreateClientThread(Socket socket);

	protected ServerSocket serverSocket;
	protected int port;
	protected String name = "";
	protected String description = "";
}
