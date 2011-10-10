package com.dslab.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private static String schedulerHost;
	private static int schedulerTCPPort;
	private static String taskDir;
	
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
		try {
			System.out.println("Client started, ready for login ('!login <company> <password>'):");
			runningClient();
		} catch (UnknownHostException e) {
			System.err.println("Couldnt reach host.");
	         System.exit(1);
		} catch (IOException e) {
			System.err.println("IOException");
	         System.exit(1);
		}
	}

	
	private static void runningClient() throws UnknownHostException, IOException {
		Client callback = new Client(schedulerHost, schedulerTCPPort, taskDir);
		checkConsoleInput(callback);
	}
	
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
								callback.handleInput(input);
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
	
	private void handleInput(String input) {
		
		if(input.lastIndexOf(" ")!=-1 && input.substring(0, 6).equals("!login") && getCharCount(input, " ") == 2) {
			System.out.println("accepted");
		} else if(input.substring(0, input.length()).equals("!logout")) {
			System.out.println("accepted");
		} else if(input.substring(0, input.length()).equals("!list")) {
			System.out.println("accepted");
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 8).equals("!prepare") && getCharCount(input, " ") == 2) {
			System.out.println("accepted");
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 14).equals("!requestEngine") && getCharCount(input, " ") == 1) {
			System.out.println("accepted");
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 12).equals("!executeTask") && getCharCount(input, " ") == 2) {
			System.out.println("accepted");
		} else if(input.lastIndexOf(" ")!=-1 && input.substring(0, 5).equals("!info") && getCharCount(input, " ") == 1) {
			System.out.println("accepted");
		} else if(input.substring(0, input.length()).equals("!exit")) {
			System.out.println("accepted");
		} else {
			System.out.println("INCORRECT INPUT");
		}
	}
	
	private void registerClient(String registerInput) throws UnknownHostException, IOException {
		Socket schedulerSocket = new Socket(schedulerHost, schedulerTCPPort);
		DataOutputStream outToServer = new DataOutputStream(schedulerSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(schedulerSocket.getInputStream()));
		outToServer.writeBytes(registerInput + "\n");
		System.out.println("FROM SERVER: " + inFromServer.readLine());
		schedulerSocket.close();
	}
	
	/**
	 * count the characters in a specific string
	 * @param text
	 * @param ch
	 * @return
	 */
	private int getCharCount(String text, String ch) {
		int count = 0;
		while(text.lastIndexOf(ch)!=-1){
			count++;
			text = text.substring(text.indexOf(ch) + 1);
		}
		return count;
	}

}
