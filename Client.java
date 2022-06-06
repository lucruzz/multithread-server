package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		String localhost = "127.0.0.1";
		
		try {
			Socket mysocket = new Socket(localhost , 5000);
			System.out.println("[+] Client connected!");
			PrintStream message = new PrintStream(mysocket.getOutputStream());
			message.println("Testando a testa!");
		}catch (Exception e) {
			System.out.println("Nenhuma conexão foi estabelecida!");
		}

	}

}
