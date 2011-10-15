package com.dslab.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sun.tools.javac.util.List;

public class Client {

	private static String schedulerHost;
	private static int schedulerTCPPort;
	private static String taskDir;
	private static List<Task> preparedTasks;
	private static Boolean registered = false;	
	
	protected Client(String schedulerHost, int schedulerTCPPort, String taskDir) {
		schedulerHost = this.schedulerHost;
		schedulerTCPPort = this.schedulerTCPPort;
		taskDir = this.taskDir;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 3)
	      {
	         System.err.println("Usage: java Client <schedulerHost> <schedulerTCPPort> <taskDir>");
	         System.exit(1);
	      }
		else {
			schedulerHost = args[0];
			schedulerTCPPort = Integer.parseInt(args[1]);
			taskDir = args[2];
		}
		System.out.println("Client started, ready for login ('!login <company> <password>'):");
		runningClient();
	}

	
	private static void runningClient() {
		Client callback = new Client(schedulerHost, schedulerTCPPort, taskDir);	
		checkConsoleInput(callback);
	}
	
	
	/**
	 * checking the console for input all the time in a thread
	 * @param callback
	 */
	private static void checkConsoleInput(final Client callback) {
		new Thread() {
			public void run() {
				InputStreamReader converter = new InputStreamReader(System.in);
				BufferedReader in = new BufferedReader(converter);
				while(true) {
					final String input;
					try {
						input = in.readLine();
						System.out.println("ready for input");
						new Thread() {
							public void run() {
								handleInput(input);
							}
						}.start();
												
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	/**
	 * This method is called from the checkConsoleInput(running an a thread)
	 * handleInput decides, depending on @input, what to do.
	 * @param input
	 */
	private static void handleInput(String input) {
		if(input.lastIndexOf(" ")!=-1 && input.substring(0, 6).equals("!login") && getCharCount(input, " ") == 2 && registered != true) {
			System.out.println("accepted");
			registerClient(input);
		} else if(input.substring(0, input.length()).equals("!logout") && registered == true) {
			System.out.println("accepted");
			logoutClient(input);
		} else if(input.substring(0, input.length()).equals("!list") && registered == true) {
			System.out.println("accepted");
			listAllTasks();
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 8).equals("!prepare") && getCharCount(input, " ") == 2 && registered == true) {
			System.out.println("accepted");
			prepareTask(input);
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 14).equals("!requestEngine") && getCharCount(input, " ") == 1 && registered == true) {
			System.out.println("accepted");
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 12).equals("!executeTask") && getCharCount(input, " ") == 2 && registered == true) {
			System.out.println("accepted");
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 5).equals("!info") && getCharCount(input, " ") == 1 && registered == true) {
			System.out.println("accepted");
		} else if(input.substring(0, input.length()).equals("!exit") && registered == true) {
			System.out.println("accepted");
		} else {
			System.out.println("INCORRECT INPUT");
		}
	}
	
	/**
	 * initial call for registering to the scheduler
	 * @param registerInput
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private static void registerClient(String input) {
		Socket schedulerSocket;
		try {
			schedulerSocket = new Socket(schedulerHost, schedulerTCPPort);
			DataOutputStream outToServer = new DataOutputStream(schedulerSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(schedulerSocket.getInputStream()));
			outToServer.writeBytes(input + "\n");
			System.out.println("FROM SERVER: " + inFromServer.readLine());
			schedulerSocket.close();
			registered = true;
		} catch (UnknownHostException e) {
			System.out.println("Server does not respond");
			e.printStackTrace();
			registered = false;
		} catch (IOException e) {
			System.out.println("Error Client:registerClient");
			e.printStackTrace();
			registered = false;
		}
	}
	
	/**
	 * logging out the Client. Sending !logout to the Server
	 * @param logout
	 */
	private static void logoutClient(String input) {
		Socket schedulerSocket;
		try {
			schedulerSocket = new Socket(schedulerHost, schedulerTCPPort);
			DataOutputStream outToServer = new DataOutputStream(schedulerSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(schedulerSocket.getInputStream()));
			outToServer.writeBytes(input + "\n");
			System.out.println("FROM SERVER: " + inFromServer.readLine());
			schedulerSocket.close();
			registered = false;
		} catch (UnknownHostException e) {
			System.out.println("Server does not respond");
			e.printStackTrace();
			registered = true;
		} catch (IOException e) {
			System.out.println("Error Client:registerClient");
			e.printStackTrace();
			registered = true;
		}
	}
	
	/**
	 * lists all tasks available in the task directory
	 */
	private static void listAllTasks() {
		for (Task t : preparedTasks) {
			System.out.println(t.taskName + "\n");
		}
	}
	
	/**
	 * adds the input task to the prepared Task list
	 * @param input
	 */
	private static void prepareTask(String input) {
		preparedTasks.add(new Task(input.split(" ")[1], Type.valueOf(input.split(" ")[2])));
	}
	
	/**
	 * sends an Engine request to the Server. the request consists of the Task Type
	 * @param input
	 */
	private static void requestEngineById(int input) {
		Socket schedulerSocket;
		try {	
			schedulerSocket = new Socket(schedulerHost, schedulerTCPPort);
			DataOutputStream outToServer = new DataOutputStream(schedulerSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(schedulerSocket.getInputStream()));
			outToServer.writeBytes("!requestEngine" + getTaskTypeById(input).name() + "\n");
			System.out.println("FROM SERVER: " + inFromServer.readLine());
			schedulerSocket.close();
		} catch (UnknownHostException e) {
			System.out.println("Server does not respond");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error Client:registerClient");
			e.printStackTrace();
		}
	}

	/**
	 * returns the type ot the Taks(id = input)
	 * @param input
	 */
	private static Type getTaskTypeById(int input) {

		for( Task t : preparedTasks) {
			if(t.id == input)
				return t.type;
		}
		return null;
	}
	
	/**
	* count the characters in a specific string
	* @param text
	* @param ch
	* @return
	*/
	private static int getCharCount(String text, String ch) {
		int count = 0;
		while(text.lastIndexOf(ch)!=-1){
			count++;
			text = text.substring(text.indexOf(ch) + 1);
		}
		return count;
	}

}
