package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SerialPortCom {
	
	Socket clientSocket;
	DataOutputStream outToServer;
	
	public SerialPortCom(String ip, int port) {
		  
		try {
			InetAddress address = InetAddress.getByName(ip);
			clientSocket = new Socket(address, port);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}

	
	public void sendData(String data) {
	
		try {
			outToServer.writeBytes(data + '\n');
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
