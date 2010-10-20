package gui;

import image.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private SidePanel sidepanel = null;
	
	public GamePanel(){
		super();
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		this.setLayout(layout);
		this.add(new GameBoard(),BorderLayout.CENTER);
		this.add(sidepanel = new SidePanel(),BorderLayout.EAST);
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setOpaque(false);
	}
	
	@Override
	public void paint(Graphics g){
		int bgW = ImageLoader.background.getWidth(),
			bgH = ImageLoader.background.getHeight(),
		    columns = (int)Math.ceil((double)this.getWidth()/(double)bgW),
		    rows = (int)Math.ceil((double)this.getHeight()/(double)bgH);
		for(int row = 0; row < rows; row++){
			for(int col = 0; col < columns; col++){
				g.drawImage(ImageLoader.background,col*bgW,row*bgH,this);
			}
		}
		super.paint(g);
	}

	public void dispose() {
		if(sidepanel != null)
			sidepanel.dispose();
	}
	
}
