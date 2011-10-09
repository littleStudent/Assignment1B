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
		
	}
	
	private static void checkConsoleInput() throws IOException {
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while(true) {
			String input = in.readLine();
		}
	}
	
	private static void registerClient(String registerInput) throws UnknownHostException, IOException {
		Socket schedulerSocket = new Socket(schedulerHost, schedulerTCPPort);
		DataOutputStream outToServer = new DataOutputStream(schedulerSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(schedulerSocket.getInputStream()));
		outToServer.writeBytes(registerInput + "\n");
		System.out.println("FROM SERVER: " + inFromServer.readLine());
		schedulerSocket.close();
	}

}
