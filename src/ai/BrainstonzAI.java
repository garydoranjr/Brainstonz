/**
 * 
 */
package ai;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import main.BrainstonzState;
import main.BrainstonzState.Successor;

/**
 * @author Administrator
 *
 */
public class BrainstonzAI {

	private static byte[] tree = null;

	public static void load() throws IOException{
		
		InputStream is = BrainstonzAI.class.getResourceAsStream("TREE.DAT");

		// Get the size of the file
		long length = is.available();
		
		if (length <= 0)
			throw new IOException("Zero length file!");

		if (length > Integer.MAX_VALUE)
			throw new IOException("File too large.");

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int)length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length)
			throw new IOException("Could not completely read file.");

		tree = bytes;
		is.close();
	}

	public static Successor move(int state, int player, double skill){
		int temp;
		if(Math.random() < skill && state != 0){
			// Use Tree
			Successor tempsucc = null;
			// Initialize Most extreme value observed (min/max)
			// to Negative/Positive "Infinity"
			int extreme = player==1?-3:3;
			// Loop through successors
			for(Successor s : BrainstonzState.successors(state,player)){
				// Maximize or Minimize?
				if(player==1){
					// Get Value of Successor
					temp = BrainstonzState.get((int)tree[s.state],3);
					temp = temp==2?-1:temp;
					// If Greater than Max, update extreme value
					// and point tempsucc to successor
					if(temp > extreme){
						extreme = temp;
						tempsucc = s;
					}
				}else{
					// Get Value of Successor
					temp = BrainstonzState.get((int)tree[s.state],1);
					temp = temp==2?-1:temp;
					// If Less than Min, update extreme value
					// and point tempsucc to successor
					if(temp < extreme){
						extreme = temp;
						tempsucc = s;
					}
				}
			}
			// Show moves and return successor state
			return tempsucc;
		}else{
			// Pick Random Successor
			List<Successor> succs = BrainstonzState.successors(state,player);
			int rand = (int)(succs.size()*Math.random());
			return succs.get(rand);
		}
	}

	/**
	 * TODO This should start a new timer thread or something to make the computer's moves happen.
	 * Probably should pass the timer to the "EventParser" to handle.
	 * @param state
	 * @param succ
	 * @return
	 */
//	public int showmove(int state, Successor succ){
//		if(succ==null){ return state; }
//		if(succ.moves[0]!=-1){
//			System.out.printf("Player %d (Computer) Places First Stone:\n",player);
//			Brainstonz.printstate(state=Brainstonz.set(state,succ.moves[0],player));
//			try{Thread.sleep(1000);}catch(InterruptedException e){}
//		}
//		if(succ.moves[1]!=-1){
//			System.out.printf("Player %d (Computer) Removes a Stone:\n",player);
//			Brainstonz.printstate(state=Brainstonz.set(state,succ.moves[1],0));
//			try{Thread.sleep(1000);}catch(InterruptedException e){}
//		}
//		if(succ.moves[2]!=-1){
//			System.out.printf("Player %d (Computer) Places Second Stone:\n",player);
//			Brainstonz.printstate(state=Brainstonz.set(state,succ.moves[2],player));
//			try{Thread.sleep(1000);}catch(InterruptedException e){}
//		}
//		if(succ.moves[3]!=-1){
//			System.out.printf("Player %d (Computer) Removes a Stone:\n",player);
//			Brainstonz.printstate(state=Brainstonz.set(state,succ.moves[3],0));
//			try{Thread.sleep(1000);}catch(InterruptedException e){}
//		}
//		return succ.state;
//	}

}
