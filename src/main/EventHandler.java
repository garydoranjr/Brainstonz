package main;

import image.ImageLoader;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import main.BrainstonzState.Successor;

import ai.BrainstonzAI;

import gui.GameBoard;
import gui.GameSpace;
import gui.SidePanel;

public class EventHandler {

	public static String[] moveLabels = {"Place First Stone",
										 "Remove A Stone",
										 "Place Second Stone",
										 "Remove A Stone",
										 "First Move",
										 "Game Over"};
	public static String[] turnLabels = {"BLACK'S TURN","WHITE'S TURN","IT'S A TIE!","BLACK WINS!","WHITE WINS!"};
	
	public enum BrainstonzPlayer { HUMAN, COMPUTER }
	public static BrainstonzPlayer player1, player2;
	
	private static int state;
	private static GameState gamestate;
	
	public enum GameState {
		PREGAME, FIRSTMOVE,
		P1M1, P1M2, P1R1, P1R2,
		P2M1, P2M2, P2R1, P2R2,
		P1WIN, P2WIN, TIE
	}
	private boolean computerMoving;
	private Thread computerThread;
	private double[] aiSkillz;
	
	private GameBoard board = null;
	private SidePanel sidePanel = null;
	
	private static EventHandler me = null;
	public static EventHandler getInstance(){
		if(me == null)
			return (me = new EventHandler());
		return me;
	}
	public EventHandler(){
		player1 = BrainstonzPlayer.HUMAN;
		player2 = BrainstonzPlayer.COMPUTER;
		state = 0;
		computerMoving = false;
		gamestate = GameState.PREGAME;
		aiSkillz = new double[]{0.0, 0.0, 0.0};
	}
	
	public void registerBoard(GameBoard board){
		this.board = board;
	}
	public void registerSidePanel(SidePanel sidePanel) {
		this.sidePanel = sidePanel;
	}
	
	public void mouseClick(GameSpace space, MouseEvent e){
		if(isValidSpace(space) && !computerMoving){
			int position = space.getPosition();
			int temp;
			switch(gamestate){
			case PREGAME:
			case TIE:
			case P1WIN:
			case P2WIN:
				return;
			case FIRSTMOVE:
				state = BrainstonzState.set(state, position, 1);
				newTurn(2);
				break;
			case P1M1:
				state = BrainstonzState.set(state, position, 1);
				// Check for a win
				if((temp = BrainstonzState.terminal(state))!=0){
					endGame(temp);
					break;
				}
				if(BrainstonzState.getPair(state, position) == 1){
					sidePanel.setMoveLabelText(moveLabels[1]);
					gamestate = GameState.P1R1;
				}else{
					if(!BrainstonzState.hasEmptySpace(state)){
						endGame(0);
						break;
					}
					sidePanel.setMoveLabelText(moveLabels[2]);
					gamestate = GameState.P1M2;
				}
				break;
			case P1M2:
				state = BrainstonzState.set(state, position, 1);
				// Check for a win
				if((temp = BrainstonzState.terminal(state))!=0){
					endGame(temp);
					break;
				}
				if(BrainstonzState.getPair(state, position) == 1){
					sidePanel.setMoveLabelText(moveLabels[3]);
					gamestate = GameState.P1R2;
				}else
					newTurn(2);
				break;
			case P2M1:
				state = BrainstonzState.set(state, position, 2);
				// Check for a win
				if((temp = BrainstonzState.terminal(state))!=0){
					endGame(temp);
					break;
				}
				if(BrainstonzState.getPair(state, position) == 2){
					sidePanel.setMoveLabelText(moveLabels[1]);
					gamestate = GameState.P2R1;
				}else{
					if(!BrainstonzState.hasEmptySpace(state)){
						endGame(0);
						break;
					}
					sidePanel.setMoveLabelText(moveLabels[2]);
					gamestate = GameState.P2M2;
				}
				break;
			case P2M2:
				state = BrainstonzState.set(state, position, 2);
				// Check for a win
				if((temp = BrainstonzState.terminal(state))!=0){
					endGame(temp);
					break;
				}
				if(BrainstonzState.getPair(state, position) == 2){
					sidePanel.setMoveLabelText(moveLabels[3]);
					gamestate = GameState.P2R2;
				}else
					newTurn(1);
				break;
			case P1R1:
				state = BrainstonzState.set(state, position, 0);
				sidePanel.setMoveLabelText(moveLabels[2]);
				gamestate = GameState.P1M2;
				break;
			case P1R2:
				state = BrainstonzState.set(state, position, 0);
				newTurn(2);
				break;
			case P2R1:
				state = BrainstonzState.set(state, position, 0);
				sidePanel.setMoveLabelText(moveLabels[2]);
				gamestate = GameState.P2M2;
				break;
			case P2R2:
				state = BrainstonzState.set(state, position, 0);
				newTurn(1);
				break;
			}
			space.setHighlighted(false);
			space.repaint();
		}
	}
	
	public void mouseEnter(GameSpace space, MouseEvent e){
		if(isValidSpace(space)){
			space.setHighlighted(true);
			space.repaint();
		}
	}
	
