package me.lyc8503.main;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;


public class Room {
	public Player p1;
	public Player p2;
	public Player p3;
	public int id;
	public String status = "Waiting for players...";
	public Thread roomManager;
	public Room thisRoom;
	
	public static String numToCard(int i){
		if(i == 1){
			return "A";
		}else if(i >= 2 && i <= 10){
			return String.valueOf(i);
		}else if(i >= 11 && i <= 13){
			if(i == 11){
				return "J";
			}else if(i == 12){
				return "Q";
			}else if(i == 13){
				return "K";
			}
		}else if(i == 14){
			return "SJoker";
		}else if(i == 15){
			return "BJoker";
		}
		
		return "Error";
	}
	
	public static int cardToNum(String s){
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			if(s.equals("A")) return 1;
			if(s.equals("J")) return 11;
			if(s.equals("Q")) return 12;
			if(s.equals("K")) return 13;
			if(s.equals("SJoker")) return 14;
			if(s.equals("BJoker")) return 15;
		}
		
		return -1;
	}
	
	public Room(int id){
		this.id = id;
		thisRoom = this;
		roomManager = new Thread(new Runnable() {
			
			public void run() {
				while(true){
					if(thisRoom.getPlayerNum() == 3 && status.equals("Waiting for players...")){
						status = "Starting...";
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						}
						p1.output.write("ready");
						p1.output.flush();
						p2.output.write("ready");
						p2.output.flush();
						p3.output.write("ready");
						p3.output.flush();
						
						status = "In Game";
						
						Vector allCards = new Vector();
						for(int i = 1; i <= 13; i ++){
							for(int i1 = 1; i1 <= 4; i1++){
								allCards.add(numToCard(i));
							}
						}
						
						allCards.addElement(numToCard(14));
						allCards.addElement(numToCard(15));
						
						System.out.println("Room" + thisRoom.id +" Game Start!");
						for(int i = 1; i <= 5000; i ++){
							Random r1 = new Random();
							int a = r1.nextInt(54);
							int b = r1.nextInt(54);
							String temp = (String) allCards.elementAt(b);
							allCards.set(b, allCards.elementAt(a));
							allCards.set(a, temp);
						}
						
						System.out.print("Room Card:");
						for(int i = 0; i <= 53; i ++){
							System.out.print(allCards.elementAt(i) + " ");
						}
						System.out.println();
						
						for(int i = 0; i <=51; i ++){
							if(i % 3 == 0){
								p1.cardsVec.addElement(allCards.elementAt(i));
							};
							if(i % 3 == 1){
								p2.cardsVec.addElement(allCards.elementAt(i));
							}
							if(i % 3 == 2){
								p3.cardsVec.addElement(allCards.elementAt(i));
							}
						}
						
						orderCard(p1.cardsVec);
						orderCard(p2.cardsVec);
						orderCard(p3.cardsVec);
						
						for(int i = 0; i <=16; i ++){
							p1.output.print(p1.cardsVec.elementAt(i) + " ");
							p2.output.print(p2.cardsVec.elementAt(i) + " ");
							p3.output.print(p3.cardsVec.elementAt(i) + " ");
						}
						p1.output.flush();
						p2.output.flush();
						p3.output.flush();
						
						Player landlordPlayer = null;
						
						try {
							boolean p1Landlord = p1.input.readLine().equals("T") ? true:false;
							boolean p2Landlord = p2.input.readLine().equals("T") ? true:false;
							boolean p3Landlord = p3.input.readLine().equals("T") ? true:false;
							
							System.out.println(p1Landlord + " " + p2Landlord + " " + p3Landlord);
							int counter = 0;
							if(p1Landlord) counter ++;
							if(p2Landlord) counter ++;
							if(p3Landlord) counter ++;
							
							if(counter == 1){
								if(p1Landlord){
									landlordPlayer = p1;
								}
								
								if(p2Landlord){
									landlordPlayer = p2;
								}
								
								if(p3Landlord){
									landlordPlayer = p3;
								}
							}else if(counter == 2){
								if(p1Landlord && p2Landlord){
									if(new Random().nextBoolean()){
										landlordPlayer = p1;
									}else {
										landlordPlayer = p2;
									}
								}
								
								if(p1Landlord && p3Landlord){
									if(new Random().nextBoolean()){
										landlordPlayer = p1;
									}else {
										landlordPlayer = p3;
									}
								}
								
								if(p3Landlord && p2Landlord){
									if(new Random().nextBoolean()){
										landlordPlayer = p3;
									}else {
										landlordPlayer = p2;
									}
								}
							}else {
								int ran = new Random().nextInt(3) + 1;
								if(ran == 1){
									landlordPlayer = p1;
								}else if(ran == 2){
									landlordPlayer = p2;
								}else {
									landlordPlayer = p3;
								}
							}
							
							landlordPlayer.output.print("landlord");
							landlordPlayer.output.flush();
							landlordPlayer.cardsVec.addElement(allCards.elementAt(51));
							landlordPlayer.cardsVec.addElement(allCards.elementAt(52));
							landlordPlayer.cardsVec.addElement(allCards.elementAt(53));
							
							p1.output.print("The player " + landlordPlayer.nickName + " get the landlord.Got Card:" + allCards.elementAt(51) + " " + allCards.elementAt(52) + " " + allCards.elementAt(53));
							p1.output.flush();
							p2.output.print("The player " + landlordPlayer.nickName + " get the landlord.Got Card:" + allCards.elementAt(51) + " " + allCards.elementAt(52) + " " + allCards.elementAt(53));
							p2.output.flush();
							p3.output.print("The player " + landlordPlayer.nickName + " get the landlord.Got Card:" + allCards.elementAt(51) + " " + allCards.elementAt(52) + " " + allCards.elementAt(53));
							p3.output.flush();

							orderCard(p1.cardsVec);
							orderCard(p2.cardsVec);
							orderCard(p3.cardsVec);
							
							boolean winFlag = false;
							while(!winFlag){
								p1.output.write("Your card(s):" + p1.cardsVec.toString());
								p1.output.flush();
								p2.output.write("Your card(s):" + p2.cardsVec.toString());
								p2.output.flush();
								p3.output.write("Your card(s):" + p3.cardsVec.toString());
								p3.output.flush();

								String p1Res = p1.input.readLine();
								if(p1Res == "pass"){
									printAll("Player " + p1.nickName + " Pass!");
								}else{
									String[] recvCards = p1Res.split(" ");
									boolean ifIllegal = false;
									for(int i = 0; i < recvCards.length; i ++){
										boolean ifCheck = false;
										inner: for(int i1 = 0; i1 < p1.cardsVec.size(); i1 ++){
											if(recvCards[i].equals(p1.cardsVec.elementAt(i1))){
												ifCheck = true;
												break inner;
											}
										}
										
										if(!ifCheck) ifIllegal = true;
									}
									
									if(ifIllegal){
										p1.output.print("Error: Your request is forbidden.");
										p1.output.flush();
									}else{
										printAll("Player " + p1.nickName + " card(s):" + p1Res);
									}
								}
								
								String p2Res = p2.input.readLine();
								if(p2Res == "pass"){
									printAll("Player " + p2.nickName + " Pass!");
								}else{
									String[] recvCards = p2Res.split(" ");
									boolean ifIllegal = false;
									for(int i = 0; i < recvCards.length; i ++){
										boolean ifCheck = false;
										inner: for(int i1 = 0; i1 < p2.cardsVec.size(); i1 ++){
											if(recvCards[i].equals(p2.cardsVec.elementAt(i1))){
												ifCheck = true;
												break inner;
											}
										}
										
										if(!ifCheck) ifIllegal = true;
									}
									
									if(ifIllegal){
										p2.output.print("Error: Your request is forbidden.");
										p2.output.flush();
									}else{
										printAll("Player " + p2.nickName + " card(s):" + p2Res);
									}
								}
								
								String p3Res = p3.input.readLine();
								if(p3Res == "pass"){
									printAll("Player " + p3.nickName + " Pass!");
								}else{
									String[] recvCards = p3Res.split(" ");
									boolean ifIllegal = false;
									for(int i = 0; i < recvCards.length; i ++){
										boolean ifCheck = false;
										inner: for(int i1 = 0; i1 < p3.cardsVec.size(); i1 ++){
											if(recvCards[i].equals(p3.cardsVec.elementAt(i1))){
												ifCheck = true;
												break inner;
											}
										}
										
										if(!ifCheck) ifIllegal = true;
									}
									
									if(ifIllegal){
										p3.output.print("Error: Your request is forbidden.");
										p3.output.flush();
									}else{
										printAll("Player " + p3.nickName + " card(s):" + p1Res);
									}
								}
							}
							
						} catch (IOException e) {
							try {
								p1.output.print("Error!");
								p1.output.flush();
								p2.output.print("Error!");
								p2.output.flush();
								p3.output.print("Error!");
								p3.output.flush();
							} catch (Exception e2) {
								// Ingore
							}
							e.printStackTrace();
							thisRoom.kickAll();
							thisRoom.status = "Waiting for players...";
						}
					}
				}
			}
		});
		roomManager.start();
	}
	
	public int convert(String s){
		if(s.equals("3")) return 1;
		if(s.equals("4")) return 2;
		if(s.equals("5")) return 3;
		if(s.equals("6")) return 4;
		if(s.equals("7")) return 5;
		if(s.equals("8")) return 6;
		if(s.equals("9")) return 7;
		if(s.equals("10")) return 8;
		if(s.equals("J")) return 9;
		if(s.equals("Q")) return 10;
		if(s.equals("K")) return 11;
		if(s.equals("A")) return 12;
		if(s.equals("2")) return 13;
		if(s.equals("SJoker")) return 14;
		if(s.equals("BJoker")) return 15;
		
		return 666;
	}
	
	public void orderCard(Vector cards){
		Comparator c = new Comparator() {
			public int compare(Object card1, Object card2){
				return convert((String) card1) - convert((String) card2);
			}
		};
		Collections.sort(cards, c);
	}
	
	public int getPlayerNum(){
		int counter = 0;
		if(p1 != null) counter ++;
		if(p2 != null) counter ++;
		if(p3 != null) counter ++;
		return counter;
	}
	
	public String toString(){
		int counter = 0;
		if(p1 != null) counter ++;
		if(p2 != null) counter ++;
		if(p3 != null) counter ++;
		return "Room" + this.id + " " + counter + "/3 " + this.status;
	}
	
	public boolean join(Player player){
		System.out.println("Player " + player.nickName + " attempts to join Room" + this.id);
		if(p1 == null){
			p1 = player;
			player.roomId = this.id - 1;
			return true;
		}else if(p2 == null){
			p2 = player;
			player.roomId = this.id - 1;
			return true;
		}else if(p3 == null){
			p3 = player;
			player.roomId = this.id - 1;
			return true;
		}else {
			return false;
		}
	}
	
	public void printAll(String s){

		p1.output.print(s);
		p1.output.flush();
		p2.output.print(s);
		p2.output.flush();
		p3.output.print(s);
		p3.output.flush();
	}
	
	public void kickAll(){
		try{
			p1.cardsVec.clear();
			p2.cardsVec.clear();
			p3.cardsVec.clear();
			p1.roomId = -1;
			p2.roomId = -1;
			p3.roomId = -1;
		}catch(Exception e){
			// Ignore
		}
		p1 = null;
		p2 = null;
		p3 = null;
	}
}
