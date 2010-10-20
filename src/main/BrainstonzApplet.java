/**
 * 
 */
package main;

import gui.GamePanel;
import image.ImageLoader;

import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ai.BrainstonzAI;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class BrainstonzApplet extends JApplet {

	@Override
	public void init(){
		try {
			BrainstonzAI.load();
			ImageLoader.load();
			UIManager.setLookAndFeel("net.beeger.squareness.SquarenessLookAndFeel");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error loading Brainstonz. Sorry.",
					"Error Loading",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				BrainstonzApplet.this.setLayout(new BorderLayout());
				BrainstonzApplet.this.add(new GamePanel(),BorderLayout.CENTER);
			}
			
		});
	}
	
	@Override
	public void start(){
		repaint();
	}
	
}
