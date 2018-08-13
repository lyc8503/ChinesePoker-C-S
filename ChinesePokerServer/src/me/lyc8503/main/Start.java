package me.lyc8503.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

public class Start {
	
	public static String serverPort;
	public static String roomNum;
	public static Vector roomVec = new Vector();
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("Chinese Poker Server");
		File confFile = new File("poker.prop");
		if(!confFile.exists()){
			System.out.println("Config Not Found!");
			confFile.createNewFile();
			Scanner sc = new Scanner(System.in);
			System.out.print("Server Port:");
			String port = sc.nextLine();
			System.out.print("Room Number:");
			String room = sc.nextLine();
			sc.close();
			FileOutputStream out = new FileOutputStream(confFile);
			String str = ("port=" + port + "\nroom=" + room);
			out.write(str.getBytes());
			out.close();
		}
		System.out.println("Loading Config...");
		Properties prop = new Properties();
		prop.load(new FileInputStream(confFile));
		serverPort = prop.getProperty("port");
		roomNum = prop.getProperty("room");
		System.out.println("OK! Starting Server...");
		SocketConnection.init();
		
		for(int i = 1; i <= Integer.parseInt(roomNum); i ++){
			roomVec.addElement(new Room(i));
		}
		
		Thread roomChecker = new Thread(new Runnable() {
			
			public void run() {
				while(true){
					try {
						for (int i = 0; i < roomVec.size(); i++) {
							Room room = (Room) roomVec.get(i);
							try{
								if(room.p1.socket.isClosed()) room.p1 = null;
								if(room.p2.socket.isClosed()) room.p2 = null;
								if(room.p3.socket.isClosed()) room.p3 = null;
							}catch(Exception e){
								//Ignore
							}
						}
						
						for(int i = 0; i < SocketConnection.playerList.size(); i++){
							Player p = (Player) SocketConnection.playerList.get(i);
							if(p.socket.isClosed()){
								System.out.println("Player " + p.nickName + " is removed!");
								SocketConnection.playerList.remove(i);
							}
						}
						
						Thread.sleep(5000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		roomChecker.start();
		
	}

}
