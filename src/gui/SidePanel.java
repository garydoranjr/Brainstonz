package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import main.EventHandler;
import main.EventHandler.BrainstonzPlayer;

@SuppressWarnings("serial")
public class SidePanel extends JPanel {
	
	public PlayerPanel player1, player2;
	public OptionsPanel options;
	public InstructionsPanel instructions;

	public SidePanel(){
		super(new GridBagLayout());
		this.setOpaque(false);
		this.setBorder(new LineBorder(Color.BLACK,3));
		player1 = new PlayerPanel(1);
		player2 = new PlayerPanel(2);
		options = new OptionsPanel();
		instructions = new InstructionsPanel();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(player1,gbc);
		
		gbc.gridy = 1;
		this.add(player2,gbc);
		
		gbc.gridy = 2;
		this.add(options,gbc);
		
		gbc.gridy = 3;
		gbc.weighty = 1;
		this.add(instructions,gbc);
		
		
		EventHandler.getInstance().registerSidePanel(this);
	}
	
	public void setNewGameEnabled(boolean enabled){
		options.newGame.setEnabled(enabled);
	}
	
	public void setNewGameButtonText(String text){
		options.newGame.setText(text);
	}
	
	public void setTurnLabelText(String text, int player){
		instructions.turnLabel.setText(text);
		setInstructionContext(player);
	}
	
	public void setMoveLabelText(String text){
		instructions.moveLabel.setText(text);
	}
	
	public void setInstructionContext(int context){
		instructions.setContext(context);
	}
	
	public void enableControls(boolean enable){
		player1.enableControls(enable);
		player2.enableControls(enable);
	}
	
	public int getDelay(){
		return 2000 - options.slider.getValue();
	}
	
	public static class PlayerPanel extends JPanel {
		
		private final JSlider slider;
		private final JComboBox typeSelect;
		private final JLabel typeLabel, sliderLabel;
		
