package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientWait extends Thread{
	
	private Socket clientsocket;
	private int operation;
	private int number;
	private String localhost = "127.0.0.1";
	
	public ClientWait(Socket clientsocket, int operation, int number ) {
		this.clientsocket = clientsocket;
		this.operation = operation;
		this.number = number;
	}
	
    @Override
    public void run() {

    	int port = 0;
    	
    	try {
    		InputStreamReader inputreader;
        	OutputStreamWriter outputwriter;
        	
        	BufferedReader inputclientBalancer;
        	BufferedWriter outputclientBalancer;
        	BufferedReader inputclientServer;
        	BufferedWriter outputclientServer;
    		
			inputreader = new InputStreamReader(clientsocket.getInputStream());
			outputwriter = new OutputStreamWriter(clientsocket.getOutputStream());
			inputclientBalancer = new BufferedReader(inputreader);
			outputclientBalancer = new BufferedWriter(outputwriter);
			
			//clientsocket.close();
			
			// Recebe a porta do servidor que vai processar a requisicao do cliente
			int dataFromLoadBalancer = Integer.parseInt(inputclientBalancer.readLine());
			port = dataFromLoadBalancer;
			System.out.println("[+] Client will talk to server on port " + port + "!");
			
			Socket mysocket = new Socket(localhost, port);
			
			inputreader = new InputStreamReader(mysocket.getInputStream());
			outputwriter = new OutputStreamWriter(mysocket.getOutputStream());
			inputclientServer = new BufferedReader(inputreader);
			outputclientServer = new BufferedWriter(outputwriter);
			
			// enviando a operacao para o servidor
			outputclientServer.write(String.valueOf(operation));
			outputclientServer.newLine();
			outputclientServer.flush();
			
			String answer = null;
			
			if(operation == 1) {
				// recebendo a resposta do servidor
				answer = inputclientServer.readLine();
				System.out.println(answer);
				
				// recebendo a resposta do servidor
				int numLinesFile = Integer.parseInt(inputclientServer.readLine());
				System.out.println("=======================================================");
				for (int i = 0; i < numLinesFile; i++) {
					String line = inputclientServer.readLine();
					System.out.println(line);
				}
				System.out.println("=======================================================");
			}
			if(operation == 2) {
				// enviando o numero para o servidor
				outputclientServer.write(String.valueOf(number));
				outputclientServer.newLine();
				outputclientServer.flush();
				
				// recebendo a resposta do servidor
				answer = inputclientServer.readLine();
				
				System.out.println("[+] Number " + number + " " + answer + " prime! [+]");
	    		
			}

			// enviando a operacao para o servidor
			outputclientBalancer.write(answer);
			outputclientBalancer.newLine();
			outputclientBalancer.flush();
			
			mysocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

}
