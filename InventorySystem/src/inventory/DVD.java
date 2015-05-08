package inventory;

public class DVD extends MediaItem
{
	// fields
	private String length;
	private final String type = "DVD";
	
	// constructor 
	public DVD(int id, String title, String producer, String genre, String length)
		{
			super(id, title, producer, genre);
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
		return String.format("Title: %s\nProducer: %s\nGenre: %s\nDuration: %s\nMedia Type: %s\n",
				super.getTitle(),super.getCreator(),super.getGenre(),getDuration(),getType());
	}
	
	@Override
	public String getType()
	{
		return this.type;
	}
}
