import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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
				System.out.println("New victim...");
				InputStream input = socket.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

	            OutputStream output = socket.getOutputStream();
	            PrintWriter writer = new PrintWriter(output, true);


	            String text;
	            
	            do
	            {
	            	text = reader.readLine();
	            	
	            	if(text.equals("1"))
	            	{
	            		writer.println("one");
	            	}
	            } while (!text.equals("Goodbye"));
	            socket.close();
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
