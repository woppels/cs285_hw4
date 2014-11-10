package hw4;
import hw4.MySQLAccess;
import java.util.Scanner;


public class Main {
  public static void main(String[] args) throws Exception {
    MySQLAccess dao = new MySQLAccess();
    int n;
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
			Scanner user = new Scanner(System.in);
			String temp = user.next(); 
			System.out.println(temp);
			System.out.println("Password:"); 
			String password; 
			while(user.hasNext())
			{
				password = user.next();
				if(!dao.login(temp,password)) 
				{
					System.out.println("Try again");
				}
				else 
				{
					System.out.println("Login success");
					login = true;
					break;
				}
			}
		} // n == 2
		
		// Create account
		if(n == 1)
		{
			System.out.println("Please enter your username: ");
			Scanner newUser = new Scanner(System.in);
			String temp = newUser.next();
			System.out.println("Please enter your password: ");
			String pw = newUser.next();
			// Not trying to make it robust, will assume password correct on first try
			boolean add = dao.addUser(temp, pw);
			if(!add) { System.out.println("User not added, something went wrong"); }
			login = true;
			System.out.println("Welcome " + temp + "!"); 
		}
	}
	
	System.out.println("past login");
    //dao.readDataBase();
	
	// User now assumed to be logged in
	dao.instructions();
    
  }

}