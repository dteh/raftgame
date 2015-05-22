package raftgame;

public class InstApplier implements raft.InstructionApplier {
	
	static final int EMPTY = 0;
	static final int PLAYER = 1;
	static final int COIN = 2;
	
	@Override
	public Object ApplyInstruction(Object inst) {
		Instruction Inst = (Instruction)inst;
		
		if(GameBoard.board[Inst.x][Inst.y] == EMPTY){
			GameBoard.board[Inst.x][Inst.y] = Inst.newState;
		}else if(GameBoard.board[Inst.x][Inst.y] == COIN){
			//TODO: increase player score
			GameBoard.board[Inst.x][Inst.y] = Inst.newState;
			if(Game.scores.get(Inst.user) != null){
				Game.scores.put(Inst.user,Game.scores.get(Inst.user)+1);
			}else{
				Game.scores.put(Inst.user,0);
			}
			System.out.println(Game.scores.get(Inst.user));
		}
		
		// if the moving player has an old position, set to empty
		if(!(Inst.oldx == -1)){
			GameBoard.board[Inst.oldx][Inst.oldy] = EMPTY;
		}
		return null;
	}
	
}
