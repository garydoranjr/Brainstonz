package gui;

import image.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.EventHandler;

@SuppressWarnings("serial")
public class GameSpace extends JPanel{
	
	private BufferedImage[] stonz = new BufferedImage[3];
	
	private static final int SIZE = 150;
	private int position;
	private boolean highlighted = false;
	
	/**
	 * TODO
	 */
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public GameSpace(int position){
		super(new BorderLayout());
		this.position = position;
//		JLabel label = new JLabel("<html><h1>"+BrainstonzState.symbols[position]+"</h1></html>");
//		label.setVerticalAlignment(JLabel.CENTER);
//		label.setHorizontalAlignment(JLabel.CENTER);
//		this.setBackground(Color.YELLOW);
		this.setOpaque(false);
//		this.add(label,BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(SIZE,SIZE));
		this.setBorder(new LineBorder(Color.BLACK, 3));
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				EventHandler.getInstance().mouseEnter(GameSpace.this, e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EventHandler.getInstance().mouseExit(GameSpace.this, e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				EventHandler.getInstance().mouseClick(GameSpace.this, e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
		});
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				EventHandler.getInstance().mouseEnter(GameSpace.this, e);
			}
			
		});
	}
	
	@Override
	public void paint(Graphics g){
		paintHighlight(g);
		super.paint(g);
		paintGlyph(g);
		paintStone(g);
	}
	
	private void paintHighlight(Graphics g){
		if(highlighted){
			g.setColor(new Color(205,133,63,90));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	}
	
	private void paintGlyph(Graphics g) {
		BufferedImage glyph = ImageLoader.glyphs[position];
		g.drawImage(glyph, 0, 0, this.getWidth(), this.getHeight(), 0, 0, glyph.getWidth(), glyph.getHeight(), this);
	}

	private void paintStone(Graphics g){
		int player = EventHandler.getInstance().playerAt(position);
		if(player > 0){
			BufferedImage stone = stonz[player];
			g.drawImage(stone, 0, 0, this.getWidth(), this.getHeight(), 0, 0, stone.getWidth(), stone.getHeight(), this);
		}
	}
	
	public void setStonz(BufferedImage bStone, BufferedImage wStone){
		this.stonz[1] = bStone;
		this.stonz[2] = wStone;
	}
	
	public int getPosition(){
		return position;
	}
	
}
