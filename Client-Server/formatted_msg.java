import java.io.*;
import java.util.Scanner ;
public class formatted_msg implements Serializable{
  public enum CTRL { NORMAL, TERMINATE, LOOPBACK, BROADCAST, SETUP, GET_ALL_CLIENTS};
  String dest;
  String msg;
  CTRL msg_ctrl;
  static Scanner scan = new Scanner(System.in) ;
  public formatted_msg (String dst, String msg){
    this.msg = msg;
    dest = dst;
    msg_ctrl = CTRL.NORMAL;
  }
  public void set_terminate() { msg_ctrl = CTRL.TERMINATE; }
  public void set_loopback() { msg_ctrl = CTRL.LOOPBACK; }
  public void set_ctrl(CTRL ctrl) { msg_ctrl = ctrl; }
  public String toString(){
    String str = "formatted_msg to " + dest + " msg: " + msg;
    switch (msg_ctrl) {
      case NORMAL: str += " NORMAL"; break;
      case TERMINATE: str += " TERMINATE"; break;
      case LOOPBACK: str += " LOOPBACK"; break;
      case BROADCAST: str += " BROADCAST"; break;
      case SETUP: str += " SETUP"; break;
      case GET_ALL_CLIENTS: str += " GET_ALL_CLIENTS"; break;
    }
    return str;
  }
  static formatted_msg init(formatted_msg msg){
	  System.out.println("Destination of the message? :");
	  String dest = scan.nextLine();
	  msg.dest = dest ;
	  
	  System.out.println("Type of message (CTRL) :");
	  String type = scan.nextLine();
	  switch (type) {
		 case  "NORMAL" : msg.set_ctrl(CTRL.NORMAL); break ;
		 case  "TERMINATE" : msg.set_ctrl(CTRL.TERMINATE); break ;
		 case  "LOOPBACK" : msg.set_ctrl(CTRL.LOOPBACK); break ;
		 case  "BROADCAST" : msg.set_ctrl(CTRL.BROADCAST); break ;
//		 case  "SETUP" : msg.set_ctrl(CTRL.SETUP); break ;
		 case  "GET_ALL_CLIENTS" : msg.set_ctrl(CTRL.GET_ALL_CLIENTS); break ;
	  }
	  
	  System.out.println("Message:");
	  String message = scan.nextLine();
	  msg.msg=message ;
	  
    return msg;
  };
}
