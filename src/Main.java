import java.util.Scanner;


public class Main {
  public static void main(String[] args) throws Exception {
    MySQLAccess dao = new MySQLAccess();
    int n;
    boolean login = false;
    dao.instructionsNew();
	Scanner input = new Scanner(System.in);
	while((n = input.nextInt()) != 0 && !login)
	{
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
				if(!password.equals("correct")) 
				{
					System.out.println("Try again");
				}
				else 
				{
					System.out.println("Login success");
				}
			}
		} // n == 2
		
		// Create account
		if(n == 1)
		{
			
		}
	}
    
    dao.readDataBase();
    
  }
}