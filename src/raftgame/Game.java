package raftgame;

import java.util.Random;
import java.awt.*;

public class Game {
	static Random rgen;
	static GameBoard map;
	static final int EMPTY = 0;
	static final int PLAYER = 1;
	static final int COIN = 2;
	
	public static void start(GameBoard g){
		map = g;
		rgen = new Random();
		spawnPlayer(0);
	}
	
	private static void spawnPlayer(int attempt){
		//prevent too many attempts to spawn on a full/almost board
		if(attempt > 5){
			System.out.println("No free spaces, try again later");
		}else{
			int x = rgen.nextInt(map.boardSize);
			int y = rgen.nextInt(map.boardSize);
			if(map.board[x][y]==0){
				raft.API.pushInstruction(new Instruction(x,y,PLAYER,-1,-1));
			}else{
				spawnPlayer(attempt + 1);
			}
		}
	}
	
}
