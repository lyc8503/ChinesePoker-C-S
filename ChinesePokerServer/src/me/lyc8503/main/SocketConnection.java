package me.lyc8503.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class SocketConnection {
	
	public static ServerSocket serverSocket;
	public static Vector playerList = new Vector();
	public static Thread thread = new Thread(new Runnable() {
		
		public void run() {
			while(true){
				try {
					final Socket socket = serverSocket.accept();
					new Thread(new Runnable() {
						public void run() {
							try {
								playerList.addElement(new Player(socket));
							} catch (IOException e) {
								e.printStackTrace();
							}	
						}
					}).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	public static void init() throws NumberFormatException, IOException{
		serverSocket = new ServerSocket(Integer.parseInt(Start.serverPort));
		thread.start();
	}

}
