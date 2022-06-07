// package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class HandleClient extends Thread {
	
	Socket clientSocket;
	
	HandleClient(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		
		System.out.println("[+] " + Thread.currentThread().getName());
		
		try {
			// Receive from client
			Scanner recv = new Scanner(clientSocket.getInputStream());
			PrintStream out = new PrintStream(clientSocket.getOutputStream());
			
			while(recv.hasNextLine()) {
				int msgChegadaCliente = recv.nextInt();
				System.out.println("[+] " + Thread.currentThread().getName() + ": received the number " + msgChegadaCliente);
				String msgResposta = isPrime(msgChegadaCliente);
				out.println(msgResposta);
			}
			
			// Close data input and output stream
			recv.close();
			
			// Close socket
			clientSocket.close();
			
			System.out.println("[+] Client disconected!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String isPrime(int num) {
		if (num == 1) {
			return "is not";
		}
		
		for ( int i = 2; i < num ; i++ ) {
			if ( num % i == 0) {
				return "is not";
			}
		}
		return "is";
	}
}
