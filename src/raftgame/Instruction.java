package raftgame;


public class Instruction {
	int x;
	int y;
	int newState;
	int oldx;
	int oldy;
	
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
	 */
	public Instruction(int nx, int ny, int state, int ox, int oy){
		x = nx;
		y = ny;
		newState = state;
		oldx = ox;
		oldy = oy;
	}
}
