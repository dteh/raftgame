package raftgame;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
	private static Scanner input;

	public static void main(String[] args) throws InterruptedException,IOException{
		input = new Scanner(System.in);
		System.out.println("Welcome!");
		System.out.println("1- Create game");
		System.out.println("2- Join existing game");
		System.out.println("*********************");
		String decision = input.nextLine();
		
		// Create a game
		if(decision.equals("1")){
			System.out.println("Choose a game name..");
			String gameName = input.nextLine();
			System.out.println("Board size?");
			GameBoard board = new GameBoard(Integer.parseInt(input.nextLine()));
			System.out.println("Creating game..");
			//uncomment when trying cluster functionality
			try{
				//System.setProperty("java.net.preferIPv4Stack" , "true");
				raft.API.setState(board);
				raft.API.init(new InstApplier(), gameName);
			}catch(Exception e){
				e.printStackTrace();
			}
			Game main = new Game(board);
			main.start();
		
		// Join a game -- not working TODO: fix multiplayer
		}else{
			System.out.println("Enter exiting game name");
			String gameName = input.nextLine();
			System.out.println("Connecting..");
			
			
			try{
				//System.setProperty("java.net.preferIPv4Stack" , "true");	
				raft.API.init(new InstApplier(), gameName);
			}catch(Exception e){
				e.printStackTrace();
			}
			Thread.sleep(5000);
			Game main = new Game(((GameBoard)raft.RaftNode.getStateObject()));
			main.start();	
			
						
		}
	}	
}
