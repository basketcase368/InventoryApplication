package inventory;

// Super class for Book, CD, and DVD.

public abstract class MediaItem 
{
	// Fields
	private String title;
	private String creator;
	private String genre;
	private int mediaID;
	
	// constructor
	
	public MediaItem( int id, String title, String creator, String genre )
	{
		setMediaID(id);
		setTitle( title );
		setCreator( creator );
		setGenre( genre );
	}
	
	// methods
	public int getMediaID()
	{
		return this.mediaID;
	}
	
	public void setMediaID(int id)
	{
		this.mediaID = id;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle( String title )
	{
		this.title = title;
	}
	
	public String getCreator()
	{
		return this.creator;
	}
	
	public void setCreator( String creator )
	{
		this.creator = creator;
	}
	
	public String getGenre()
	{
		return this.genre;
	}
	
	public void setGenre( String genre )
	{
		this.genre = genre;
	}
	
	public abstract String getDuration();
	public abstract void setDuration( String duration );
	public abstract String getType();
}
