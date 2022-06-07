// package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		
		int port = 5000;
		
		try ( ServerSocket serverSocket = new ServerSocket(port) ){
			
			System.out.println("[+] Server listening on port " + port + "!");
			System.out.println("[+] Server is wainting for connections...");
				
			while ( true ) {
				
				// Wait for client connection
				Socket connectionSocket = serverSocket.accept();
				// Show IP address from client
				System.out.println("[+] Client " + serverSocket.getInetAddress().getHostAddress() + " conected!");
				// Open a thread for connected client
				HandleClient t = new HandleClient(connectionSocket);
				t.start();
			}
			
		} catch (Exception e) {
				System.out.println("Ops! Ocorreu um erro na conexão!");
		}

	}

}
