// package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		String localhost = "127.0.0.1";
		int port = 5000;
		int min = 2;
		int max = 1000000;
		
		try ( Socket mysocket = new Socket(localhost , port) ){
			
			System.out.println("[+] Client connected!");
			
			
			PrintStream messageTosend = new PrintStream(mysocket.getOutputStream());
			
			while( true) {
				
				Random num = new Random();
				int sendInt = num.nextInt(max) + min;
				
				messageTosend.println(sendInt);
				
				Scanner recv = new Scanner(mysocket.getInputStream());
				
				String recvString = recv.nextLine();
				System.out.println(sendInt);
				System.out.println("Number " + sendInt + " " + recvString + " prime!");
				
				Thread.sleep(50000); // 50 milesegundos
				
			}
	
		}catch (Exception e) {
			System.out.println("Nenhuma conexão foi estabelecida!");
		}

	}

}
