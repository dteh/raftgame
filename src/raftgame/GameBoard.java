package raftgame;

public class GameBoard {
	public final int boardSize;
	public static Integer[][] board;
	public GameBoard(int size){
		boardSize = size;
		board = new Integer[boardSize][boardSize];
	}
}
