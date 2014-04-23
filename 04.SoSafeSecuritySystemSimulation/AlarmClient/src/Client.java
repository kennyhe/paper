import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

	public static void main(String[] args) {
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
        if ((args.length != 1) && (args.length != 3)) {
        	System.out.println("Syntax: Client <IP/name of server> [<uid> <pass>]");
        	System.exit(0);
        }

        try {
            echoSocket = new Socket(args[0], 12345);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + args[0]);
            System.exit(1);
        }

		String uid="", pass="";
	    if (args.length == 2) {
			BufferedReader stdIn = new BufferedReader(
		                                   new InputStreamReader(System.in));
			System.out.println("Connecting to server " + args[0]);
			System.out.print("User:");
			try {
				uid = stdIn.readLine();
				System.out.print("Password:");
				pass = stdIn.readLine();
				stdIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } else {
	    	uid = args[1];
	    	pass = args[2];
	    }
		
	    out.println(uid + "/" + pass);
	    out.flush();
	    
	    String buf = null;
    	try {
			buf = in.readLine();
		} catch (IOException e) {
		}
	    while (buf != null) {
	    	System.out.println(buf);
	    	try {
				buf = in.readLine();
			} catch (IOException e) {
				break;
			}
	    }
	
		out.close();
		try {
			in.close();
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
