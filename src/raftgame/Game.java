package raftgame;

import java.util.Random;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game extends Canvas implements KeyListener{
	static Random rgen;
	static GameBoard map;
	
	private BufferStrategy strategy;
	private Image pSpr;
	
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
		map = g;
		rgen = new Random();
		
		Frame frame = new Frame("fuk yeh");
		frame.setLayout(null);
		setBounds(0,0,60 * map.boardSize, 60 * map.boardSize);
		frame.add(this);
		frame.setSize(60 * map.boardSize, 60 * map.boardSize);
		
		frame.addKeyListener(this);
		addKeyListener(this);
		frame.setVisible(true);
		
		pSpr = ImageIO.read(new File("player.png"));
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
	}
	
	public void start() throws InterruptedException{
		int c = 0;
		spawnPlayer(0);
		while(true){
			Graphics2D drawing = (Graphics2D) strategy.getDrawGraphics();
			drawing.setColor(Color.gray);
			drawing.fillRect(0, 0, 60 * map.boardSize, 60 * map.boardSize);
			logic();
			
			Thread.sleep(20);
			
			render(drawing);
			drawing.dispose();
			strategy.show();
			//debugging - uncomment if you want to see map + movement;
			/*System.out.println(c);
			c++;
			printMap();*/
		}
	}
	
	private void render(Graphics2D drawing){
		 for(int i = 0; i < map.boardSize; i++){
			 for(int j = 0; j < map.boardSize; j++){
				 if(GameBoard.board[i][j] == 1){
					 int xpos = 60 * j;
					 int ypos = 60 * i;
					 drawing.drawImage(pSpr,xpos,ypos,null);
				 }
				 if(GameBoard.board[i][j] == 2){
					 
				 }
			 }
		 }
	}
	
	private void printMap(){
		for(int i = 0; i < map.boardSize; i++){
			for(int j = 0; j < map.boardSize; j++){
				System.out.print(GameBoard.board[i][j]);
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
			
			System.out.printf("Old: %d,%d - New: %d,%d",oldX,oldY,playerX,playerY);
			
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
			raft.API.pushInstruction(new Instruction(playerY,playerX,PLAYER, oldY, oldX));
		}
	}
	
	private void spawnPlayer(int attempt){
		//prevent too many attempts to spawn on a full/almost board
		if(attempt > 5){
			System.out.println("No free spaces, try again later");
		}else{
			int randx = rgen.nextInt(map.boardSize);
			int randy = rgen.nextInt(map.boardSize);
			System.out.println(map.boardSize);
			System.out.println("x: " + randx + ", y: " + randy );
			System.out.println(map);
			
			
			if(GameBoard.board[randx][randy]==0){
				playerX = randx;
				playerY = randy;
				raft.API.pushInstruction(new Instruction(randy,randx,PLAYER,-1,-1));
			}else{
				spawnPlayer(attempt + 1);
			}
		}
	}

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
