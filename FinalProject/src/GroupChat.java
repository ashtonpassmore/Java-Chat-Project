	import java.net.*;
	import java.io.*;
	import java.util.*;
public class GroupChat{
	    private static final String TERMINATE = "/leave";
	    static String name;
	    static volatile boolean finished = false;
	    public static void groupChat(String userName, String chatName, String groupName, int port) {
	            try {
	                InetAddress group = InetAddress.getByName(groupName);
	                Scanner sc = new Scanner(System.in);
	                name = userName;
	                MulticastSocket socket = new MulticastSocket(port);
	                // Since we are deploying
	                socket.setTimeToLive(0);
	                //this on localhost only (For a subnet set it as 1)
	                  
	                socket.joinGroup(group);
	                Thread t = new Thread(new
	                ReadThread(socket,group,port));
	              
	                // Spawn a thread for reading messages
	                t.start(); 
	                  
	                // sent to the current group
	                System.out.println("welcome to " + chatName +"!");
	                System.out.println("Type messages or type /help for help");
	                System.out.println("------------------------------------");
	                System.out.println("Prior chat history");
	                System.out.println("------------------------------------");
            		DataBase dataBase = new DataBase();
            		dataBase.DataBaseConnection();
            		dataBase.printChatHistory(chatName);
            		System.out.println("------------------------------------");
            		dataBase.DataBaseConnection();
	                dataBase.onlineUsersInsert(chatName, userName);
	                while(true) {
	                    String message;
	                    message = sc.nextLine();
	                    if(message.equalsIgnoreCase(GroupChat.TERMINATE)) {
	                        finished = true;
	                        dataBase.DataBaseConnection();
	                        dataBase.deleteFromOnlineUsers(chatName, userName);
	                        socket.leaveGroup(group);
	                        socket.close();
	                        break;
	                    }else if (message.startsWith("/")){
	                    	// command to list what commands can be used
	                    	if (message.equalsIgnoreCase("/help")) {
	                    		System.out.println("Avaliable Commands");
	                    		System.out.println("/list    : returns the users currently in the chat");
	                    		System.out.println("/history : shows the entire chat history");
	                    		System.out.println("/leave   : leaves the current chat room");
	                    		System.out.println("------------------------------------------------");
	                    		continue;
	                    	}
	                    	// shows list of online users
	                    	else if (message.equalsIgnoreCase("/list")) {
	                    		dataBase.DataBaseConnection();
	                    		dataBase.printOnlineUsers(chatName);
	                    		continue;

	                    	    }
	                    	
	                    	// shows full chat history
	                    	else if (message.equalsIgnoreCase("/history")) {
	                    		dataBase.DataBaseConnection();
	                    		dataBase.printChatHistory(chatName);
	                    		continue;
	                    	}
	                    	else {
	                    		System.out.println
	                    		("Unknown Command please type /help for a list of commands");
	                    		continue;
	                    	}
	                    }
	                    message = name + ": " + message;
	                    byte[] buffer = message.getBytes();
	                    DatagramPacket datagram = new
	                    DatagramPacket(buffer,buffer.length,group,port);
	                    socket.send(datagram);
	            		dataBase.DataBaseConnection();
	            		dataBase.chatHistoryInsert(chatName, message);
	                }
	            }
	            catch(SocketException se)
	            {
	                System.out.println("Error creating socket");
	                se.printStackTrace();
	            }
	            catch(IOException ie)
	            {
	                System.out.println("Error reading/writing from/to socket");
	                ie.printStackTrace();
	            }
	        }
	    }
	class ReadThread implements Runnable
	{
	    private MulticastSocket socket;
	    private InetAddress group;
	    private int port;
	    private static final int MAX_LEN = 1000;
	    ReadThread(MulticastSocket socket,InetAddress group,int port)
	    {
	        this.socket = socket;
	        this.group = group;
	        this.port = port;
	    }
	      
	    @Override
	    public void run()
	    {
	        while(!GroupChat.finished)
	        {
	                byte[] buffer = new byte[ReadThread.MAX_LEN];
	                DatagramPacket datagram = new
	                DatagramPacket(buffer,buffer.length,group,port);
	                String message;
	            try
	            {
	                socket.receive(datagram);
	                message = new
	                String(buffer,0,datagram.getLength(),"UTF-8");
	                if(!message.startsWith(GroupChat.name))
	                	System.out.println(message);
	                
	            }
	            catch(IOException e)
	            {
	                System.out.println("Leaving chat..........");
	            }
	        }
	    }
	}