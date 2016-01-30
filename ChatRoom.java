//These two files are a host/client for a simple chatroom. Both can send and recieve messages.
//I have only tested it over  a local network and only with 2 computers (I only own a laptop and a desktop)
//Everything works from what I can tell except for a small weirdness if you send more than one word.
//"Sent: How are you" comes out as "Recived: How", "Recived: are", "Recived: you". 
//I think the "problem" is with readUTF() and writeUTF(). There are other read/write options but I haven't messed with them.
//This is literaly the only thing I have ever made with Sockets, so there is a good chance I missed some stuff.
//Also: Github did some wierd stuff with tabs that I cant figure out how to fix.

//------------------------------------------------------------------------------------------------
// The Host server
//------------------------------------------------------------------------------------------------
import java.net.*;
import java.util.Scanner;
import java.io.*;

//Class used as host for the chat room (as the name implies)
public class ChatRoomHost implements Runnable {

	private ServerSocket serverSocket;//From what I can tell sockets establish a connection between 2 computers
	private Socket server;            //then lock it down. Still don't have a great understanding of it.
	private DataInputStream in;
	private static DataOutputStream out;//Sockets still need a dedicate in/out stream
	public static boolean quit = false;
	
	//Constructor sets the port to use and and timeout time
	public ChatRoomHost(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	
	//Sets up the connection between this host and whatever client is trying to connect
	public void connect() {
         try {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            server = serverSocket.accept(); //.accept() opens the connection and the client socket must
                                            // be declared and set before this times out
            in = new DataInputStream(server.getInputStream());	//sets up the in/out streams
            out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("You are connecting to "	//Writes to the client to let them know it worked
              + server.getLocalSocketAddress());
         }catch(SocketTimeoutException s)// SocketTimeoutException is thrown if .accept() doesn't get what it wants in time
         {
            System.out.println("Socket timed out!");
         }catch(IOException e)
         {
            e.printStackTrace();
         }
    }

	//Function to handle actually sending the message
	public void send(String outMessage){
		if(out != null){ //Makes sure the outstream is setup
			try {
            out.writeUTF(outMessage);  //This is the command that send the message
            //writeUTF() might not be what we need to use but it was in all the 
            //examples. There are more options but I haven't played with them
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No connected Sockets");
		}
	}
	
	//Shuts down the whole thing and closes any open Sockets and Streams (I may have missed some so possible leaks)
	public void quit(){
		quit=true;
		try {
			in.close();
			out.close();
			server.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//A tread that runs a loop checking for input, post it if there is
	public void run(){
		while(!quit){
			try {
				if(in.available() > 0){ //If you try to read before there is anything to read everything locks up
					System.out.println("Recived: " +  in.readUTF());//.readUTF() is what receives the message from the client
                                                          //Again UTF might not be what we need
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String [] args) throws IOException
	{
	   java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
	   String hostname = addr.getHostName();  //Honestly don't know what exactly this is, just stole it from Stackoverflow
	   System.out.println("Hostname of system = " + hostname);
	   System.out.println("Port = 6066");
	   
	   Scanner hostInput = new Scanner(System.in);//Scanner for user text input
	   ChatRoomHost host = new ChatRoomHost(6066);//Implementing the Chat Room Host
	   String hostMessage;
        
        while(!quit){// feel like it's pretty self explanatory from here
        	hostMessage = hostInput.next();
        	if(hostMessage.equals("~quit")){
        		host.quit();
        	} else if(hostMessage.equals("~connect")){
        		host.connect();
                new Thread(host).start();
        		System.out.println("Connected");
        	} else {
        		System.out.println("Sent: " + hostMessage);
        		host.send(hostMessage);
        	}
        }
      System.out.println("Good Bye!");
      hostInput.close();
	}
}

//------------------------------------------------------------------------------------------------
//  The Client: 90% the same as the Host so I didn't bother commenting too much
//------------------------------------------------------------------------------------------------

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatRoomClient implements Runnable{

    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    public static boolean quit;
    
    public ChatRoomClient() {
        
    }
    
    public void connect(int port, String serverName) {
        System.out.println("Connecting...");
        try{
        	client = new Socket(serverName, port);  //Only real difference from the host, instead of a SeverSocket then a Socket
                                                  //the client only uses a single socket and feeds it the port and host name
                                                  //This is the part that must be done between host.accept() and timeout
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
            out.writeUTF("You are connected to "
                      + client.getLocalSocketAddress());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void run(){
        while(!quit){
            try {
                if(in.available()>0){
                    System.out.println("Recived: " + in.readUTF());
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
	}
    
    public void send(String outMessage){
        if(out != null){
            try {
                out.writeUTF(outMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No connected Sockets");
        }
    }
    
    public void quit(){
		quit=true;
		try {
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
            
    public static void main(String[] args) {
        Scanner clientInput = new Scanner(System.in);
        ChatRoomClient client = new ChatRoomClient();
        String clientMessage;

        while(!quit){
            clientMessage = clientInput.next();
            if(clientMessage.equals("~quit")){
                    client.quit();
            } else if(clientMessage.equals("~connect")){
                System.out.println("Host Name: ");
                String hostName = clientInput.next();
                System.out.println("Port: ");
                int port = Integer.parseInt(clientInput.next());
                    client.connect(port, hostName); //I couldn't test weather this works on a non-local network or not
                    new Thread(client).start();     //but I'm 90% sure switching out host name for IP address will make it work
            } else {
                    System.out.println("Sent: " + clientMessage);
                    client.send(clientMessage);
            }
        }
       System.out.println("Good Bye!");
       clientInput.close();
    }
    
}
