import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestEverything {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException 
	{
		//testingIMGCanvas(new File("TestingImage.png"));
		//testingRegex();
		testingMBDColetion();
	}

	
	/**
	 * This is a successful example of how to manipulate images, 
	 * an new image that double the size is created and the 
	 * original image is pasted on the top left corner. 
	 * @param in
	 * @throws IOException
	 */
	private void testCloningImage(File in) throws IOException 
	{
		BufferedImage img = ImageIO.read(in);
		
		BufferedImage imgclone = new BufferedImage(img.getWidth()*2,img.getHeight()*2,BufferedImage.TYPE_INT_RGB);
		
		{
			int h = img.getHeight(), w = img.getWidth();
		
			int[] imgdatastream = img.getRGB(0,0,w,h, new int[h*w], 0,w);
			
			imgclone.setRGB(0, 0, w, h,imgdatastream , 0, w);
		}
		
		System.out.println(ImageIO.write(imgclone, "png", new File("Outputimage")));
		
	}
	
	
	// Throughly tested. 
	public void testingIMGCanvas(File in) throws IOException
	{
//		BufferedImage img = ImageIO.read(in);
//		IMGManipulator subject = new IMGManipulator(img.getWidth()*2,img.getHeight()*2);
//		subject.alignTopLeftCornerAt(img.getWidth(), img.getHeight(), img);
//		
//		println(in.getAbsoluteFile().getParentFile().isDirectory());
//		System.out.println
//				(
//				subject.storeTheImage
//						(
//						in.getAbsoluteFile().getParentFile().getAbsolutePath(),
//						"this is the lovely image"+subject.SFG_WritePostfix
//						)
//				)
//		;
		
	}
	
	
	//Thorougly tested. 
	public static void testingRegex()
	{
//		println("Metallic menger Big renderX02Y01.png".matches(".*X\\d+Y\\d+.*"));
//		println("asl;fk jX0098Y0987.png".matches(".*X\\d+Y\\d+.*"));
//		
//		String s = "Metallic menger Big renderX02Y01.png";
//		System.out.println(MBD3dCollection.extractCoords_XandY(s,'X'));
//		System.out.println(s);
		
	}
	
	
	public static void println(Object o)
	{
		System.out.println(o);
	}

	public static void testingMBDColetion() throws IOException
	{
		File f = new File
				(
				"C:/Users/victo/Desktop/Candy Manger fractal revisit/"
				+ "Metallic menger final/Metallic menger Big render"
				);
		
		Object[] whatever = MBD3dCollection.scanDirectory
				(
						f
				);
		System.out.println(Arrays.toString(whatever));
		
		MBD3dCollection something = new MBD3dCollection(f);
		
		System.out.println(something);
		for(Object o : something.G_ImagesGroups)
		{
			System.out.println(Arrays.toString(something.preparedata((Set<Groupable>) o)));
		}
		
		something.stichTiles();
	}
	
	
}
