package gamestoreapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class db {        
	String url="jdbc:mysql://localhost:3306/gamestore";
	String name="root";
	String pass="aysha123";
	protected Connection con;
	
	public db ()throws SQLException{
		 con = DriverManager.getConnection(url,name,pass);	
	}
	public abstract int create(String Platform) throws SQLException;
	public abstract int add(String Platform,String name,float price) throws SQLException;
	public abstract int edit(String Platform,int id,float price) throws SQLException;
	public abstract int remove(String Platform,int id) throws SQLException;
	public abstract void view(String Platform, int id) throws SQLException;
	public abstract void view(String Platform) throws SQLException; 
	public abstract int buyGame(String user,String Platform,int id) throws SQLException;
	public abstract void viewPurchase(String user) throws SQLException;
	public abstract void bill(String user,String Platform) throws SQLException, IOException;
	public abstract boolean register(String name,String password,String email,String mobile) throws SQLException;
	public abstract boolean login(String name,String password) throws SQLException;
	
	public void close() throws SQLException{
		
		if(con!=null) {
			con.close();
			System.out.println("Database Close");
		}
		
	}
		
		
	

}
