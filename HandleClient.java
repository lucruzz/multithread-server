package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HandleClient extends Thread {
	
	Socket clientSocket;
	
	HandleClient(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		System.out.println("Thread " + this.getId() + ": Cliente conectado!");
		
		InputStreamReader data;
		try {
			data = new InputStreamReader(clientSocket.getInputStream());
			
			BufferedReader message = new BufferedReader(data);

			System.out.println(message.readLine());
			
			Thread.sleep(15000);
			
			clientSocket.close();
			
			System.out.println("Cliente desconectado!");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
