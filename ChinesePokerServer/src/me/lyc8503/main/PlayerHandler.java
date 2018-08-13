package me.lyc8503.main;

import java.io.IOException;

public class PlayerHandler {
	
	public static void handle(Player p) throws IOException{
		while(true){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1){
				e1.printStackTrace();
			}
			String command = null;
			if(p.roomId == -1){
				command = p.input.readLine();
			}
			if(command != null){
				if(command.equals("hub")){
					for(int i = 0; i < Start.roomVec.size(); i ++){
						p.output.print(((Room)(Start.roomVec.get(i))).toString() + "\n");
					}
					p.output.flush();
				}else if(command.contains("join")){
					try {
						int roomId = Integer.parseInt(command.split("@") [1]);
						if(((Room)(Start.roomVec.get(roomId - 1))).join(p)){
							p.output.print("Success: You are now in Room" + (roomId));
							p.output.flush();
						}else{
							p.output.print("Fail: The room is full.");
							p.output.flush();
						}
					} catch (Exception e) {
						p.output.print("Fail: Your request is forbidden.");
						p.output.flush();
					}
				}else {
					p.socket.close();
					throw new IOException("Illegal Client");
				}
			}
		}
	}
}
