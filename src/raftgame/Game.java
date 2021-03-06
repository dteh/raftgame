package raftgame;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game extends Canvas implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2140477079429652397L;
	static Random rgen;
	static GameBoard map;
	
	static HashMap<String,Integer> scores;
	static String scoreString;
	static String winner = "";
	
	private BufferStrategy strategy;
	private Image pSpr;
	private Image bgSpr;
	private Image coinSpr;
	
	//coin spawning
	private int coinTimer = 0;
	private int coinTimerLimit = 100;
	
	//tile values
	static final int EMPTY = 0;
	static final int PLAYER = 1;
	static final int COIN = 2;
	
	//whether keys are pressed
	private boolean DOWN = false;
	private boolean UP = false;
	private boolean LEFT = false;
	private boolean RIGHT = false;
	
	//current player xy coordinates
	private static int playerX;
	private static int playerY;
	
	public Game(GameBoard g) throws IOException{
		scores = new HashMap<String,Integer>();
		map = g;
		rgen = new Random();
		scores.put(raft.RaftNode.myAddress, 0);

		//game will crash here if no host for game is found
		Game.map.setScore(scores);

		
		Frame frame = new Frame("RAFTGAME 1.0");
		frame.setLayout(null);
		setBounds(0,0,60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize, 60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize + 50);
		frame.add(this);
		frame.setSize(60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize, 60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize + 50);
		
		frame.addKeyListener(this);
		addKeyListener(this);
		frame.setVisible(true);
		
		pSpr = ImageIO.read(new File("player.png"));
		bgSpr = ImageIO.read(new File("bg.png"));
		coinSpr = ImageIO.read(new File("coin.png"));

		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
	}
	
	public void start() throws InterruptedException{
		//int c = 0;	
		spawn(0, PLAYER);
		while(true){
			Graphics2D drawing = (Graphics2D) strategy.getDrawGraphics();
			drawing.setColor(Color.white);
			drawing.fillRect(0, 0, 60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize, 60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize + 50);
			
			logic();
			coinTimer++;
			if(coinTimer >= coinTimerLimit){
				spawn(0,COIN);
				coinTimer = 0;
			}
			scoreString = "";
			for(Entry<String, Integer> entry:((GameBoard)raft.RaftNode.getStateObject()).scores.entrySet()){
				scoreString = scoreString + String.format("%s: %d, ",entry.getKey(),entry.getValue()); 
			}
			scores = ((GameBoard)raft.RaftNode.getStateObject()).scores;
			
			Thread.sleep(20);
			
			render(drawing);
		    drawing.setFont(new Font("TimesRoman", Font.PLAIN, 10));
		    drawing.setColor(Color.red);
			drawing.drawString(scoreString, 10, 60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize + 10);
		    drawing.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			drawing.drawString(winner, 60*((GameBoard)raft.RaftNode.getStateObject()).boardSize/4, 60 * ((GameBoard)raft.RaftNode.getStateObject()).boardSize/2);
			drawing.dispose();
			strategy.show();
			//debugging - uncomment if you want to see map + movement;
			/*System.out.println(c);
			c++;
			printMap();*/
		}
	}
	
	private void render(Graphics2D drawing){
		 for(int i = 0; i < ((GameBoard)raft.RaftNode.getStateObject()).boardSize; i++){
			 for(int j = 0; j < ((GameBoard)raft.RaftNode.getStateObject()).boardSize; j++){
				 int xpos = 60 * j;
				 int ypos = 60 * i;
				 drawing.drawImage(bgSpr,xpos,ypos,null);
				 if((((GameBoard)raft.RaftNode.getStateObject()).board[i][j] == 1) || ((GameBoard)raft.RaftNode.getStateObject()).board[i][j] == 3){
					 drawing.drawImage(pSpr,xpos,ypos,null);
				 }
				 if(((GameBoard)raft.RaftNode.getStateObject()).board[i][j] == 2){
					 drawing.drawImage(coinSpr,xpos,ypos,null);
				 }
			 }
		 }
	}
	
	public static void printMap(GameBoard b){
		for(int i = 0; i < b.boardSize; i++){
			for(int j = 0; j < b.boardSize; j++){
				System.out.print(b.board[i][j]);
			}
			System.out.println();
		}
	}
	
	private void logic(){
		// check if player needs to be moved
		int x = 0;
		int y = 0;
		if(UP){
			y -= 1;
			UP = false;
		}
		if(DOWN){
			y += 1;
			DOWN = false;
		}
		if(LEFT){
			x -= 1;
			LEFT = false;
		}
		if(RIGHT){
			x += 1;
			RIGHT = false;
		}
		if((x != 0) || (y != 0)){
			//save old positions
			int oldX = playerX;
			int oldY = playerY;
			
			// update new position
			playerX += x;
			playerY += y;
			
			System.out.printf("Old: %d,%d - New: %d,%d \n",oldX,oldY,playerX,playerY);
			
			// if player goes over boundaries, move to other side
			if(playerX < 0){
				playerX = map.boardSize + playerX;
			}
			if(playerX >= map.boardSize){
				playerX = playerX - map.boardSize;
			}
			if(playerY < 0){
				playerY = map.boardSize + playerY;
			}
			if(playerY >= map.boardSize){
				playerY = playerY - map.boardSize;
			}
			//send or process instruction (note, coords are reversed)
			raft.API.pushInstruction(new Instruction(playerY,playerX,PLAYER, oldY, oldX, raft.RaftNode.myAddress));
		}
	}
	
	private void spawn(int attempt, int type){
		//prevent too many attempts to spawn on a full/almost board
		if(attempt > 5){
			System.out.println("No free spaces, try again later");
		}else{
			int randx = rgen.nextInt(map.boardSize);
			int randy = rgen.nextInt(map.boardSize);
			
			if(map.board[randx][randy]==0){
				if(type == PLAYER){
					playerX = randx;
					playerY = randy;
				}
				raft.API.pushInstruction(new Instruction(randy,randx,type,-1,-1, raft.RaftNode.myAddress));
			}else{
				spawn(attempt + 1, type);
			}
		}
	}
	
	
	
	
	/**
	 * Keybindings section
	 */
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			DOWN = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP){
			UP = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			LEFT = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			RIGHT = true;
			printMap(map);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			DOWN = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP){
			UP = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			LEFT = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			RIGHT = false;
		}
	}
	
}
