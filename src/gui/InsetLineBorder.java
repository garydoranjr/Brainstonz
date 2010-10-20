/**
 * 
 */
package gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class InsetLineBorder extends AbstractBorder {
	
	private EmptyBorder empty = null;
	private LineBorder line = null;

	public InsetLineBorder(EmptyBorder empty, LineBorder line){
		this.empty = empty;
		this.line = line;
	}
	
	@Override
	public void paintBorder(Component c, 
			Graphics g, int x, int y, int width, 
			int height) {
		empty.paintBorder(c, g, x, y, width, height);
		line.paintBorder(c, g, x, y, width, height);
	}
	
	@Override
	public Insets getBorderInsets(Component c){
		return empty.getBorderInsets(c);
	}
	
	@Override
	public Insets getBorderInsets(Component c,
            Insets insets){
		return empty.getBorderInsets(c,insets);
	}
	
	@Override
	public boolean isBorderOpaque(){
		return false;
	}
	
	@Override
	public Rectangle getInteriorRectangle(Component c,
            int x,
            int y,
            int width,
            int height){
		return empty.getInteriorRectangle(c, x, y, width, height);
	}
}