	public void mouseExit(GameSpace space, MouseEvent e){
		switch(gamestate){
		case P1WIN:
		case P2WIN:
			return;
		}
		space.setHighlighted(false);
		space.repaint();
	}
	
	public boolean isValidSpace(GameSpace space){
		if(computerMoving)
			return false;
		switch(gamestate){
		case PREGAME:
		case TIE:
		case P1WIN:
		case P2WIN:
			return false;
		case FIRSTMOVE:
		case P1M1:
		case P1M2:
		case P2M1:
		case P2M2:
			return BrainstonzState.get(state, space.getPosition()) == 0;
		case P1R1:
		case P1R2:
			return BrainstonzState.get(state, space.getPosition()) == 2;
		case P2R1:
		case P2R2:
			return BrainstonzState.get(state, space.getPosition()) == 1;
		}
		return false;
	}
	
	public void newGameButton(){
		switch(gamestate){
		case PREGAME:
			newGame();
			return;
		case P1WIN:
		case P2WIN:
		case TIE:
			break;
		default:
			Object[] options = {"Yes", "No"};
			if(JOptionPane.showOptionDialog(null,
					"Quit the current game?",
					"New Game",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					ImageLoader.questionIcon,
					options,
					options[1]) != JOptionPane.YES_OPTION) {
				return;
			}
		}
		preGame();
	}
	
	private void preGame(){
		computerMoving = false;
		state = 0;
		gamestate = GameState.PREGAME;
		board.reset();
		sidePanel.enableControls(true);
		sidePanel.setNewGameButtonText("Start Game");
		sidePanel.setInstructionContext(-1);
	}
	
	private void newGame(){
		computerMoving = false;
		state = 0;
		sidePanel.setTurnLabelText(turnLabels[0],1);
		sidePanel.setMoveLabelText(moveLabels[4]);
		gamestate = GameState.FIRSTMOVE;
		sidePanel.enableControls(false);
		sidePanel.setNewGameButtonText("New Game");
		board.reset();
		aiSkillz[1] = sidePanel.player1.getSkill();
		aiSkillz[2] = sidePanel.player2.getSkill();
		player1 = sidePanel.player1.getType();
		player2 = sidePanel.player2.getType();
		if(player1 == BrainstonzPlayer.COMPUTER){
			computerTurn(1);
		}
	}
	
	private void endGame(int winner){
		sidePanel.setTurnLabelText(turnLabels[winner+2],winner);
		sidePanel.setMoveLabelText(moveLabels[5]);
		switch(winner){
		case 0:
			gamestate = GameState.TIE;
			break;
		case 1:
			highlightWinningSet();
			gamestate = GameState.P1WIN;
			break;
		case 2:
			highlightWinningSet();
			gamestate = GameState.P2WIN;
			break;
		}
		board.repaint();
	}
	
	private void highlightWinningSet(){
		final int[] set = BrainstonzState.winningSet(state);
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				if(set != null){
					for(int i = 0; i < 4; i++)
						board.spaces[set[i]].setHighlighted(true);
				}
				board.repaint();
			}
		});
		
	}
	
	private void newTurn(int player){
		sidePanel.setNewGameEnabled(true);
		int temp;
		// Check for a win
		if((temp = BrainstonzState.terminal(state))!=0){
			endGame(temp);
			return;
		}
		// Check for a tie
		if(BrainstonzState.successors(state,player).size() == 0){
			endGame(0);
			return;
		}
		computerMoving = false;
		sidePanel.setTurnLabelText(turnLabels[player-1],player);
		sidePanel.setMoveLabelText(moveLabels[0]);
		if(player == 1){
			gamestate = GameState.P1M1;
			if(player1 == BrainstonzPlayer.COMPUTER){
				computerTurn(player);
			}
		}else{
			gamestate = GameState.P2M1;
			if(player2 == BrainstonzPlayer.COMPUTER){
				computerTurn(player);
			}
		}
	}
		
	private int getDelay(){
		return sidePanel.getDelay();
	}
	
	private void computerTurn(final int player){
		computerMoving = true;
		sidePanel.setNewGameEnabled(false);
		final Successor succ = BrainstonzAI.move(state, player, aiSkillz[player]);
		computerThread = new Thread(new Runnable(){

			@Override
			public void run() {
				if(succ==null){ return; }
				int setVal;
				GameSpace space;
				try{Thread.sleep(getDelay());}catch(InterruptedException e){}
				for(int i = 0; i < 4; i++){
					if(succ.moves[i] != -1){
						setVal = ((i+1)%2)*player; //Even --> player, Odd --> 0 (remove)
						space = board.spaces[succ.moves[i]];
						state = BrainstonzState.set(state,succ.moves[i],setVal);
						space.setHighlighted(true);
						space.repaint();
						for(int j = i+1; j < 4; j++){
							if(succ.moves[j] != -1){
								sidePanel.setMoveLabelText(moveLabels[j]);
								break;
							}
						}
						try{Thread.sleep(getDelay());}catch(InterruptedException e){}
						space.setHighlighted(false);
						space.repaint();
					}
				}
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						newTurn(BrainstonzState.opponent[player]);
					}
					
				});
			}
			
		});
		computerThread.start();
	}
	
	public int playerAt(int position){
		return BrainstonzState.get(state, position);
	}
	
}
