/**
 * 
 */
package gui;

import image.ImageLoader;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class HelpFrame extends JFrame {

	public HelpFrame(){
		super();
		JTextPane text = new JTextPane();
		try {
			text.setPage(HelpFrame.class.getResource("help.html"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not load help.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		JScrollPane scroll = new JScrollPane(text);
		text.setOpaque(false);
		scroll.setOpaque(false);
		add(scroll);
		setAlwaysOnTop(true);
		setIconImage(ImageLoader.icon);
		setTitle("Help");
		setSize(new Dimension(500,600));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
}
