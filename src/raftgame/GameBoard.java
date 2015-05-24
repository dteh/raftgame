package raftgame;

import java.io.Serializable;
import java.util.HashMap;

public class GameBoard implements Serializable{
	public HashMap<String,Integer> scores;
	public int boardSize;
	public int[][] board;
	public GameBoard(int size){
		boardSize = size;
		board = new int[boardSize][boardSize];
	}
	public void setScore(HashMap<String,Integer> scoreVar){
		scores = Game.scores;
	}
}
