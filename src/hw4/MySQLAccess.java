package hw4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

				String userdb = resultSet.getString("USER");
				if(user.equals("")) { System.out.println("User not found"); }

				String dbpw = resultSet.getString("password");
				if(!dbpw.equals(password)) { System.out.println("Password incorrect"); check = false;}
				else { check = true; }
				System.out.println("User: " + user);
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
			// "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
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

	public void readDataBase() throws Exception {
		try {
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=sqluser&password=assword");

			String test = getPasswordHash("Testpass");

			preparedStatement = connect.prepareStatement("select USER, password from FEEDBACK.main where USER= ? ; ");
			preparedStatement.setString(1,"Test2");
			resultSet = preparedStatement.executeQuery(); 
			writeResultSet(resultSet);


		} catch (Exception e) {
			throw e;
		} finally {
			//close();
		}

	}


	void writeMetaData(ResultSet resultSet) throws SQLException {
		// now get some metadata from the database
		System.out.println("The columns in the table are: ");
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getString(2);
			String user = resultSet.getString("USER"); 
			//String website = resultSet.getString("ROLE");
			//String summary = resultSet.getString("OWNED");
			String password = resultSet.getString("password");

			String userdb = resultSet.getString("USER");
			if(user.equals("")) { System.out.println("User not found"); }

			String dbpw = "hello";
			if(!dbpw.equals(password)) { System.out.println("Password incorrect"); }

			System.out.println("User: " + user);
			//System.out.println("Website: " + website);
			//System.out.println("Summary: " + summary);

		}
	}

	private static String getPasswordHash(String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		byte[] hash = digest.digest(password.getBytes("UTF-8"));
		return DatatypeConverter.printHexBinary(hash);
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

	public void instructionsNew()
	{
		System.out.println("Please enter a number for a command: ");
		System.out.println("0. Quit");
		System.out.println("1. Create Account");
		System.out.println("2. Login");
	}

	public void instructions()
	{
		System.out.println("Please enter a number for a command: ");
		System.out.println("0. Quit");
		System.out.println("1. Create new file");
		System.out.println("2. Modify file");
		System.out.println("3. Delete");
		System.out.println("4. Logout");
	}
} 