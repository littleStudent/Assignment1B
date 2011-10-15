package com.dslab.scheduler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dslab.client.Client;

public class Scheduler {

	private static int tcpPort;
	private static int udpPort;
	private static int min;
	private static int max;
	private static int timeout;
	private static int checkPeriod;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 6)
	      {
	         System.err.println("Usage: java Client <tcpPort> <udpPort> <min> <max> <timeout> <checkPeriod>");
	         System.exit(1);
	      }
		else {
			tcpPort = Integer.parseInt(args[0]);
			udpPort = Integer.parseInt(args[1]);
			min = Integer.parseInt(args[2]);
			max = Integer.parseInt(args[3]);
			timeout = Integer.parseInt(args[4]);
			checkPeriod = Integer.parseInt(args[5]);
		}
		System.out.println("Scheduler running, ready for action:");
		runningScheduler();
	}
	
	private static void runningScheduler() {
		startTcpSocket();
		startUdpSocket();
		checkConsoleInput();
	}
	
	private static void checkConsoleInput() {
		new Thread() {
			public void run() {
				InputStreamReader converter = new InputStreamReader(System.in);
				BufferedReader in = new BufferedReader(converter);
				while(true) {
					try {
						String input = in.readLine();
						System.out.println("ready for input");
						new Thread() {
							public void run() {
								//handleInput(input);
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
	
	private static void startTcpSocket() {
		new Thread() {
			public void run() {
				try {
					ServerSocket schedulerSocket = new ServerSocket(tcpPort);
					while(true) {
						Socket clientSocket = schedulerSocket.accept(); 
						new Thread () {
							public void run() { 	
								/*BufferedReader inFromClient =
							               new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
							    clientSentence = inFromClient.readLine();
							    System.out.println("Received: " + clientSentence);
							    capitalizedSentence = clientSentence.toUpperCase() + '\n';
							    outToClient.writeBytes(capitalizedSentence);*/
							}
							
							private void handleInput(String input) {
								
							}
						}.start();
					}
				} catch (UnknownHostException e) {
					System.out.println("Server does not respond");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Error Client:registerClient");
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private static void startUdpSocket() {
		/*new Thread() {
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
		}.start();*/	
	}
}

	
