package net.itarray.sikuli.api.visual;

import com.sun.awt.AWTUtilities;
import edu.umd.cs.piccolo.PCanvas;

import javax.swing.*;

class ScreenOverlayWindow extends JWindow implements ScreenDisplayable {

	private final PCanvas canvas;
	public ScreenOverlayWindow() {
		canvas = new PCanvas();
		canvas.setBackground(null);
		canvas.setOpaque(false); 
		add(canvas);
//		setBackground(null);
		getContentPane().setBackground(null); // this line is needed to make the window transparent on Windows
		AWTUtilities.setWindowOpaque(this, false);
		setAlwaysOnTop(true);						
	}

	public PCanvas getCanvas(){
		return canvas; 
	}
	
	@Override
	public void displayOnScreen() {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
		    	setVisible(true);
		    }
		});
	}

	@Override
	public void hideFromScreen() {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
				setVisible(false);
				dispose();
		    }
		});
	}		

}