		public PlayerPanel(int player){
			super(new GridBagLayout());
			this.setOpaque(false);
			TitledBorder border = new TitledBorder((player == 1 ? "Black" : "White"));
			border.setBorder(new LineBorder(Color.BLACK,2));
			border.setTitleFont(border.getTitleFont().deriveFont(24f));
			this.setBorder(border);
			String[] types = new String[]{"Human","Computer"};
			typeSelect = new JComboBox(types);
			if(player == 2)
				typeSelect.setSelectedIndex(1);
			typeLabel = new JLabel("Type:");
			typeLabel.setFont(typeLabel.getFont().deriveFont(20f));
			typeLabel.setLabelFor(typeSelect);
			slider = new JSlider(SwingConstants.HORIZONTAL,0,10,5);/*{
				@Override
				public void paint(Graphics g){
					super.paint(g);
					Insets insets = this.getInsets();
					int width = this.getWidth() - insets.left - insets.right - 1,
						height = this.getHeight() - insets.top - insets.bottom - 1;
					if(isEnabled())
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.GRAY);
					g.drawRect(insets.left, insets.top, width, height);
				}
			};*/
			slider.setMajorTickSpacing(1);
			slider.setPaintTrack(true);
			slider.setOpaque(false);
			Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
			JLabel easy = new JLabel("Easy"),
				   hard = new JLabel("Hard");
			easy.setFont(easy.getFont().deriveFont(16f));
			hard.setFont(hard.getFont().deriveFont(16f));
			labelTable.put( new Integer( 1 ), easy );
			labelTable.put( new Integer( 9 ), hard );
			slider.setLabelTable( labelTable );
			slider.setPaintLabels(true);
			sliderLabel = new JLabel("Skill:");
			sliderLabel.setLabelFor(slider);
			sliderLabel.setFont(typeLabel.getFont().deriveFont(20f));
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(3,3,3,3);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridheight = 1;
			gbc.anchor = GridBagConstraints.EAST;
			this.add(typeLabel,gbc);
			
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			this.add(typeSelect,gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.EAST;
			this.add(sliderLabel,gbc);
			
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			this.add(slider,gbc);
			
		}
		
		public BrainstonzPlayer getType(){
			if(typeSelect.getSelectedIndex() == 0)
				return BrainstonzPlayer.HUMAN;
			else
				return BrainstonzPlayer.COMPUTER;
		}
		
		public double getSkill(){
			return ((double)slider.getValue()/10.0);
		}
		
		public void enableControls(final boolean enable){
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					slider.setEnabled(enable && typeSelect.getSelectedIndex() == 1);
					sliderLabel.setEnabled(enable && typeSelect.getSelectedIndex() == 1);
					typeSelect.setEnabled(enable);
					typeLabel.setEnabled(enable);
				}
				
			});
			
		}
		
	}
	
	private static class OptionsPanel extends JPanel {

		private JButton newGame, help, about;
		private JSlider slider;
		private HelpFrame helpframe = null;
		private AboutFrame aboutframe = null;
		
		public OptionsPanel(){
			super(new GridBagLayout());
			this.setOpaque(false);
			TitledBorder border = new TitledBorder("Options");
			border.setBorder(new LineBorder(Color.BLACK,2));
			border.setTitleFont(border.getTitleFont().deriveFont(24f));
			this.setBorder(border);
			
			newGame = new JButton("Start Game");
			newGame.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					EventHandler.getInstance().newGameButton();
				}
			});
			newGame.setEnabled(true);
			
			help = new JButton("Help");
			help.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(helpframe == null){
						helpframe = new HelpFrame();
					}else if(!helpframe.isVisible()){
						helpframe.dispose();
						helpframe = new HelpFrame();
					}else{
						helpframe.toFront();
					}
				}
			});
			help.setEnabled(true);
			
			about = new JButton("About");
			about.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(aboutframe == null){
						aboutframe = new AboutFrame();
					}else if(!aboutframe.isVisible()){
						aboutframe.dispose();
						aboutframe = new AboutFrame();
					}else{
						aboutframe.toFront();
					}
				}				
			});
			about.setEnabled(true);
			
			JLabel animationLabel = new JLabel("Animation Speed:");
			animationLabel.setFont(animationLabel.getFont().deriveFont(20f));
			
			slider = new JSlider(SwingConstants.HORIZONTAL,0,2000,1000);/*{
				@Override
				public void paint(Graphics g){
					super.paint(g);
					Insets insets = this.getInsets();
					int width = this.getWidth() - insets.left - insets.right - 1,
						height = this.getHeight() - insets.top - insets.bottom - 1;
					if(isEnabled())
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.GRAY);
					g.drawRect(insets.left, insets.top, width, height);
				}
			};*/
			slider.setMajorTickSpacing(1);
			slider.setPaintTrack(true);
			slider.setOpaque(false);
			Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
			JLabel fast = new JLabel("Fast"),
				   slow = new JLabel("Slow");
			fast.setFont(fast.getFont().deriveFont(16f));
			slow.setFont(slow.getFont().deriveFont(16f));
			labelTable.put( new Integer( 200 ), slow );
			labelTable.put( new Integer( 1800 ), fast );
			slider.setLabelTable( labelTable );
			slider.setPaintLabels(true);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(3,3,3,3);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.NONE;
			this.add(newGame,gbc);
			
			gbc.gridx = 1;
			this.add(help,gbc);
			
			gbc.gridx = 2;
			this.add(about,gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 3;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.BOTH;
			this.add(animationLabel,gbc);
			
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.CENTER;
			this.add(slider,gbc);
			
		}
		
		public void dispose(){
			if(helpframe != null)
				helpframe.dispose();
			if(aboutframe != null)
				aboutframe.dispose();
		}
		
	}
	
	private static class InstructionsPanel extends JPanel {

		public JLabel turnLabel, moveLabel;
		private int context;
		private static final String defaultMoveLabel = "Press \"Start Game\"",
									defaultTurnLabel = "Select Options";
		
		public InstructionsPanel(){
			super(new GridBagLayout());
			this.setOpaque(false);
			TitledBorder border = new TitledBorder("Instructions");
			border.setBorder(new LineBorder(Color.BLACK,2));
			border.setTitleFont(border.getTitleFont().deriveFont(24f));
			this.setBorder(border);
			turnLabel = new JLabel(defaultTurnLabel);
			turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
			moveLabel = new JLabel(defaultMoveLabel);
			moveLabel.setHorizontalAlignment(SwingConstants.CENTER);
			setContext(-1);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(3,3,3,3);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.anchor = GridBagConstraints.SOUTH;
			gbc.fill = GridBagConstraints.NONE;
			this.add(turnLabel,gbc);
			
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.NORTH;
			this.add(moveLabel,gbc);
			
		}
		
		public void setContext(int context){
			this.context = context;
			switch(context){
			case 0:
			case 1:
			case 2:
				moveLabel.setFont(moveLabel.getFont().deriveFont(20f));
				break;
			default:
				moveLabel.setText(defaultMoveLabel);
				turnLabel.setText(defaultTurnLabel);
				moveLabel.setFont(moveLabel.getFont().deriveFont(24f));
				turnLabel.setFont(turnLabel.getFont().deriveFont(24f));
				break;
			}
		}
		
		@Override
		public void paint(Graphics g){
			super.paint(g);
			switch(context){
			
			}
		}
		
	}

	public void dispose() {
		if(options != null)
			options.dispose();
	}
	
}
