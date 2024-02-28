import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase {
	private Connection c = null;
	private Statement stmt = null;
	
	// data base connection
	public void DataBaseConnection() {
	try {
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/usersdb",
				"postgres","null");
	}catch(Exception e) {
		e.printStackTrace();
		System.err.println(e.getClass().getName()+": "+ e.getMessage());
		System.exit(0);
	}
	}

	//get ip address of a certain chat
	public String getIP_address(String chatName) {
		String groupName = null;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT IP_ADDRESS FROM groupchats WHERE chatname='" + chatName + "'");
			if (rs.next()) {
				groupName = rs.getString("IP_ADDRESS");
			}
			rs.close();
			stmt.close();
			c.close();
			}catch(Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+ e.getMessage());
				System.exit(0);	
		}
		return groupName;
	}
	
	//create a table to put in online users
	public void onlineUsersTableCreation(String chatName) {
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE online" + chatName +
			" (ONLINEUSERS VARCHAR(1000) NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//insert online users into online users table
	public void onlineUsersInsert(String chatName, String userName) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO online" + chatName + " ("
					+ "ONLINEUSERS)"+
					"VALUES('"+userName+"');";
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//print all online users
	public void printOnlineUsers(String chatName) {
	    try {
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM online" + chatName);
	        while (rs.next()) {
	            String onlineUsers = rs.getString("ONLINEUSERS");
	            System.out.println(onlineUsers);
	        }
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//delete users from online table when they go offline
	public void deleteFromOnlineUsers(String chatName, String userName) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE from online" + chatName + " where ONLINEUSERS = '"+userName+"';";
			stmt.executeUpdate(sql);
			c.commit();
			stmt.close();
			c.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);
		}
		
	}
	
	//create a table to store a chats history
	public void historyTableCreation(String chatName) {
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE " + chatName +
			" (MESSAGE VARCHAR(1000) NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//insert messages into chat history table
	public void chatHistoryInsert(String chatName, String message) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO " + chatName + " ("
					+ "MESSAGE)"+
					"VALUES('"+message+"');";
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//print messages from chat history table
	public void printChatHistory(String chatName) {
	    try {
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM " + chatName);
	        while (rs.next()) {
	            String message = rs.getString("MESSAGE");
	            System.out.println(message);
	        }
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//create new table for chats
	public void chatTableCreation() {
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE GROUPCHATS" + 
			"(CHATNAME TEXT NOT NULL," + 
			" IP_ADDRESS VARCHAR(100) NOT NULL," +
			" PORT INT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
			System.out.println("Table has been created.");
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//insert new chat into chat table
	public void insertGroupChat(String chatName, String groupName, int port) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO GROUPCHATS("
					+ "CHATNAME, IP_ADDRESS, PORT)"+
					"VALUES('"+ chatName+"', '"+ groupName + "', + "+port+");";
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Group chat created");
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//check to see if an ip is already assigned to an existing chat
	public boolean checkIPExists(String groupName) {
	    boolean IPExists = false;
	    try {
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM groupchats WHERE ip_address='" + groupName + "'");
	        if (rs.next()) {
	            IPExists = true;
	        }
	        rs.close();
	        stmt.close();
	        c.close();
	        return IPExists;
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        System.exit(0);
	        return IPExists;
	    }
	}
	
	//check to see if a chat name already exists
	public boolean checkChatNameExists(String chatName) {
	    boolean usernameExists = false;
	    try {
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM groupchats WHERE chatname='" + chatName + "'");
	        if (rs.next()) {
	            usernameExists = true;
	        }
	        rs.close();
	        stmt.close();
	        c.close();
	        return usernameExists;
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        System.exit(0);
	        return usernameExists;
	    }
	}
	
	//create a table of users
	public void tableCreation() {
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE USERS" +
			"(USERNAME TEXT NOT NULL," + 
			" PASSWORD TEXT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
			System.out.println("Table has been created.");
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
		
	}
	
	//check to see if a uername already exists
	public boolean checkUsernameExists(String userName) {
	    boolean usernameExists = false;
	    try {
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username='" + userName + "'");
	        if (rs.next()) {
	            usernameExists = true;
	        }
	        rs.close();
	        stmt.close();
	        c.close();
	        return usernameExists;
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        System.exit(0);
	        return usernameExists;
	    }
	}
	
	//check to see if a username and password is in the users table
	public boolean checkCredentials(String username, String password) {
	    boolean credentialsValid = false;
	    try {
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");
	        if (rs.next()) {
	            credentialsValid = true;
	        }
	        rs.close();
	        stmt.close();
	        c.close();
	        return credentialsValid;
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        System.exit(0);
	        return credentialsValid;
	    }
	}
	
	//create new user in user table
	public void insert(String userName, String password) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO USERS("
					+ "USERNAME, PASSWORD)"+
					"VALUES('"+ userName+"', '"+ password + "');";
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Registered");
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);	
		}
	}
	
	//select from the user table
	public void select() {
	
			try {
				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("select * from users;");
				
				while(rs.next()) {
					String userName = rs.getString("username");
					String password = rs.getString("password");
					
					System.out.println("User Name: " +userName);
					System.out.println("Password: " +password);
					System.out.println();
				}
				
				
				System.out.println("Done...");
				rs.close();
				stmt.close();
				c.close();
				
			}catch(Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+ e.getMessage());
				System.exit(0);
			}
	}
		
		// update password of given username in users table
		public void updatePassword(String userName, String password) {
			
			try {
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String sql = "UPDATE USERS set PASSWORD = '" + password +
						"' where USERNAME = '" + userName+"';";
				stmt.executeUpdate(sql);
				c.commit();
				
				ResultSet rs = stmt.executeQuery("select * from USERS;");
				
				while(rs.next()) {
					String userName1 = rs.getString("username");
					String password1 = rs.getString("password");
					
					System.out.println("User Name: " +userName1);
					System.out.println("Password: " +password1);
					System.out.println();
				}
				rs.close();
				stmt.close();
				c.close();
				
			}catch(Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+ e.getMessage());
				System.exit(0);
			}
			}
		
		//update username to new username in users table
		public void updateUsername(String userName, String password) {
			
			try {
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String sql = "UPDATE USERS set USERNAME = '" + userName +
						"' where PASSWORD = '" + password +"';";
				stmt.executeUpdate(sql);
				c.commit();
				
				ResultSet rs = stmt.executeQuery("select * from USERS;");
				
				while(rs.next()) {
					String userName1 = rs.getString("username");
					String password1 = rs.getString("password");
					
					System.out.println("User Name: " +userName1);
					System.out.println("Password: " +password1);
					System.out.println();
				}
				rs.close();
				stmt.close();
				c.close();
				
			}catch(Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+ e.getMessage());
				System.exit(0);
			}
			}
		
		// delete user from user table
		public void delete(String userName, String password) {
			try {
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String sql = "DELETE from USERS where USERNAME = '"+userName+"';";
				stmt.executeUpdate(sql);
				c.commit();
				
				ResultSet rs = stmt.executeQuery("select * from users;");
				
				while(rs.next()) {
					String userName1 = rs.getString("username");
					String password1 = rs.getString("password");
					
					System.out.println("User Name: " +userName1);
					System.out.println("Password: " +password1);
					System.out.println();
				}
				rs.close();
				stmt.close();
				c.close();
				
			}catch(Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+ e.getMessage());
				System.exit(0);
			}
			
		}

	}


