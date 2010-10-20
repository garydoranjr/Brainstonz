/**
 * 
 */
package image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import main.BrainstonzState;

/**
 * Loads icons and images used by
 * the Brainstonz GUI.
 *
 */
public class ImageLoader {
	
	private static final String backgroundName = "wood.jpg";
	public static BufferedImage background = null;
	
	private static final String[] glyphNames = {"a","b","c","d","e","f","g","b","g","h","f","e","c","d","a","h"};
	private static final String glyphExt = ".png";
	public static BufferedImage[] glyphs = new BufferedImage[16];

	private static final String blackStoneName = "bs",
								whiteStoneName = "ws",
								stoneExt = ".png";
	public static final int numStoneImgs = 5;
	public static final int numStonz = 4*numStoneImgs;
	public static BufferedImage[] blackStonz = new BufferedImage[numStonz],
								  whiteStonz = new BufferedImage[numStonz];
	
	public static final String iconName = "icon16.png";
	public static BufferedImage icon = null;
	
	public static final String questionIconName = "question.png";
	public static ImageIcon questionIcon = null;
	
	/**
	 * Loads the images for the Brainstonz
	 * GUI.  Should be called before instantiating
	 * any GUI components.
	 * @throws IOException if loading fails
	 */
	public static void load() throws IOException {
		
		// Load Icon
		icon = ImageIO.read(ImageLoader.class.getResourceAsStream(iconName));
		
		// Load Background
		background = ImageIO.read(ImageLoader.class.getResourceAsStream(backgroundName));
		
		// Load Glyphs
		for(int i = 0; i < 16; i++){
			if(glyphs[BrainstonzState.pair[i]] != null){
				glyphs[i] = glyphs[BrainstonzState.pair[i]];
			}else{
				glyphs[i] = ImageIO.read(ImageLoader.class.getResourceAsStream(glyphNames[i]+glyphExt));
			}
		}
		
		// Load Stonz
		for(int i = 0; i < numStoneImgs; i++){
			blackStonz[i] = ImageIO.read(ImageLoader.class.getResourceAsStream(blackStoneName+i+stoneExt));
			whiteStonz[i] = ImageIO.read(ImageLoader.class.getResourceAsStream(whiteStoneName+i+stoneExt));
			for(int j = 1; j < 4; j++){
				blackStonz[i+j*numStoneImgs] = rotate(blackStonz[i], 90*j);
				whiteStonz[i+j*numStoneImgs] = rotate(whiteStonz[i], 90*j);
			}
		}
		
		// Load Question Icon
		questionIcon = new ImageIcon(ImageIO.read(ImageLoader.class.getResourceAsStream(questionIconName)));
		
	}
	
	/**
	 * Rotates a {@code BufferedImage} by a
	 * specified angle.  Used to add variety
	 * to the stonz.
	 * @param img image to rotate
	 * @param angle angle of rotation
	 * @return rotated {@code BufferedImage}
	 */
	public static BufferedImage rotate(BufferedImage img, int angle) {  
		int w = img.getWidth();  
		int h = img.getHeight();  
		BufferedImage dimg = new BufferedImage(w, h, img.getType()==0?BufferedImage.TYPE_INT_ARGB:img.getType());  
		Graphics2D g = dimg.createGraphics();  
		g.rotate(Math.toRadians(angle), w/2, h/2);  
		g.drawImage(img, null, 0, 0);  
		return dimg;  
	}  
	
}
