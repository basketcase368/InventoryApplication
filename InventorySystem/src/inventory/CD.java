package inventory;

public class CD extends MediaItem
{
	// fields
	private String length;
	private final String type = "CD";
	// constructor 
	public CD(int id, String title, String artist, String genre, String length)
	{
		super(id, title, artist, genre);
		setDuration( length );
	}
		
	@Override
	public String getDuration()
	{
		return this.length;
	}
	
	@Override
	public void setDuration( String length )
	{
		this.length = length;
	}
	
	@Override
	public String toString()
	{
		return String.format("Title: %s\nArtist: %s\nGenre: %s\nNumber of Tracks: %s\nMedia Type: %s\n",
				super.getTitle(),super.getCreator(),super.getGenre(),getDuration(),getType());
	}
	
	@Override
	public String getType()
	{
		return this.type;
	}
}
