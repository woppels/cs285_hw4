package hw4;
import hw4.MySQLAccess;

import java.util.Scanner;


public class Main {
  public static void main(String[] args) throws Exception {
    MySQLAccess dao = new MySQLAccess();
    int n;
    String logUser = ""; //user that is currently logged in
    boolean login = false;
    dao.instructionsNew();
	Scanner input = new Scanner(System.in);

	//while((n = input.nextInt()) != 0)
	while(!login)
	{
		n = input.nextInt();
		// Login
		if(n == 2)
		{
			System.out.println("Username: ");
			//Scanner user = new Scanner(System.in);
			String temp = input.next(); 
			//System.out.println(temp);
			System.out.println("Password:"); 
			String password; 
			while(input.hasNext())
			{
				password = input.next();
				if(!dao.login(temp,password)) 
				{
					System.out.println("Try again");
				}
				else 
				{
					System.out.println("Login success");
					login = true;
					logUser = temp; 
					break;
				}
			}
		} // n == 2
		
		// Create account
		if(n == 1)
		{
			System.out.println("Please enter your username: ");
			//Scanner newUser = new Scanner(System.in);
			String temp = input.next();
			System.out.println("Please enter your password: ");
			String pw = input.next();
			// Not trying to make it robust, will assume password correct on first try
			boolean add = dao.addUser(temp, pw);
			if(!add) { System.out.println("User not added, something went wrong"); }
			login = true;
			logUser = temp; 
			System.out.println("Welcome " + temp + "!"); 
		}
	}
	
	// User now assumed to be logged in
	dao.instructions(logUser);
	//Scanner input2 = new Scanner(System.in);
	while(login && (n = input.nextInt()) != 0)
	{
		// List files
		if(n == 1)
		{
			System.out.println("Listing files..."); 
			dao.list();
		}
		
		// Create new file
		if(n == 3)
		{
			System.out.println("Filename:");
			String temp = input.next();input.nextLine();
			System.out.println("Contents: ");
			String cont = input.nextLine();
			boolean check = dao.createFile(logUser, temp, cont);
			if(check) System.out.println("File " + temp + " added.");
			else System.out.println("File " + temp + " already exists, please try again."); 
		}
		
		// Modify file
		if(n == 4)
		{
			System.out.println("Please specify filename:");
			String file = input.next(); input.nextLine(); 
			if(dao.exists(file)) { 
				dao.printContents(file);
			}
			else { System.out.println("File not found, please try again"); dao.instructions(logUser); continue; }
			
			System.out.println("Please specify new contents:");
			String new_contents = input.nextLine();
			boolean check = dao.modify(logUser, file, new_contents);
			if(check) System.out.println("File " + file + " modified successfully");
			
		}
		
		// Delete file
		if(n == 5)
		{
			//Scanner file_name = new Scanner(System.in);
			System.out.println("Please specify filename:"); 
			String file = input.next();
			boolean check = dao.delete(logUser, file); 
			if(check) System.out.println("File " + file + " deleted successfully");
		}

		// List file contents
		if(n == 2)
		{
			System.out.println("Please specify filename:");
			String file = input.next();
			if(dao.exists(file)) { 
				dao.printContents(file);
			}
			else { System.out.println("File not found, please try again"); continue; }
		}
		// Admin create user
		boolean isAdmin = dao.fetchRole(logUser).equals("Admin"); 
		if(n == 6 && isAdmin)
		{
			// Make sure the only admin can use this
			System.out.println("Please specify new user:");
			String newUser = input.next();
			System.out.println("Please enter their temporary password:"); 
			String newPass = input.next();
			boolean success = dao.addUser(newUser, newPass);
			if(success) System.out.println("New user " + newUser + " added."); 
		}
		
		// Admin delete user
		if(n == 7 && isAdmin)
		{
			// Make sure the only admin can use this
			System.out.println("Please specify user to delete:");
			String delete = input.next();
			if(delete.equals(logUser)) { 
				System.out.println("You cannot delete yourself."); 
				dao.instructions(logUser); 
				continue;
			}
			boolean success = dao.deleteUser(delete);
			if(success) System.out.println("User " + delete + " deleted."); 
		}
		dao.instructions(logUser);
	}
	
	System.out.println("Logout.");
  }

}