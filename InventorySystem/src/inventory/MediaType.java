package inventory;

public enum MediaType 
{
	BOOK("Book"),
	CD("CD"),
	DVD("DVD"),
	ALL("ALL");
	
	// fields
	String type;
	
	// constructor
	private MediaType(String type)
	{
		this.type = type;
	}
	
	//to string
	@Override
	public String toString()
	{
		return String.format("%s",this.type);
	}
	
	public String returnType()
	{
		return this.type;
	}
}
