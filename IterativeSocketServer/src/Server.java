import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


public class Server 
{
	public static void main(String[] args) 
	{
		Scanner scnr = new Scanner(System.in);
		
		System.out.println("Please enter the port number: ");
		String num = scnr.nextLine();
		
		int portNum = inputValidation(num);
		
		if(portNum == -1)
		{
			while(portNum == -1)
			{
				System.out.println("The information you have entered is not an integer! \nPlease enter the port number:");
				num = scnr.nextLine();
				portNum = inputValidation(num);
			}
		}
				
		try(ServerSocket serverSocket = new ServerSocket(portNum))
		{
			System.out.println("Server is listening on port " + portNum);
			
			while(true)
			{
				Socket socket = serverSocket.accept();
				System.out.println("New client connected");
                new ServerRequestHandler(socket).run(); //use ServerRequestHandler to handle
                									    //every input from the user
			}
		}
		catch (IOException e)
		{
			System.out.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	public static int inputValidation(String num)
	{
		try
		{
			int portNum = Integer.valueOf(num);
			return portNum;
			
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
	}
	
}

class ServerRequestHandler implements Runnable
{
	private Socket socket;
	public ServerRequestHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run() 
	{	
		try(InputStream input = socket.getInputStream())
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));	         
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			
	        String text;
	            
	        text = reader.readLine(); //Reads in client thread input
	        
	        switch(text) //executes based on input number
	        {
	        	case "1":
	        		writer.println(new Date().toString());
	        		break;
	        	case "2":
	        	{
	        		//This gets the server's uptime
	        		RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
	        		long uptime = rb.getUptime(); //these are in milliseconds
	        			
	        		long days = uptime / (24 * 60 * 60 * 1000);
	        		long hours = (uptime / (60 * 60 * 1000)) % 24;
	        		long minutes = (uptime / (60 * 1000)) % 60;
	        		long seconds = (uptime / 1000) % 60;
        	        
	        		writer.println("Uptime: "
	        						+ days + " Day(s), " + hours + " Hour(s), " + minutes
	        						+ " Minute(s), " + seconds + " Second(s)");
	        		break;
	        	}
        		case "3":
        		{
        			Runtime rt = Runtime.getRuntime(); //use runtime to get memory information
        			long totalMem = rt.totalMemory(); //these are in bytes
        			long freeMem = rt.freeMemory();
        			long usedMem = totalMem - freeMem;
        			
        			long usedMemMB = usedMem / (1024*1024); //conversion to megabytes

        			writer.println("Memory used: " + usedMem + " B, " + usedMemMB + " MB");
        			break;
        		}
        		case "4":
        			writer.println("You have entered 4");
        			break;
        		case "5":
        			writer.println("You have entered 5");
        			break;
        		case "6":
        			writer.println("You have entered 6");
        			break;
        		case "7":
        			writer.println("Exiting program...");
        			break;
        		default:
        			writer.println("Unrecognized input");
	        }	
		}
		catch (IOException e)
		{
			System.out.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
