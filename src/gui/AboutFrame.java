/**
 * 
 */
package gui;

import image.ImageLoader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AboutFrame extends JFrame {

	public AboutFrame(){
		super();
		add(getInfoPanel());
		setAlwaysOnTop(true);
		setIconImage(ImageLoader.icon);
		setTitle("About Brainstonz\u2122");
		pack();
		squareAndCenter();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void squareAndCenter(){
		int max = (int) Math.max(getWidth(), getHeight());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-max)/2,(screenSize.height-max)/2, max, max);
	}
	
	private int clickCount = 0;
	public int getClicks(){
		return ++clickCount;
	}
	
	private JPanel getInfoPanel(){
		JPanel retVal = new JPanel(){
			@Override
			public void paint(Graphics g){
				g.setColor(new Color(205,133,63));
				g.fillRect(0, 0, getWidth(), getHeight());
				BufferedImage glyph = ImageLoader.glyphs[0];
				g.drawImage(glyph, 0, 0, this.getWidth(), this.getHeight(),
							0, 0, glyph.getWidth(), glyph.getHeight(), this);
				g.setColor(new Color(205,133,63,200));
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paint(g);
			}
		};
		retVal.setLayout(new BoxLayout(retVal,BoxLayout.Y_AXIS));
		retVal.setBorder(new EmptyBorder(10,10,10,10));
		retVal.setOpaque(false);
		JLabel title = new JLabel("Brainstonz\u2122"),
			   subtitle = new JLabel("By McWiz"),
		   	   version = new JLabel("Version 1.0");
		final JLabel date = new JLabel("Created 07/04/2009");
		setFontSize(title,30);
		setFontSize(subtitle,24);
		setFontSize(version,20);
		setFontSize(date,18);
		JButton okButton = new JButton("OK");
		okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutFrame.this.dispose();
			}
		});
		date.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(AboutFrame.this.getClicks() >= 7){
					date.setText("Created By Gary Doran");
					AboutFrame.this.pack();
					AboutFrame.this.squareAndCenter();
				}
			}
		});
		retVal.add(title);
		retVal.add(subtitle);
		retVal.add(Box.createVerticalStrut(10));
		retVal.add(version);
		retVal.add(date);
		retVal.add(Box.createVerticalStrut(10));
		retVal.add(okButton);
		
		return retVal;
	}
	
	private void setFontSize(JLabel label, int fontSize){
		label.setFont(label.getFont().deriveFont((float)fontSize));
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	}
	
}
