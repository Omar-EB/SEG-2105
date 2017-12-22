import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer {
	
	public static void main (String args[]) {
		int serverPort = 7896; // the server port
    List <Connection> all_connections = new ArrayList<Connection>();
	   	try {
	        InetAddress addr = InetAddress.getLocalHost();
	    
	        // Get IP Address
	        byte[] ipAddr = addr.getAddress();
	    
	        // Get hostname
	        String hostname = addr.getHostName();
	        System.out.println("Server Name: " + hostname + "\nServer Port: " + serverPort);
	    } catch (UnknownHostException e) {
	    }

		try{
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("Server is Ready");
			while(true) {
				System.out.println("listening to client sockets");
				Socket clientSocket = listenSocket.accept();
				System.out.println("connection found, creating a new connection thread");
				Connection c = new Connection(clientSocket);
				all_connections.add(c);
				c.all_connections = all_connections;
			}
		} catch(IOException e) {while (true) System.out.println("IOException Listen socket:"+e.getMessage());}
	}
}
class Connection extends Thread {
	InetAddress addr;
	ObjectInputStream in;
	ObjectOutputStream out;
	Socket clientSocket;
  String name;
  List <Connection> all_connections;
	public Connection (Socket aClientSocket) {
    System.out.println("creating a new connection for client" );
		try {
			clientSocket = aClientSocket;
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
	public void run(){
		System.out.println("server thread started");
		try{
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException x){
			x.getStackTrace();
		}
		while(true){
			try {			                 // an echo server

					// String data = in.readUTF();	                  // read a line of data from the stream
					// out.writeUTF(data);
			  formatted_msg msg = (formatted_msg) in.readObject();
			  if(msg.msg_ctrl==formatted_msg.CTRL.SETUP){
				  name = msg.dest ;
				  System.out.println("Setting up name for client:" + msg.dest);
				  out.writeObject(new formatted_msg (name, "Setup complete"));  
			  } else if(msg.msg_ctrl==formatted_msg.CTRL.LOOPBACK){
				  System.out.println("Client: "+ name +" checking connection");
				  out.writeObject(new formatted_msg (name, "No connection issues"));  
			  } else if(msg.msg_ctrl==formatted_msg.CTRL.NORMAL){
				  boolean found = false ;
				  for (int i=0; i< all_connections.size(); i++) {
					if(all_connections.get(i).name.equals(msg.dest)){
						all_connections.get(i).out.writeObject(msg);
						System.out.println("Sending");
						out.writeObject(new formatted_msg (name, "Sent")) ;
						found = true ;
					}
				}
				if(!found){
					System.out.println("Couldn't find receiver") ;
					out.writeObject(new formatted_msg (name, "Couldn't find receiver")) ;
				}
			  } else if(msg.msg_ctrl==formatted_msg.CTRL.BROADCAST){
				  System.out.println("Broadcasting...");
				  Connection currentCN ;
				for (int i=0; i< all_connections.size(); i++) {
					currentCN = all_connections.get(i) ;
					currentCN.out.writeObject(msg);
				}
			  } else if(msg.msg_ctrl==formatted_msg.CTRL.GET_ALL_CLIENTS){
				 Connection currentCN ;
				 String megaMSG = "";
				for (int i=0; i< all_connections.size(); i++) {
					currentCN = all_connections.get(i) ;
					megaMSG+=currentCN.name+", " ;
				}
				out.writeObject(new formatted_msg (name, megaMSG)) ;
			  } else if(msg.msg_ctrl==formatted_msg.CTRL.TERMINATE){
				  System.out.println("Reply: " + msg);
				  out.writeObject(new formatted_msg (name, "Server terminated")); 
				  Thread.sleep (5000);  
				  break ;
			  }
			  System.out.println("num connection " + all_connections.size());
				}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
				} catch(IOException e) {System.out.println("readline:"+e.getMessage());
				} catch(ClassNotFoundException e) {System.out.println("readline:"+e.getMessage());
				}catch (InterruptedException e){System.out.println("readline:"+e.getMessage());
				}
		}
		/*finally {
			try {
			clientSocket.close();
			for (int i=0; i< all_connections.size(); i++) {
				if (this == (Connection) all_connections.get(i)) {
					System.out.println("Removing connection from the list, for " + name);
					System.out.println("num connection upon removing " + all_connections.size());
					all_connections.remove(i);
					break;
				}
			}
			}catch (IOException e){}
		} */
		

	}
}
