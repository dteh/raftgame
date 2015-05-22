package raftgame;

import java.io.Serializable;

public class GameBoard implements Serializable{
	public int boardSize;
	public static int[][] board;
	public GameBoard(int size){
		boardSize = size;
		board = new int[boardSize][boardSize];
	}
}
