import java.net.*;
import java.io.*;
import java.util.Scanner;
public class TCPClient {
	public static void main (String args[]) {
		// arguments supply message and hostname
		// Check command line
		if (args.length < 2) {
			System.err.println("Usage : ");
			System.err.println("java TCPClient <Name> <server>");
			System.exit (1);
		}    
		Socket s = null;
		try{
			int serverPort = 7896;
      System.out.println("starting a new client socket");
			s = new Socket(args[1], serverPort);    
			ObjectOutputStream out =new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream( s.getInputStream());
      System.out.println("subscribing as: " + args[0]);
      formatted_msg msg = new formatted_msg(args[0], "Setup message");
      formatted_msg.CTRL ctrl = formatted_msg.CTRL.SETUP;
      msg.set_ctrl(ctrl);
			// out.writeUTF(args[0]);      	// UTF is a string encoding see Sn. 4.4
      System.out.println("sending " + msg);
	  out.writeObject(msg);
		  System.out.println("receiving response");
				// String data = in.readUTF();	    // read a line of data from the stream
		  msg = (formatted_msg) in.readObject();
				System.out.println("Received: "+ msg) ;
				
	 Scanner sc = new Scanner(System.in);
	  while(true){
			System.out.println("enter 'W' to write a message");
			String answer = sc.nextLine();
			if(answer.equals("W") || answer.equals("w") ){
			  msg = formatted_msg.init(msg);
			  System.out.println("sending " + msg);
			  out.writeObject(msg);
			}
		  System.out.println("receiving response");
				// String data = in.readUTF();	    // read a line of data from the stream
		  msg = (formatted_msg) in.readObject();
				System.out.println("Received: "+ msg) ;
	  	
			Thread.sleep (5000);
	  }
		}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}catch (ClassNotFoundException e){System.out.println("readline:"+e.getMessage());
		}catch (InterruptedException e){System.out.println("readline:"+e.getMessage());
		}finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
  }
}
