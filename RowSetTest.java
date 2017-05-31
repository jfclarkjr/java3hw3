package org.jfclarkjr.java3hw2;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;



public class RowSetTest
{
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mediainventory?useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	public static void main(String[] args)
	{
		
		try(JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet())
		{
			// Set properties for the RowSet
			rowSet.setUrl(DATABASE_URL);
			rowSet.setUsername(USERNAME);
			rowSet.setPassword(PASSWORD);
			rowSet.setCommand("SELECT * FROM mediaitems");
			rowSet.execute();
			
			// Get results of the query
			ResultSetMetaData metaData = rowSet.getMetaData();
			
			while ( rowSet.next())
			{
				for (int i=1; i <= 4; i++)
					System.out.printf("%-16s\t", rowSet.getObject(i));
				
				System.out.println();

			}
				
			
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1);
		}

	}

}
