/**
 * 
 */
package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 * @author Administrator
 *
 */
public class SquareLayout implements LayoutManager {

	@Override
	public void addLayoutComponent(String arg0, Component arg1) {}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	@Override
	public void layoutContainer(Container parent) {
		// TODO Auto-generated method stub
		Insets insets = parent.getInsets();
        int width = parent.getWidth()
                       - (insets.left + insets.right);
        int height = parent.getHeight()
                        - (insets.top + insets.bottom);
        
        // Forget about all components but the first
        for(int i = 1; i < parent.getComponentCount(); i++)
        	parent.getComponent(i).setVisible(false);
        
        Component c = parent.getComponent(0);
        c.setVisible(true);
        
        if(width > height)
        	c.setBounds(new Rectangle(insets.left+(width-height)/2,insets.top,height,height));
        else
        	c.setBounds(new Rectangle(insets.left,insets.top+(height-width)/2,width,width));
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension retVal = parent.getComponent(0).getMinimumSize();
		Insets insets = parent.getInsets();
        retVal.width += insets.left + insets.right;
        retVal.height += insets.top + insets.bottom;
		return retVal;
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension retVal = parent.getComponent(0).getPreferredSize();
		Insets insets = parent.getInsets();
        retVal.width += insets.left + insets.right;
        retVal.height += insets.top + insets.bottom;
		return retVal;
	}

	@Override
	public void removeLayoutComponent(Component arg0) {}

}
