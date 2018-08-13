package me.lyc8503.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class Player {
	
	public Socket socket;
	public BufferedReader input;
	public PrintWriter output;
	public String nickName;
	public String status = "free";
	public Player thisPlayer;
	public int roomId = -1;
	public Vector cardsVec;
	
	public Player(Socket socket) throws IOException{
		System.out.println("A new player Connected!");
		cardsVec = new Vector();
		this.socket = socket;
		output = new PrintWriter(socket.getOutputStream());
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		thisPlayer = this;
		
		if(input.readLine() != "pokerClient"){
			output.print("OK");
			output.flush();
			nickName = input.readLine();
			System.out.println("Player nick name:" + nickName);
			new Thread(new Runnable() {
				
				public void run() {
					try {
						PlayerHandler.handle(thisPlayer);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}else{
			throw new IOException("Illegal Client");
		}
	}

}
