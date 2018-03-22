package nkn6294.java.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class UdpCommon {
	 public static void main(String[] args) throws Exception {
	        DatagramSocket serverSocket = new DatagramSocket(8989);
	        byte[] receiveData = new byte[1024];
	        byte[] sendData;// = new byte[1024];
	        System.out.println("UDP LISTENNING...");
	        int i = 0;
	        while(i++ < 100) {
	            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	            serverSocket.receive(receivePacket);
	            String sentence = new String(receivePacket.getData());
	            InetAddress ipAddress = receivePacket.getAddress();
	            int port = receivePacket.getPort();
	            System.out.println(String.format("RECEIVE:'%s'[%s:%d][Length:%d]" , sentence, ipAddress, port, sentence.length()));
	            sendData = sentence.toUpperCase().getBytes("UTF8");
	            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
	            serverSocket.send(sendPacket);
	        }
	        serverSocket.close();
	    }
	    
	    public static void main2(String[] args) throws Exception {
	          byte[] receiveData = new byte[1024];
	        DatagramSocket udpSocket = new DatagramSocket();
	        InetAddress ipAddress = InetAddress.getByName("255.255.255.255");
	        int port = 8989;
	        byte[] sendData = "Hello11111111111111".toUpperCase().getBytes();
	        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
	        udpSocket.send(sendPacket);
	        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        udpSocket.receive(receivePacket);
	        String receiceString = new String(receivePacket.getData());
	        port = receivePacket.getPort();
	        ipAddress = receivePacket.getAddress();
	        System.out.println("Data:" + receiceString);
	        System.out.println("IP:" + ipAddress);
	        System.out.println("Port:" + port);
	        udpSocket.close();
	    }
}
