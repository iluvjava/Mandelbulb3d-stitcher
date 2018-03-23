import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** 
 * Targeted at a particular MB3D file, buffered or fraction of an image. \
 * <br>
 * 
 * 
 */
public class MB3DImage extends IMGManipulator implements Groupable
{
	
	private final int[] G_Coords = new int[2];
	
	public MB3DImage(File arg) throws IOException 
	{

		super(arg);
		if(!arg.getName().matches(".*X\\d+Y\\d+.*"))throw new AssertionError();
		this.G_Coords[0] = extractCoords_XandY(this.getFile().getName(), 'X');
		this.G_Coords[1] = extractCoords_XandY(this.getFile().getName(), 'Y');
	}
	
	
	
	@Override
	/**
	 * Overrided from the groupable interface. 
	 * @return 
	 * true if this instance and the arg is is supposed to be in one image. 
	 * 
	 * 
	 */
	public boolean isInOneCollection(Groupable arg) 
	{
		if (! (arg instanceof MB3DImage))return false; 
		
		MB3DImage temp =(MB3DImage)arg;
		
		String name = this.getFile().getName();
		{
		int i = name.lastIndexOf('X'); 
		name = name.substring(0, i);
		}
		
		String name2 = temp.getFile().getName();
		{
		int i = name2.lastIndexOf('X'); 
		name2 = name2.substring(0, i);
		}
		return name.equals(name2);
	}

	
	/**
	 * Tested ! 
	 * @param arg
	 * The name of the file
	 * @param XorY
	 * 'x', 'y' are legal input 
	 * @return
	 * The coords of the image that it should be. 
	 */
	private static int extractCoords_XandY(String arg, char XorY)
	{
		{
			int temp = arg.lastIndexOf(".");
			int temp2 = arg.lastIndexOf(XorY);
			arg = arg.substring(0,temp);
			arg = arg.substring(temp2);
		}
		Pattern p = Pattern.compile(XorY+"\\d+");
		Matcher m = p.matcher(arg);
		
		m.find();
		arg = arg.substring(1,m.end());
		return Integer.parseInt(arg);
	}
	
	public String toString()
	{
		return this.getFile().getName()+"\n"+Arrays.toString(this.G_Coords);
	}
	
	
	/**
	 * Getter method, return the grid coordinate of this tile. 
	 * @return
	 * [x,y]-> format of the coord. 
	 */
	public int[] getCoords()
	{
		return this.G_Coords;
	}
}
