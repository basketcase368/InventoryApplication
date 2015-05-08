package inventory;

public class Book extends MediaItem
{
	// fields
	private String numPages;
	private final String type = "Book";
	// constructor 
	public Book(int id, String title, String author, String genre, String numPages)
		{
			super(id, title, author, genre);
			setDuration( numPages );
		}
	
	@Override
	public String getDuration()
	{
		return this.numPages;
	}
	
	@Override
	public void setDuration( String numPages )
	{
		this.numPages = numPages;
	}
	
	@Override
	public String toString()
	{
		return String.format("Title: %s\nAuthor: %s\nGenre: %s\nNo. Pages: %s\nMedia Type: %s\n",
				super.getTitle(),super.getCreator(),super.getGenre(),getDuration(),getType());
	}
	
	@Override
	public String getType()
	{
		return this.type;
	}
}
