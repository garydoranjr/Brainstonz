package gui;

import javax.swing.SwingUtilities;


public class Test {

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				HelpFrame test = new HelpFrame();
				test.dispose();
				test.dispose();
			}
		});
	}
	
}
