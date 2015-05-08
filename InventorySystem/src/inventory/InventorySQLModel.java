/**
 * This class will provide the basic structure for the model that will be used with the InventoryView.
 * This class will establish a connection to a MySQL database and update, retrieve and edit data in
 * the database via specific prepared statements.
 * 
 */
package inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class InventorySQLModel
{
	//------------------------------------- FIELDS ------------------------------------- 
	private final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/inventory";
	private final String USER = "root";
	private final String PASSWORD = "TH3R3ISN0C0WL3V3L"; 
	private Connection connection;
	private PreparedStatement selectAllFromBook, selectAllFromCD, selectAllFromDVD, 
		insertMediaItemBook, insertMediaItemCD, insertMediaItemDVD, 
		removeMediaItemBook, removeMediaItemCD, removeMediaItemDVD,
		updateMediaItemBook, updateMediaItemCD, updateMediaItemDVD;
	private ArrayList<MediaItem> bookInventory, cdInventory, dvdInventory, allInventory;
	private InventoryView view;
	//------------------------------------- CONSTRUCTOR ------------------------------------- 
	public InventorySQLModel(InventoryView view)
	{
		try
		{
			//establish connection 
			connection = DriverManager.getConnection(DATABASE_URL,USER,PASSWORD);
			
			//create prepared statements for getting all entries from certain table (note that table objects cannot be parameterized)
			selectAllFromBook = connection.prepareStatement("SELECT * FROM inventory.book ORDER BY title;");
			selectAllFromCD = connection.prepareStatement("SELECT * FROM inventory.cd ORDER BY title;");
			selectAllFromDVD = connection.prepareStatement("SELECT * FROM inventory.dvd ORDER BY title;");

			//create prepared statement for inserting mediaItem into specific table (note that table objects cannot be parameterized)
			insertMediaItemBook = connection.prepareStatement("INSERT INTO inventory.book (title,creator,genre,duration,`type`) "
					+ "VALUES(?, ?, ?, ?, ?)");
			insertMediaItemCD = connection.prepareStatement("INSERT INTO inventory.cd (title,creator,genre,duration,`type`) "
					+ "VALUES(?, ?, ?, ?, ?)");
			insertMediaItemDVD = connection.prepareStatement("INSERT INTO inventory.dvd (title,creator,genre,duration,`type`) "
					+ "VALUES(?, ?, ?, ?, ?)");
			
			//create prepared statement for removing media item from the inventory (note that table objects cannot be parameterized)
			removeMediaItemBook = connection.prepareStatement("DELETE FROM inventory.book "
					+"WHERE id = ?");
			removeMediaItemCD = connection.prepareStatement("DELETE FROM inventory.cd "
					+"WHERE id = ?");
			removeMediaItemDVD = connection.prepareStatement("DELETE FROM inventory.dvd "
					+"WHERE id = ?");
			
			//create prepared statement for updating media item (note that table objects cannot be parameterized)
			updateMediaItemBook = connection.prepareStatement("UPDATE inventory.book "
					+ "SET title = ?, creator = ?,  genre = ?, duration = ?, `type` = ? "
					+ " WHERE id = ?");
			updateMediaItemCD = connection.prepareStatement("UPDATE inventory.cd "
					+ "SET title = ?, creator = ?,  genre = ?, duration = ?, `type` = ? "
					+ " WHERE id = ?");
			updateMediaItemDVD = connection.prepareStatement("UPDATE inventory.dvd "
					+ "SET title = ?, creator = ?,  genre = ?, duration = ?, `type` = ? "
					+ " WHERE id = ?");
			
			// prepare ArrayList from data inside of the database
			bookInventory = loadData(MediaType.BOOK);
			cdInventory = loadData(MediaType.CD);
			dvdInventory = loadData(MediaType.DVD);
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1);
		}
		
		//create and update all inventory
		allInventory = new ArrayList<>();
		updateAllArray();
		
		//set view
		this.view = view;
	}
	
	//------------------------------------- LOAD METHODS -------------------------------------
	// method to load the initial data from the database server and place the data into each of the arrayList.
	private ArrayList<MediaItem> loadData(MediaType type)
	{
		ResultSet resultSet = null;
		ArrayList<MediaItem> results = new ArrayList<>();
		
		try
		{
			
			if(type == MediaType.BOOK) 
			{
				resultSet = selectAllFromBook.executeQuery();
				while(resultSet.next())
				{
					results.add(new Book(
							resultSet.getInt("id"),
							resultSet.getString("title"),
							resultSet.getString("creator"),
							resultSet.getString("genre"),
							resultSet.getString("duration")));
				}
			}
			
			else if(type == MediaType.CD) 
			{
				resultSet = selectAllFromCD.executeQuery();
				while(resultSet.next())
				{
					results.add(new CD(
							resultSet.getInt("id"),
							resultSet.getString("title"),
							resultSet.getString("creator"),
							resultSet.getString("genre"),
							resultSet.getString("duration")));
				}
			}
			
			else if(type == MediaType.DVD) 
			{
				resultSet = selectAllFromDVD.executeQuery();
				while(resultSet.next())
				{
					results.add(new DVD(
							resultSet.getInt("id"),
							resultSet.getString("title"),
							resultSet.getString("creator"),
							resultSet.getString("genre"),
							resultSet.getString("duration")));
				}
			}
			
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			closeConnection();
		}
		
		return results;
	}
	
	// method to close database connection.
	public void closeConnection()
	{
		try
		{
			connection.close();
		}
		catch (SQLException sqlException)
		{
			System.out.println("Error Closing");
			sqlException.printStackTrace();
			System.exit(1);
		}
		
	}

	// method to update the allArray variable. to be called after any changes are made to the data in arrayList.
	private void updateAllArray()
	{
		this.allInventory.clear();
		this.allInventory.addAll(bookInventory);
		this.allInventory.addAll(cdInventory);
		this.allInventory.addAll(dvdInventory);
	}

	//------------------------------------- DATA MANIPULATION METHODS -------------------------------------
	// method to refresh data after any manipulation has been done
	private void refreshData()
	{
		// clear all of the inventories
		bookInventory.clear();
		cdInventory.clear();
		dvdInventory.clear();
		
		// reload data into arrayList and update allArray;
		bookInventory.addAll(loadData(MediaType.BOOK));
		cdInventory.addAll(loadData(MediaType.CD));
		dvdInventory.addAll(loadData(MediaType.DVD));
	}
	
	// method to add item to the database with given format: (id/title/creator/genre/duration)
	public int addItem(MediaType type, String[] format)
	{
		int result = 0;
		try
		{
			if(type == MediaType.BOOK)
			{
				insertMediaItemBook.setString(1, format[1]);
				insertMediaItemBook.setString(2, format[2]);
				insertMediaItemBook.setString(3, format[3]);
				insertMediaItemBook.setString(4, format[4]);
				insertMediaItemBook.setString(5, type.toString());
				
				result = insertMediaItemBook.executeUpdate();
				
				refreshData();
				updateAllArray();
			}
			
			else if(type == MediaType.CD)
			{
				insertMediaItemCD.setString(1, format[1]);
				insertMediaItemCD.setString(2, format[2]);
				insertMediaItemCD.setString(3, format[3]);
				insertMediaItemCD.setString(4, format[4]);
				insertMediaItemCD.setString(5, type.toString());
				
				result = insertMediaItemCD.executeUpdate();
				
				refreshData();
				updateAllArray();
			}
			
			else if(type == MediaType.DVD)
			{
				insertMediaItemDVD.setString(1, format[1]);
				insertMediaItemDVD.setString(2, format[2]);
				insertMediaItemDVD.setString(3, format[3]);
				insertMediaItemDVD.setString(4, format[4]);
				insertMediaItemDVD.setString(5, type.toString());
				
				result = insertMediaItemDVD.executeUpdate();
				
				refreshData();
				updateAllArray();
			}
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			closeConnection();	
		}
		
		return result;
	}
	
	// method to remove item from database by id
	public int removeItem(MediaType type, int id)
	{
		int result = 0;
		try
		{
			if (type == MediaType.BOOK)
			{
				removeMediaItemBook.setInt(1, id);
				result = removeMediaItemBook.executeUpdate();
				refreshData();
				updateAllArray();
			}
			
			else if (type == MediaType.CD)
			{
				removeMediaItemCD.setInt(1, id);
				result = removeMediaItemCD.executeUpdate();
				refreshData();
				updateAllArray();
			}
			
			else if (type == MediaType.DVD)
			{
				removeMediaItemDVD.setInt(1, id);
				result = removeMediaItemDVD.executeUpdate();
				refreshData();
				updateAllArray();
			}
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			closeConnection();
		}
		
		return result;
	}
	
	// method to update media item by id with given format: (id/title/creator/genre/duration)
	public int updateItem(MediaType type, String[] format)
	{
		int result = 0;
		try
		{
			if (type == MediaType.BOOK)
			{
				updateMediaItemBook.setString(1, format[1]);
				updateMediaItemBook.setString(2, format[2]);
				updateMediaItemBook.setString(3, format[3]);
				updateMediaItemBook.setString(4, format[4]);
				updateMediaItemBook.setString(5, type.toString());
				updateMediaItemBook.setInt(6,Integer.parseInt(format[0]));
				
				System.out.println(updateMediaItemBook);
				result = updateMediaItemBook.executeUpdate();
				
				refreshData();
				updateAllArray();
			}
			
			else if (type == MediaType.CD)
			{
				updateMediaItemCD.setString(1, format[1]);
				updateMediaItemCD.setString(2, format[2]);
				updateMediaItemCD.setString(3, format[3]);
				updateMediaItemCD.setString(4, format[4]);
				updateMediaItemCD.setString(5, type.toString());
				updateMediaItemCD.setInt(6,Integer.parseInt(format[0]));
				
				result = updateMediaItemCD.executeUpdate();
				
				refreshData();
				updateAllArray();
			}
			
			else if (type == MediaType.DVD)
			{
				updateMediaItemDVD.setString(1, format[1]);
				updateMediaItemDVD.setString(2, format[2]);
				updateMediaItemDVD.setString(3, format[3]);
				updateMediaItemDVD.setString(4, format[4]);
				updateMediaItemDVD.setString(5, type.toString());
				updateMediaItemDVD.setInt(6,Integer.parseInt(format[0]));
				
				result = updateMediaItemDVD.executeUpdate();
				
				refreshData();
				updateAllArray();
			}
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			closeConnection();
		}
		
		return result;	
	}
	
	// method to search inventory by title (easier then searching through database although not as clean)
	public String searchInventory(String searchString)
	{
		MediaItem tempItem = null;
		String mediaToString = null;
		for(int i = 0; i < allInventory.size(); i++)
		{
			tempItem = allInventory.get(i);
			if(searchString.equalsIgnoreCase(tempItem.getTitle()))
			{
					mediaToString = tempItem.toString();
					break;
			}
		}
		
		return mediaToString;
	}
	
	//------------------------------------- DATA QUERY METHODS -------------------------------------
	// method to return the ArrayList containing the objects for the given MediaType entered as a parameter.
	public ArrayList<MediaItem> getInventory(MediaType type)
	{
		if (type == MediaType.BOOK)
			return bookInventory;
		else if (type == MediaType.CD)
			return cdInventory;
		else if (type == MediaType.DVD)
			return dvdInventory;
		else if (type == MediaType.ALL)
			return allInventory;
		else
			return null;
	}
	
	// method to return the MediaItem associated with the given row selection from table model.
	public MediaItem getInventorySingle(MediaType type, int row)
	{
		if (type == MediaType.BOOK)
			return bookInventory.get(row);
		else if (type == MediaType.CD)
			return cdInventory.get(row);
		else if (type == MediaType.DVD)
			return dvdInventory.get(row);
		else if (type == MediaType.ALL)
			return allInventory.get(row);
		else
			return null;
	}
}// end class
