import java.net.*;


public class Main {
	
	static ChatServer cs = new ChatServer();
	
	public Main()
	{
		
	}
	
	public static void initChatServer() throws Exception
	{
		String[] options = null;
		cs.init(options);
	}

	/*
	 * 	Temporarily just having an infinite loop in the main
	 *	method running the server until the program terminates
	*/
//	public static void main(String[] args) throws Exception {
//	java.net.InetAddress addr = InetAddress.getLocalHost();
//        System.out.println(addr.getHostName());
//        System.out.println("The chat server is running.");
//        ServerSocket listener = new ServerSocket(cs.PORT);
//        try {
//            while (true) {
//                new Handler(listener.accept()).start();
//            }
//        } 
//        finally {
//            listener.close();
//        }
//    }
	
	public static void main(String[] args) throws Exception{
		initChatServer();
	}
}
