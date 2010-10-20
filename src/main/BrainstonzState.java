package main;

import java.util.LinkedList;

public class BrainstonzState {
	
	public static final String[] symbols = {"A","B","C","D","E","F","G","B","G","H","F","E","C","D","A","H"};

	// Powers of Three
	public static final int[] p3 =
	{1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683,
		59049, 177147, 531441, 1594323, 4782969, 14348907};
	public static int get(int state, int position){ return (state/p3[position])%3; }
	public static int set(int state, int position, int value){
		return state + p3[position]*(value - ((state/p3[position])%3));
	}
	// Location of other symbol in pair
	public static final int[] pair = {14,7,12,13,11,10,8,1,6,15,5,4,2,3,0,9};
	public static int getPair(int state, int position){ return get(state, pair[position]); }
	// Array holds opponent of index value
	public static final int[] opponent = {0,2,1};

	// Generate Successors of a State, given a player
	public static LinkedList<Successor> successors(int state, int player){
		// Temporary Variable
		Successor temp;
		// List holding returned successors
		LinkedList<Successor> succs = new LinkedList<Successor>();

		// If the first state, player only makes one move.
		if( state==0 ){
			// Put piece at each possible location
			for(int i=0; i<16; i++){
				temp = new Successor(set(state,i,player));
				temp.moves[0] = i;
				succs.add(temp);
			}
		}
		// Otherwise, up to four sub-moves
		else{
			// Lists for holding intermediate results
			LinkedList<Successor> succs1 = new LinkedList<Successor>();
			LinkedList<Successor> succs2 = new LinkedList<Successor>();
			LinkedList<Successor> succs3 = new LinkedList<Successor>();
			Successor temp2;
			Boolean foundOne;

			// First Sub-Move
			for(int i=0; i<16; i++){
				if(get(state,i) == 0){
					temp = new Successor(set(state,i,player));
					temp.moves[0] = i;
					// If the state is terminal, the player's turn is over
					if(terminal(temp.state)!=0){ succs.add(temp); }
					else{
						// If the move makes a pair, remove a piece (succs1)
						if(get(state,pair[i]) == player){ succs1.add(temp); }
						// Otherwise, make another move (succs2)
						else                            { succs2.add(temp); }
					}
				}
			}

			// Remove an opponent's piece (when applicable)
			while(succs1.size() > 0){
				// Get a sub-move state for which opponent removal is applicable
				temp2 = succs1.removeFirst();
				for(int i=0; i<16; i++){
					if(get(temp2.state,i) == opponent[player]){
						temp =  new Successor(set(temp2.state,i,0));
						temp.moves[0] = temp2.moves[0];
						temp.moves[1] = i;
						// Make second Move
						succs2.add(temp);
					}
				}
			}

			// Make second sub-move
			while(succs2.size() > 0){
				// Get a sub-move state for the second move
				temp2 = succs2.removeFirst();
				// For keeping track of whether there
				// is a spot found to make the second move
				foundOne = false;
				for(int i=0; i<16; i++){
					if(get(temp2.state,i) == 0){
						temp = new Successor(set(temp2.state,i,player));
						temp.moves[0] = temp2.moves[0];
						temp.moves[1] = temp2.moves[1];
						temp.moves[2] = i;
						// If terminal, it is a win, so add it immediately to successors
						if(terminal(temp.state)!=0){ succs.add(temp); }
						else{
							// If the move makes a pair, remove an opponent's piece (succs3)
							if(get(temp2.state,pair[i])==player){ succs3.add(temp); }
							// Otherwise, this turn is over, so add it to successors list
							else                                { succs.add(temp);  }
						}
						foundOne = true;
					}
				}
				// If no spot to move, this is a tie, so add it to successors as is
				if(!foundOne){ succs.add(temp2); }
			}

			// Make Final sub-move, if applicable
			while(succs3.size() > 0){
				// Get a sub-move state for which the last move is applicable
				temp2 = succs3.removeFirst();
				for(int i=0; i<16; i++){
					if(get(temp2.state,i) == opponent[player]){
						temp = new Successor(set(temp2.state,i,0));
						temp.moves[0] = temp2.moves[0];
						temp.moves[1] = temp2.moves[1];
						temp.moves[2] = temp2.moves[2];
						temp.moves[3] = i;
						// After removing the opponents piece,
						// this turn is over, so add it to successors list
						succs.add(temp);
					}
				}
			}

		}
		return succs;
	}

	public static boolean hasEmptySpace(int state){
		for(int i = 0; i < 16; i++){
			if(get(state,i)==0)
				return true;
		}
		return false;
	}

	// Terminal Test
	public static int terminal(int state){
		int temp;
		for(int i=0; i<4; i++){

			// Check a Column
			temp = get(state, i + 0);
			if( temp==get(state, i + 4) && temp==get(state, i + 8)
					&& temp==get(state, i + 12) && temp!=0)
				return temp;

			// Check a Row
			temp = get(state, 4*i + 0);
			if( temp==get(state, 4*i + 1) && temp==get(state, 4*i + 2)
					&& temp==get(state, 4*i + 3) && temp!=0)
				return temp;

		}
		// Check a Diagonal
		temp = get(state, 0);
		if( temp==get(state, 5) && temp==get(state, 10)
				&& temp==get(state, 15) && temp!=0)
			return temp;
		// Check other Diagonal
		temp = get(state, 3);
		if( temp==get(state, 6) && temp==get(state, 9)
				&& temp==get(state, 12) && temp!=0)
			return temp;

		// No Winners: Return 0
		return 0;
	}
	
	public static int[] winningSet(int state){
		int temp;
		for(int i=0; i<4; i++){

			// Check a Column
			temp = get(state, i + 0);
			if( temp==get(state, i + 4) && temp==get(state, i + 8)
					&& temp==get(state, i + 12) && temp!=0)
				return new int[]{i+0,i+4,i+8,i+12};

			// Check a Row
			temp = get(state, 4*i + 0);
			if( temp==get(state, 4*i + 1) && temp==get(state, 4*i + 2)
					&& temp==get(state, 4*i + 3) && temp!=0)
				return new int[]{4*i+0,4*i+1,4*i+2,4*i+3};

		}
		// Check a Diagonal
		temp = get(state, 0);
		if( temp==get(state, 5) && temp==get(state, 10)
				&& temp==get(state, 15) && temp!=0)
			return new int[]{0,5,10,15};
		// Check other Diagonal
		temp = get(state, 3);
		if( temp==get(state, 6) && temp==get(state, 9)
				&& temp==get(state, 12) && temp!=0)
			return new int[]{3,6,9,12};

		// No Winners: Return 0
		return null;
	}

	public static class Successor{

		public int state = 0;
		public int[] moves = new int[]{-1, -1, -1, -1};

		public Successor(int state){
			this.state = state;
		}

	}

}
