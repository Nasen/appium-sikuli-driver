package net.itarray.sikuli.api;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * StaticImageScreen class implements a Screen that behaves as if it *always* displays a
 * given image. All calls to getScreenshot returns the given image. It is useful
 * for debugging the image search capabilities of the API.
 */
public class StaticImageScreen implements Screen {
	
	static private BufferedImage crop(BufferedImage src, int x, int y, int width, int height){
	    BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	    Graphics g = dest.getGraphics();
	    g.drawImage(src, 0, 0, width, height, x, y, x + width, y + height, null);
	    g.dispose();
	    return dest;
	}

	
	final private BufferedImage image;
	public StaticImageScreen(BufferedImage image){
		this.image = image;
	}
	
	@Override
	public BufferedImage getScreenshot(int x, int y, int width, int height) {
		BufferedImage regionImage = crop(image, x,y,width,height);
		return regionImage;
	}

	@Override
	public Dimension getSize(){
		return new Dimension(image.getWidth(),image.getHeight());
	}
}