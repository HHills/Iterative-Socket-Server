import java.io.IOException;
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
		
		System.out.println("Valid input");
		
		try(ServerSocket serverSocket = new ServerSocket(portNum))
		{
			System.out.println("Server is listening on port " + portNum);
			
			while(true)
			{
				Socket socket = serverSocket.accept();
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
