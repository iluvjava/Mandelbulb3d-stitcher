import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ListSelectionEvent;

import LoggingSystem.Log;

/**
 * <p>
 * <b>
 * This is a class that contains a collection of mandelbulb images. 
 * <b>
 * </p>
 * <ol>
 * <li>It should be able to extract all the images given the directory and construct the object</li>
 * <li>it will be able to tell if each image belong to a collection. </li>
 * <li>It will group all the images that are in one collection.s</li>
 * <ol>
 * @author I love Dashie
 *
 */
public class MBD3dCollection{
	
	public File[] G_CollectionOfFiles;
	
	// all the coordinates and its corresponding images, the index is matched. 
	private MB3DImage[] G_CollectioOfMB3DImg; 
	
	private final File G_RootDir;
	
	LinkedList<Object> G_ImagesGroups = new LinkedList<>();

	private Set<IMGManipulator> G_StichedImage= new HashSet<>();
	
	
	/**
	 * The given parameter is a directiory that contains all the m3d files. 
	 * @param dir
	 * @throws IOException
	 */
	public MBD3dCollection(File dir) throws IOException
	{
		if(dir == null)throw new NullPointerException();
		this.G_CollectionOfFiles=scanDirectory(dir);
		if(this.G_CollectionOfFiles.length==0)throw new IOException();
		
		this.G_RootDir = dir;
		
		this.G_CollectioOfMB3DImg = new MB3DImage[this.G_CollectionOfFiles.length];
		
		int i = 0;
		for(File f : this.G_CollectionOfFiles)
		{
			this.G_CollectioOfMB3DImg[i++] = new MB3DImage(f);
		}
		this.GroupAll();
	}
	
	
	/**
	 * ---Casually Tested---<br>
	 * Group all the images in the MB3D images collection. 
	 * <br>
	 * <ol>
	 * <li>Some of the images are not in one collection (part of a larger image)
	 * , thus we need t try to group all those images. 
	 * <li> The result should be in that class field. 
	 * <ol\>
	 */
	private void GroupAll()
	{
		List<Groupable> lst = new LinkedList<>();
		
		// copy the list of images first. 
		for(Groupable g : this.G_CollectioOfMB3DImg)
		{
			lst.add(g);
		}
		
		while(!lst.isEmpty())
		{
			Set<Groupable> Acollection = new HashSet<>();
			
			Groupable nextelement      = lst.remove(0);
			
			Acollection.add(nextelement);
			
			Iterator<Groupable> temp   = (Iterator<Groupable>) lst.iterator();
			
			while(temp.hasNext())
			{
				Groupable nextnextelement = temp.next();
				
				if (nextelement.isInOneCollection(nextnextelement))
				{
					Acollection.add(nextnextelement);
					temp.remove();
				}
				
			}
			this.G_ImagesGroups.add(Acollection);
		}
	}
	
	
	/**
	 * ---Casually Tested---
	 * <br>
	 * <ol>
	 * <li>Testing the coordinate of the bottom right corner
	 * <li>Testing whether images are homogeneous (not entirely necessary)
	 * <ol\>
	 * @return
	 * [width, height, tile_width, tile_hight] 
	 */
	int[] preparedata(Set<Groupable> argIn)
	{
		int x = 0;
		int y = 0; 
		int[] tiledimension = null;
		for (Groupable i : argIn)
		{
			MB3DImage ii = (MB3DImage)i;
			int h = ii.G_SubjectImage.getHeight();
			int w = ii.G_SubjectImage.getWidth();
			if(ii.getCoords()[0]*w>x) x = ii.getCoords()[0]*w;
			if(ii.getCoords()[1]*h>y) y = ii.getCoords()[1]*h;
			
			if(tiledimension == null)
			{
				tiledimension = new int[2];
				tiledimension[0] = w;
				tiledimension[1] = h; 
			}
		}
			
		int[] result = new int[4];
		result[0]=x;result[1] = y;
		result[2] = tiledimension[0]; result[3] = tiledimension[1];
		return result;
	}
	
	
	/**
	 * ---Under Testing ---<br>
	 * For each of the set in the groups collection, the program will
	 * Stitch all the images in there. 
	 */
	public void stichTiles()
	{
		for (Object g : this.G_ImagesGroups)
		{
			Log.println("StichTiles: ");
			Log.println(g);
			this.stichTiles_part(g);
		}
	}
	
	private void stichTiles_part(Object argIn)
	{
		Set<Groupable> images = (Set<Groupable>)argIn;
		int[] dimension = this.preparedata(images);
		
		IMGManipulator canvas = new IMGManipulator(dimension[0],dimension[1]);
		
		for(Groupable g : images)
		{
			MB3DImage temp = (MB3DImage) g;
			int[] tileCoords = temp.getCoords();
			tileCoords[0]--; tileCoords[1]--;
			
			Log.println(temp);
			
			canvas.setTopLeftAtThis
			(
				dimension[2]*tileCoords[0], dimension[3]*tileCoords[1], temp.G_SubjectImage
			);
			
			Log.println(dimension[2]*tileCoords[0]+" "+dimension[3]*tileCoords[1]);
			
			
			
		}
		
		canvas.storeTheImage
		(
				this.G_RootDir.toString(), 
				Integer.toHexString((int) (this.hashCode()+Math.random()*1000))
		);
		
	}
	
	
	
	
	/**
	 * <br>
	 * This function will try to extract all the mandelbulbs images, the names of the 
	 * file should match the expression: ".*X\\d+Y\\d+.*"
	 * @param dir
	 *  The file object should be a directory.<br>
	 * @return
	 * null if directory is not valid or it doesn't exist. 
	 * <br>A list of all the files that can be qualified as m3d images.
	 * @throws IOException 
	 */
	public static File[] scanDirectory(File dir) throws IOException
	{
		if(dir == null){System.out.println("scanDirectory: null input.");return null;};
		
		List<File> result  = new ArrayList<File>();
		File[] temp = dir.listFiles();
		
		if(temp == null)throw new IOException();
		
		for(File f : temp)
		{
			Log.println(f.getName());
			
			if(f.getName().matches(".*X\\d+Y\\d+.*"))result.add(f);
		}
		return result.toArray(new File[result.size()]);
	}
	
	
	
	public String toString()
	{
		String s = "---------Class instance: "+ this.getClass()+"\n";
		s += "Collection of files: "+Arrays.toString(this.G_CollectionOfFiles)+"\n";
		s+= "Collection of MB3D images:\n "+ Arrays.toString(this.G_CollectioOfMB3DImg)+'\n';
		if(!this.G_ImagesGroups.isEmpty())
		{
			s+="This is images classified into different groups:\n"+ this.G_ImagesGroups.toString()+"\n";
		}
		s+= "Root dirctory: "+ this.G_RootDir.toString()+"\n";
		return s;
	}

}
