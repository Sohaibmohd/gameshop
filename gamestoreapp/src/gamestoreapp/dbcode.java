package gamestoreapp;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class dbcode extends db {
    public dbcode() throws SQLException {
        super();
    }

    @Override
    public int create(String Platform) throws SQLException {
        String q = "CREATE TABLE IF NOT EXISTS " + Platform + " ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) , "
                + "price FLOAT)";
        
        Statement stmt = con.createStatement();
        return stmt.executeUpdate(q);
    }

    @Override
    public int add(String Platform, String name, float price) throws SQLException {
        String q = "INSERT INTO " + Platform + " (name, price) VALUES (?, ?)";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setString(1, name);
        pst.setFloat(2, price);
        return pst.executeUpdate();
    }

    @Override
    public int edit(String Platform, int id, float price) throws SQLException {
        String q = "UPDATE " + Platform + " SET price = ? WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setFloat(1, price);
        pst.setInt(2, id);
        return pst.executeUpdate();
    }

    @Override
    public int remove(String Platform, int id) throws SQLException {
        String q = "DELETE FROM " + Platform + " WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setInt(1, id);
        return pst.executeUpdate();
    }

    @Override
    public void view(String Platform, int id) throws SQLException {
        String q = "SELECT name, price FROM " + Platform + " WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Price:$ " + rs.getFloat("price"));
        } else {
            System.out.println("No records found.");
        }
    }

    @Override
    public void view(String Platform) throws SQLException {
        String q = "SELECT * FROM " + Platform;
        PreparedStatement pst = con.prepareStatement(q);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Price:$ " + rs.getFloat("price"));
            System.out.println("----------------------------");
        }
    }

    public void close() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

	@Override
	public int buyGame(String user, String Platform, int id) throws SQLException {
		
		String q="SELECT name,price FROM " +Platform+ " WHERE id =?";
		PreparedStatement pst =con.prepareStatement(q);
		pst.setInt(1, id);
		ResultSet rs = pst.executeQuery();
		String gameName="";
		float price=0;
		if(rs.next()) {
			gameName = rs.getString("name");
		   price = rs.getFloat("price");
			System.out.println("Name:"+rs.getString("name"));
			System.out.println("Price:$ "+rs.getFloat("price"));
	    } else {
	           System.out.println("Game not found.");
	           return 0;
	       }
		
		String iq="INSERT INTO purchases(user,platform,game_id,game_name,price,purchase_date)VALUES(?,?,?,?,?,?)";
		PreparedStatement ipst =con.prepareStatement(iq);
		ipst.setString(1, user);
		ipst.setString(2,Platform);
		ipst.setInt(3, id);
		ipst.setString(4, gameName);
        ipst.setFloat(5, price);
        ipst.setDate(6, new java.sql.Date(System.currentTimeMillis()));
           return ipst.executeUpdate();
	}

	@Override
	public void viewPurchase(String user) throws SQLException {
		String q="SELECT * FROM purchases WHERE user=?";
		PreparedStatement pst = con.prepareStatement(q);
		pst.setString(1, user);
		ResultSet rs = pst.executeQuery();
			System.out.println("=======PURCHASE HISTORY=======");
			while(rs.next()) {
				System.out.println("Game: "+rs.getString("game_name"));
				System.out.println("Platform: "+rs.getString("platform"));
				System.out.println("Price:$ "+rs.getFloat("price"));
				System.out.println("Purchase Date: "+rs.getDate("purchase_date"));
				System.out.println("---------------------------------");
		}
	}

	@Override


		public void bill(String user,String Platform) throws IOException, SQLException {
		    String q = "SELECT game_name, price, purchase_date FROM purchases WHERE user = ? ORDER BY purchase_date DESC LIMIT 1";
		    
		    try (PreparedStatement pst = con.prepareStatement(q)) {
		        pst.setString(1, user);
		        ResultSet rs = pst.executeQuery();
		        
		        if (rs.next()) {
		            String name = rs.getString("game_name");
		            float price = rs.getFloat("price");
		            String timestamp = rs.getDate("purchase_date").toString();

		            String File = "BILL_" + user + ".txt";

		            try (FileWriter w = new FileWriter(File, true)) {
		                System.out.println("=================");
		                System.out.println("GAME STORE BILL");
		                System.out.println("=================");
		                System.out.println("USER: " + user);
		                System.out.println("GAME: " + name);
		                System.out.println("PLATFORM: " + Platform);
		                System.out.println("PRICE $: " + price);
		                System.out.println("DATE: " + timestamp);
		                System.out.println("===================");
		                System.out.println("Bill Saved to " + File);

		                w.write("=================\n");
		                w.write("GAME STORE BILL\n");
		                w.write("=================\n");
		                w.write("USER: " + user + "\n");
		                w.write("GAME: " + name + "\n");
		                w.write("PLATFORM: " + Platform + "\n");
		                w.write("PRICE $: " + price + "\n");
		                w.write("DATE: " + timestamp + "\n");
		                w.write("===================\n");
		              
		            }
		        } else {
		            System.out.println("No purchase found for user: " + user);
		        }
		    } catch (SQLException | IOException e) {
		        System.out.println("Error: " + e.getMessage());
		    }
		}

	@Override
	public boolean register(String name, String password, String email, String mobile) throws SQLException {
	    try {
	        PreparedStatement ps = con.prepareStatement("INSERT INTO data(username, password, email, mobile) VALUES(?, ?, ?, ?)");
	        ps.setString(1, name);
	        ps.setString(2, password);
	        ps.setString(3, email);
	        ps.setString(4, mobile);
	        
	        int result = ps.executeUpdate();
	        return result > 0;  // Returns true if at least one row is affected (registration success)
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	        return false;  // Returns false if an error occurs
	    }
	}

	    public boolean login(String name, String password) throws SQLException {
	        try {
	            PreparedStatement ps = con.prepareStatement("SELECT * FROM data WHERE username = ? AND password = ?");
	            ps.setString(1, name);
	            ps.setString(2, password);
	            ResultSet rs = ps.executeQuery();

	            return rs.next(); 
	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	            return false; 
	        }
	    }
	}