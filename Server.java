package server;

import java.net.ServerSocket;

public class Server {

	public static void main(String[] args) {
		
		int port = 5000;
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("[+] Escutando na porta 5000!");
				
			while ( true ) {
				HandleClient t = new HandleClient(serverSocket.accept());
				t.start();
			}
		} catch (Exception e) {
				System.out.println("Ops! Ocorreu um erro na conexão!");
		}

	}

}
