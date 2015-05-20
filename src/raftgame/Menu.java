package raftgame;

import java.util.Scanner;

public class Menu {
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
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
			/*try{
				raft.API.setState(board);
				raft.API.init(new InstApplier(), gameName);
			}catch(Exception e){
				e.printStackTrace();
			}*/
		
		// Join a game
		}else{
			System.out.println("Enter exiting game name");
			String gameName = input.nextLine();
			System.out.println("Connecting..");
			
			//uncomment when trying cluster functionality
			/*
			try{
				raft.API.init(new InstApplier(), gameName);
			}catch(Exception e){
				e.printStackTrace();
			}
			*/			
		}
	}	
}
