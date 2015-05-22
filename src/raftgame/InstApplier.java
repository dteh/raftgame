package raftgame;

public class InstApplier implements raft.InstructionApplier {
	/**
	 * TILE TYPES:
	 * 0 = EMPTY
	 * 1 = PLAYER
	 * 2 = COIN
	 */
	@Override
	public Object ApplyInstruction(Object inst) {
		Instruction Inst = (Instruction)inst;
		
		if(GameBoard.board[Inst.x][Inst.y] == 0){
			GameBoard.board[Inst.x][Inst.y] = Inst.newState;
		}else if(GameBoard.board[Inst.x][Inst.y] == 2){
			//TODO: increase player score
			GameBoard.board[Inst.x][Inst.y] = Inst.newState;
		}
		
		// if the moving player has an old position, set to empty
		if(!(Inst.oldx == -1)){
			GameBoard.board[Inst.oldx][Inst.oldy] = 0;
		}
		return null;
	}
	
}
