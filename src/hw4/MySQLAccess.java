package hw4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	boolean login(String user, String password) throws Exception 
	{
		boolean check = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect.prepareStatement("select USER, password from FEEDBACK.main where USER= ? ; ");
			preparedStatement.setString(1,user);
			resultSet = preparedStatement.executeQuery(); 

			// Check password
			while (resultSet.next()) {

				String user2 = resultSet.getString("USER");
				if(user2.equals("")) { System.out.println("User not found"); }

				String dbpw = resultSet.getString("password");
				if(!dbpw.equals(password)) { System.out.println("Password incorrect"); check = false;}
				else { check = true; }
			}  

		} catch (Exception e) {
			throw e;
		}
		return check;
	}

	public boolean addUser(String user, String password) throws Exception
	{
		boolean added = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect
					.prepareStatement("insert into FEEDBACK.main values (default, ?, ?, ?)");
			// parameters start with 1
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, "Employee");
			preparedStatement.setString(3, password);
			preparedStatement.executeUpdate();
			added = true;
		} catch (Exception e) {
			throw e;
		}
		
		return added;
	}

	public int fetchID(String user) throws Exception
	{
		int fet = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			Connection connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			PreparedStatement preparedStatement = connect.prepareStatement("select id from FEEDBACK.main where USER= ? ; ");
			preparedStatement.setString(1,user);
			ResultSet resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) {
				fet = resultSet.getInt("id");
			}  
		} catch (Exception e) {
			throw e;
		}
		return fet;
	}
	
	public String fetchRole(String user) throws Exception
	{
		String fet = "Employee";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect.prepareStatement("select ROLE from FEEDBACK.main where USER= ? ; ");
			preparedStatement.setString(1,user);
			resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) {
				fet = resultSet.getString("ROLE");
			}  
		} catch (Exception e) {
			throw e;
		}
		return fet;
	}
	
	public String fetchOwnerbyID(int id) throws Exception
	{
		String owner = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect.prepareStatement("select USER from FEEDBACK.main where id= ? ; ");
			preparedStatement.setInt(1,id);
			ResultSet resultSet2 = preparedStatement.executeQuery();
			resultSet2 = preparedStatement.executeQuery(); 
			while (resultSet2.next()) {
				owner = resultSet2.getString("USER");
			}  
		} catch (Exception e) {
			throw e;
		}
		
		return owner; 
	}
	
	public void list() throws Exception
	{
		//int u_id = fetchID(user);
		String filename = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect.prepareStatement("select name, owner_id from FEEDBACK.files; ");
			//preparedStatement.setInt(1,u_id);
			resultSet = preparedStatement.executeQuery(); 
			int i = 1;
			//System.out.println("\t   File\t\tOwner");
			System.out.printf("File\t\t\tOwner\n");
			while (resultSet.next()) 
			{
				filename = resultSet.getString("name");
				int o_id = resultSet.getInt("owner_id");
				String owner = fetchOwnerbyID(o_id);
				
				// For more consistent printing
				if(filename.length() <  5)
				{
					System.out.printf(i++ + ": %s\t\t\t%s\n", filename, owner);
				}
				else System.out.printf(i++ + ": %s\t\t%s\n", filename, owner);
			}  
		} catch (Exception e) {
			throw e;
		}
	}
	
	public boolean exists(String filename) throws Exception
	{
		boolean exists = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect.prepareStatement("select name from FEEDBACK.files; ");
			ResultSet resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) 
			{
				String db_file = resultSet.getString("name");
				if(filename.equals(db_file)) exists = true;
			}  
		} catch (Exception e) {
			throw e;
		}
		return exists;
	}
	
	public boolean createFile(String user, String filename, String contents) throws Exception
	{	
		boolean check = false;
		try {
			int u_id = fetchID(user);
			
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			PreparedStatement preparedStatement = connect
					.prepareStatement("insert into FEEDBACK.files values (default, ?, ?, ?, ?, ?)");
			// id, filename, contents, created, modified, owner_id
			// 0   1         2         3        4         5
			// parameters start with 1
			Timestamp timestamp = new Timestamp(new Date().getTime());
			// Need to check if filename is already in the database
			if(exists(filename)) return false; 
			
			// Otherwise go ahead and add it. 
			preparedStatement.setString(1, filename);
			preparedStatement.setString(2, contents);
			preparedStatement.setTimestamp(3, timestamp);
			preparedStatement.setTimestamp(4, timestamp);
			preparedStatement.setInt(5, u_id);
			preparedStatement.executeUpdate();
			check = true;
		} catch (Exception e) {
			throw e;
		}
		
		return check;
	}
	
	public void printContents(String filename) throws Exception
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			Connection connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			PreparedStatement preparedStatement = connect
					.prepareStatement("select contents from FEEDBACK.files where name=?;");
			preparedStatement.setString(1,filename);
			ResultSet rs = preparedStatement.executeQuery();

			while(rs.next()) {
				System.out.println("------------------------------------------------");
				System.out.println("***** Current Contents of " + filename + " *****");
				String old_contents = rs.getString("contents");
				System.out.println(old_contents);
				System.out.println("------------------------------------------------");
			}
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean modify(String user, String filename, String contents) throws Exception
	{
		boolean ok = false;
		if(!exists(filename)) {
			System.out.println("File does not exist, please try again.");
			return false; // Can't modify what isn't there. 
		}
		
		// Need to check if the user trying to modify is the owner or admin
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			Connection connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			PreparedStatement preparedStatement = connect
					.prepareStatement("select name, owner_id, contents from FEEDBACK.files where name=?;");
			preparedStatement.setString(1,filename);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				int o_id = rs.getInt("owner_id"); // get file owner's id from file table
				int file_owner = fetchID(user); // get owner's id from main table
				
				// If the id's match, then the file can be modified or if the user is an admin				
				if(o_id == file_owner || fetchRole(user).equals("Admin"))
				{
					Connection update = DriverManager
							.getConnection("jdbc:mysql://localhost/feedback?"
									+ "user=sqluser&password=assword");
					PreparedStatement psUpdate = update.prepareStatement("update files set contents=? where name=? ;");
					psUpdate.setString(1, contents);
					psUpdate.setString(2, filename);
					psUpdate.executeUpdate();
					ok = true;
				}
				else
				{
					System.out.println("Sorry, you do not have permission to modify " + filename); 
					return false;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return ok; 
	}
	
	public boolean delete(String user, String filename) throws Exception
	{
		// Need to check if the user trying to delete is the owner, admin, or manager
		boolean ok = false;
		if(!exists(filename)) {
			System.out.println("File does not exist, please try again.");
			return false; // Can't modify what isn't there. 
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			Connection connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			PreparedStatement preparedStatement = connect
					.prepareStatement("select name, owner_id from FEEDBACK.files;");
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				int o_id = rs.getInt("owner_id"); // get file owner's id from file table
				int file_owner = fetchID(user); // get owner's id from main table
				
				// If the id's match, then the file can be modified or if the user is an admin
				String temp = fetchRole(user); 
				if(o_id == file_owner || temp.equals("Admin") || temp.equals("Manager"))
				{
					Connection update = DriverManager
							.getConnection("jdbc:mysql://localhost/feedback?"
									+ "user=sqluser&password=assword");
					PreparedStatement psUpdate = update.prepareStatement("delete from files where name=? ; ");
					psUpdate.setString(1, filename);
					psUpdate.executeUpdate();
					ok = true;
				}
				else 
				{
					System.out.println("Sorry, you do not have permission to delete " + filename); 
					return false;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return ok; 
	}
	
	public boolean checkPass(String user, String password) throws Exception
	{
		boolean check = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			statement = connect.createStatement();
			preparedStatement = connect.prepareStatement("select USER, password from FEEDBACK.main where USER= ? ; ");
			preparedStatement.setString(1,user);
			resultSet = preparedStatement.executeQuery(); 

			// Check password 
			String dbpw = resultSet.getString("password");
			if(!dbpw.equals(password)) { System.out.println("Password incorrect: " + dbpw); return check;}

			check = true;
		} catch (Exception e) {
			throw e;
		}
		return check;
	}
	
	boolean deleteUser(String user) throws Exception 
	{
		boolean check = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");
			preparedStatement = connect.prepareStatement("delete from FEEDBACK.main where user=? ; ");
			preparedStatement.setString(1,user);
			String role = fetchRole(user); 
			if(role.equals("Admin")) { System.out.println("You cannot have ultimate power."); return false; }
			preparedStatement.executeUpdate(); 

		} catch (Exception e) {
			throw e;
		}
		return check;
	}


	public void instructionsNew()
	{
		System.out.println("Please enter a number for a command: ");
		System.out.println("0. Quit");
		System.out.println("1. Create Account");
		System.out.println("2. Login");
	}

	public void instructions(String logUser) throws Exception
	{
		System.out.println("Please enter a number for a command: ");
		System.out.println("0. Quit");
		System.out.println("1. List files");
		System.out.println("2. List file contents");
		System.out.println("3. Create new file");
		System.out.println("4. Modify file");
		System.out.println("5. Delete a file");
		String role = fetchRole(logUser);
		if(role.equals("Admin")) {
			System.out.println("6. Create user."); 
			System.out.println("7. Delete user.");
		}
		
	}
} 