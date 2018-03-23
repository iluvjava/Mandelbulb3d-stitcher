import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * <p>
 * This is a class that will store a buffered image in the field given its 
 * dimension.
 * </p>
 * <br>
 * <title>
 * Features
 * </title>
 * <ol>
 * <li> Add a image, left corner located at a given position, given the image and the point; to the 
 * canvas
 * </li> 
 * <li>Immutable class.<li/>
 * </ol>
 * @author Dashie my little cutie
 *
 */
public class IMGManipulator 
{
	public final BufferedImage G_SubjectImage;
	public static final String SFG_WritePostfix = ".png";
	
	protected File G_IMGFile; 
	
	/**
	 * Creating Black background. 
	 * @param x
	 * width
	 * @param y
	 * hight
	 */
	public IMGManipulator(int x, int y)
	{
		this.G_SubjectImage = new BufferedImage(x,y,BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Creating an instance with a file of an image. 
	 * @param x
	 * @param y
	 * @throws IOException 
	 */
	public IMGManipulator(File arg) throws IOException
	{
		if(arg == null)throw new AssertionError();
		this.G_SubjectImage = ImageIO.read(arg);
		this.G_IMGFile = arg; 
	}
	
	
	/**
	 * ---Under testing---<br>
	 * Put the input para img into this instance of image. 
	 */
	public void setTopLeftAtThis(int x, int y, BufferedImage img)
	{
		int w = img.getWidth(), h = img.getHeight();
		int[] Var_imagedatastream = new int[h*w];
		Var_imagedatastream =img.getRGB(0,0, w, h,Var_imagedatastream ,0, w);
		
		this.G_SubjectImage.setRGB(x,y ,w, h, Var_imagedatastream, 0, w);
	}
	
	

	
	/**
	 * @param dir
	 * @param filename
	 * @return
	 */
	public boolean storeTheImage(String dir, String filename)
	{
		File f = new File(dir);
		filename = filename+this.SFG_WritePostfix;
		//Testing the validity of the file. 
		{
			if(!f.exists()||!f.isDirectory())return false;
		}
		try 
		{
			ImageIO.write(this.G_SubjectImage, "png", new File(dir+"/"+filename));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	public File getFile()
	{
		return this.G_IMGFile;
	}
	
	
}
