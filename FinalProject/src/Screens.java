import java.util.Random;
import java.util.Scanner;
public class Screens {
	public static void mainScreen() {
	while(true){
		System.out.println("Welcome to Ashton's chat app");
		System.out.println();
		System.out.println("Please select from the following options:");
		System.out.println("(R)egister, (L)ogin, (Q)uit");
		System.out.println("-----------------------------------------");
		Scanner scnr = new Scanner(System.in);
		System.out.println("Select: R, L, or Q.");
		String option = scnr.next();
		System.out.println(option + " is selected");
			//register new user
			if(option.equalsIgnoreCase("R") == true) {
				System.out.println("Please enter a user name and password for registration");
				System.out.println("user name: ");
				String userName = scnr.next();
				System.out.println("password: ");
				String password = scnr.next();
				DataBase dataBase = new DataBase();
				dataBase.DataBaseConnection();
				boolean checkUsernameExists = dataBase.checkUsernameExists(userName);
					if(checkUsernameExists == false) {
						dataBase.DataBaseConnection();
						dataBase.insert(userName, password);
						Screens.LoggedInScreen(userName, password);
					}
					else {
						System.out.println("username already exists please login with that username"
								+ " or register with a different username");
						continue;
					}
				}
			//log in a user
			else if (option.equalsIgnoreCase("L")) {
				System.out.println("Please enter a user name and password for login");
				System.out.println("user name: ");
				String userName = scnr.next();
				System.out.println("password: ");
				String password = scnr.next();
				DataBase dataBase = new DataBase();
				dataBase.DataBaseConnection();
				boolean checkCredentials = dataBase.checkCredentials(userName, password);
					if (checkCredentials == true) {
						Screens.LoggedInScreen(userName, password);
					}
					else {
						System.out.println("Username or password is wrong or username doesn't exist");
						continue;
					}
			}
			//close app
			else if (option.equalsIgnoreCase("Q")){
				System.out.println("exiting......");
				System.exit(0);
			}
			//if given an invalid input
			else {
				System.out.println("Option " + option + " is invalid please select something else.");
				continue;
			}
			}
	}
	public static void LoggedInScreen(String userName, String password) {
		while(true) {
		System.out.println("Welcome " + userName + "!");
		System.out.println("Please select from the following options:");
		System.out.println("(J)oin, (C)reate, (A)ccount, or (L)ogout");
		System.out.println("-----------------------------------------");
		Scanner scnr = new Scanner(System.in);
		System.out.println("Select: J, C, A, or L.");
		String option = scnr.next();
		System.out.println(option + " is selected");
		DataBase dataBase = new DataBase();
			//join a chat room
			if (option.equalsIgnoreCase("J")) {
				System.out.println("Please enter chat room name");
				System.out.println("room name: ");
				String chatName = scnr.next();
				DataBase dataBase1 = new DataBase();
				dataBase1.DataBaseConnection();
				boolean checkGroupchat = dataBase1.checkChatNameExists(chatName);
					if (checkGroupchat == true) {
						dataBase1.DataBaseConnection();
						int port = 1234;
						String groupName = dataBase1.getIP_address(chatName);
						GroupChat.groupChat(userName, chatName, groupName, port);;
					}
					else {
						System.out.println("chat doesn't exist");
						continue;
					}
			}
			//create a chat room
			else if (option.equalsIgnoreCase("C")) {
				System.out.println("Please enter name for new chat room");
				System.out.println("chat name: ");
				String chatName = scnr.next();
				DataBase dataBase1 = new DataBase();
				boolean checkifUorN = isChatNameUppercase(chatName);
				dataBase1.DataBaseConnection();
				boolean checkGroupchat = dataBase1.checkChatNameExists(chatName);
					if (checkGroupchat == false && checkifUorN == false) {
						dataBase1.DataBaseConnection();
							Random rand = new Random();
							int num1 = 239;
							int num2 = rand.nextInt(256);
							int num3 = rand.nextInt(256);
							int num4 = rand.nextInt(256);
							
							String groupName = num1 + "."+num2+"."+num3+"."+num4;
							boolean checkGroupName = dataBase1.checkIPExists(groupName);
							if(checkGroupName == false) {
								dataBase1.DataBaseConnection();
								String checkedGroupName = groupName;
								int port = 1234;
								dataBase1.insertGroupChat(chatName, checkedGroupName, port);
								dataBase1.DataBaseConnection();
								dataBase1.historyTableCreation(chatName);
								dataBase1.DataBaseConnection();
								dataBase1.onlineUsersTableCreation(chatName);
								dataBase1.DataBaseConnection();
								GroupChat.groupChat(userName, chatName, groupName, port);;
							}
							//if the random numbers somehow equal one of the ones int the chart
							//then it'll go to the beggining and ask to try agian
							else {
								System.out.println("Something went wrong please try agian");
								continue;
							}
					}
					
					else if (checkifUorN == true){
						System.out.println("Chat name can't contain uppercase letters or numbers");
						continue;
					}
					else {
						System.out.println("chat already exits");
						continue;
					}
			}
			//change account username or password
			else if (option.equalsIgnoreCase("A")){
				System.out.println("Change (U)sername, (P)assword, or (B)ack");
				System.out.println("--------------------------------");
				Scanner scnr1 = new Scanner(System.in);
				System.out.println("Select: U, P, or B.");
				String option1 = scnr1.next();
				System.out.println(option1 + " is selected");
				if (option1.equalsIgnoreCase("U")) {
					System.out.println("Enter new user name: ");
					String newUserName = scnr1.next();
					dataBase.DataBaseConnection();
					boolean checkUsernameExists = dataBase.checkUsernameExists(newUserName);
						if(checkUsernameExists == false) {
							dataBase.DataBaseConnection();
							dataBase.updateUsername(newUserName, password);
							System.out.println("Username updated to " + newUserName);
							Screens.LoggedInScreen(newUserName, password);
						}
						else {						
							System.out.println("username already exists please"
								+ " change to a different username");
						continue;
							
						}
				
				}
				else if (option1.equalsIgnoreCase("P")) {
					System.out.println("enter new password: ");
					String newPassword = scnr1.next();
					dataBase.DataBaseConnection();
					dataBase.updatePassword(userName, newPassword);
					System.out.println("password updated to " + newPassword);
					
				}
				else if (option1.equalsIgnoreCase("B")) {
					System.out.println("Going Back......");
					Screens.LoggedInScreen(userName, password);
				}
				else {
					System.out.println("invalid selection");
					continue;
				}
			}
			//log out of the app
			else if (option.equalsIgnoreCase("L")){
				System.out.println("Logged out......");
				Screens.mainScreen();
			}
			//if given an invalid selection
			else {
				System.out.println("Option " + option + " is invalid please select something else.");
				continue;
			}
			}
		}
	public static boolean isChatNameUppercase(String chatName) {
		boolean containsUorN = false;
		for(int i = 0; i<chatName.length(); i++) {
			char c = chatName.charAt(i);
			if (Character.isDigit(c)){
				containsUorN = true;				
			}
			else if (Character.isUpperCase(c)) {
				containsUorN = true;
			}
		}
		return containsUorN;
	}
}