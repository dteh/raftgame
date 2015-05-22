package raftgame;


public class Instruction {
	int x;
	int y;
	int newState;
	int oldx;
	int oldy;
	String user;
	
	/**
	 * Instruction specifying new position of object.
	 * 
	 * @param nx - new x coord
	 * @param ny - new y coord
	 * @param state - type of tile to place
	 * 
	 * These ones are only used if a player is being moved
	 * @param ox - old x coord (can be -1 for none)
	 * @param oy - old y coord (can be -1 for none)
	 * @param user TODO
	 */
	public Instruction(int nx, int ny, int state, int ox, int oy, String usr){
		x = nx;
		y = ny;
		newState = state;
		oldx = ox;
		oldy = oy;
		user = usr;
	}
}
