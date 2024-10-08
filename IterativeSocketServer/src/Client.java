import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client 
{
	public static void main(String[] args) 
	{		
		Scanner scnr = new Scanner(System.in);

		System.out.println("Please enter the network address: ");
		String netAddr = scnr.nextLine();
		
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
		try(Socket socket = new Socket(netAddr, portNum))
		{
			OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            Console console = System.console();
            String text;
            
            do
            {
            	text = console.readLine("\nPlease enter the number corresponding to the desired command: \n"
            			+ "1. Date and Time\n2. Uptime\n3. Memory Use\n4. Netstat\n5. Current Users\n"
            			+ "6. Running Process\n7. Exit Program\n");
            	
            	
            	writer.println(text);

            	InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
                String time = reader.readLine();
 
                System.out.println(time);
 
            	
            } while (!text.equals("7"));
            socket.close();
		}
		catch(UnknownHostException e)
		{
 
            System.out.println("Server was not found: " + e.getMessage());
		}
		catch(IOException e)
		{
			System.out.println("I/O error: " + e.getMessage());
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
