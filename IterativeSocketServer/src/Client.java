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
		
		System.out.println("Choose your number of client requests: 1, 5, 10, 15, 20, 25");
		String sessions = scnr.nextLine();
		
		int clientSessions = inputValidation(sessions);
		
		//If clause ensures that the input is a number and fits the request options
		if(clientSessions == -1 || (clientSessions != 1 && clientSessions != 5 && clientSessions != 10 &&
                clientSessions != 15 && clientSessions != 20 && clientSessions != 25))
		{
			while(clientSessions == -1 ||(clientSessions != 1 && clientSessions != 5 && clientSessions != 10 &&
	                clientSessions != 15 && clientSessions != 20 && clientSessions != 25))
			{
				if(clientSessions == -1)
				{
					System.out.println("The information you have entered is not an integer! \nChoose your number of client sessions: 1, 5, 10, 15, 20, 25");
				}
				else
				{
					System.out.println("The information you have entered is not in the range of options! \nChoose your number of client sessions: 1, 5, 10, 15, 20, 25");
				}
				sessions = scnr.nextLine();
				clientSessions = inputValidation(sessions);
			}
		}
		
		String option;
		
		do
		{
			System.out.println("\nPlease enter the number corresponding to the desired command: \n"
           						+ "1. Date and Time\n2. Uptime\n3. Memory Use\n4. Netstat\n5. Current Users\n"
           						+ "6. Running Process\n7. Exit Program\n");
			option = scnr.nextLine();
			
			long totalTurnAroundTime = 0;

			for(int i = 0; i < clientSessions; i++)
			{				
				Thread clientThread = new Thread(new ClientRequestThread(netAddr, portNum, option));
				
				//Get start time
				long startTime = System.currentTimeMillis();
				
				//Start thread
				clientThread.start();
				
				try {
	                clientThread.join();  //This ensures that the thread is finished first
	                
					System.out.println("\nStart time: " + startTime + " ms");
	                long endTime = System.currentTimeMillis();
					System.out.println("End time: " + endTime + " ms");
					
					long turnAroundTime = endTime - startTime;
					System.out.println("Turnaround time: " + turnAroundTime + " ms\n");
					
					
					totalTurnAroundTime += turnAroundTime; //adding up total turnaround time
					
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
			}
			
			double decimalClientSessions = (double) clientSessions; //ensuring we get decimal precision
			double avgTurnAroundTime = totalTurnAroundTime/decimalClientSessions;
			System.out.println("\nTotal turnaround time: " + totalTurnAroundTime + " ms");
			System.out.println("Average turnaround time: " + avgTurnAroundTime + " ms\n");
			
		}while(!option.equals("7"));
		
	}
	
	public static int inputValidation(String num)
	{
		try
		{
			int intNum = Integer.valueOf(num);
			return intNum;
			
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
	}
}

class ClientRequestThread implements Runnable
{
	private String netAddr;
	private int portNum;
	private String userOpt; //Represents the user input
	
	public ClientRequestThread(String netAddr, int portNum, String userOpt)
	{
		this.netAddr = netAddr;
		this.portNum = portNum;
		this.userOpt = userOpt;
	}

	@Override
	public void run()
	{
		try(Socket socket = new Socket(netAddr, portNum))
		{
			OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            writer.println(userOpt); //Sends client's response to server
            
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            String serverResp = reader.readLine(); //Gets server's response
            System.out.println(serverResp); //Prints server's response
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
}
