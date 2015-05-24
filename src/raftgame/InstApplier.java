package raftgame;

public class InstApplier implements raft.InstructionApplier {
	
	static final int EMPTY = 0;
	static final int PLAYER = 1;
	static final int COIN = 2;
	static final int NPLAYERS = 3;
	
	@Override
	public Object ApplyInstruction(Object inst) {
		Instruction Inst = (Instruction)inst;
		
		//if spot is avail, place instructed item in spot
		if(Game.map.board[Inst.x][Inst.y] == EMPTY){
			Game.map.board[Inst.x][Inst.y] = Inst.newState;
		//if spot is filled with a coin, move player on to it
		}else if(Game.map.board[Inst.x][Inst.y] == COIN){
			Game.map.board[Inst.x][Inst.y] = Inst.newState;
			//add player to score db if non existant, or increment
			if(Game.scores.get(Inst.user) != null){
				if(Game.scores.get(Inst.user) >= 20){
					Game.winner = (Inst.user + " is the winner!");
				}else{
					Game.scores.put(Inst.user,Game.scores.get(Inst.user)+1);
				}
			}else{
				Game.scores.put(Inst.user,1);
			}
			Game.map.setScore(Game.scores);
			System.out.println(Game.scores.get(Inst.user));
		}else if ((Game.map.board[Inst.x][Inst.y] == PLAYER) && (Inst.newState == PLAYER) ){
			Game.map.board[Inst.x][Inst.y] = NPLAYERS;
		}
		
		// if the moving player has an old position, set to empty
		if(!(Inst.oldx == -1)){
			if(Game.map.board[Inst.oldx][Inst.oldy] == NPLAYERS){
				Game.map.board[Inst.oldx][Inst.oldy] = PLAYER;
			}else {
				Game.map.board[Inst.oldx][Inst.oldy] = EMPTY;
			}
		}
		raft.RaftNode.setStateObject(Game.map);
		return null;
	}
	
}
