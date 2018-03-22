package nkn6294.java.serial;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
public class CheckPort {

    public static void main2(String[] args) {
//        getListPort();
//        sendSMS("CheckPortClass Message");
//        makeCall("840962640096");
    }
    public static void getListPort() {
        String[] portNames = SerialPortList.getPortNames();
        for(int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }
    public static boolean makeCall() {
        SerialPort serialPort = new SerialPort("COM3");
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600, 
                    SerialPort.DATABITS_8, 
                    SerialPort.STOPBITS_1, 
                    SerialPort.PARITY_NONE);
            serialPort.writeString("ATD84962640096;" + (char)13);
        } catch(SerialPortException ex) {
            System.out.println("SerialPortException:" + ex.getMessage());
        }
        return false;
    }
    public static boolean sendSMS(String message) {
        boolean success = false;
        SerialPort serialPort = new SerialPort("COM3");
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600, 
                    SerialPort.DATABITS_8, 
                    SerialPort.STOPBITS_1, 
                    SerialPort.PARITY_NONE);
                success = serialPort.writeBytes("AT+CMGS=".getBytes());
                success = success && serialPort.writeString("\"84962640096\"" + (char)13);
                success = success && serialPort.writeString(message + (char)26);
	        serialPort.closePort();//Close serial port

        } catch(SerialPortException ex) {
            System.out.println("SerialPortException:" + ex.getMessage());
        }
        return success;
    }
}