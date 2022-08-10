package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class ProcessaRequisicao extends Thread {
	
	private Socket clientSocket;
	private int serverPort;
	private String pathFile;

	public ProcessaRequisicao(Socket clientSocket, ServerSocket serversocket, int port, String path) {
		this.clientSocket = clientSocket;
		this.serverPort = port;
		this.pathFile = path;
	}
	
	public ProcessaRequisicao() {}
	
	@Override
	public void run() {
		
		System.out.println("[+] " + Thread.currentThread().getName() + " processing request from client!");
		
		try {
			
			InputStreamReader inputreader;
	    	OutputStreamWriter outputwriter;
	    	
	    	BufferedReader inputserver;
	    	BufferedWriter outputserver;

	    	inputreader = new InputStreamReader(clientSocket.getInputStream());
			outputwriter = new OutputStreamWriter(clientSocket.getOutputStream());
			inputserver = new BufferedReader(inputreader);
			outputserver = new BufferedWriter(outputwriter);

			// recebe mensagem vinda do load-balancer/cliente estabelencendo conexao
			String messageFromClient = inputserver.readLine();

			int operation = Integer.parseInt(messageFromClient);
			if(operation == 1) {
				System.out.println("READ");
				
				// voltando a resposta ao cliente
				outputserver.write("[>] File will be sent!");
				outputserver.newLine();
				outputserver.flush();
				
				// Read the File
				Path file = Paths.get(pathFile + "/" + serverPort + ".txt");
				
				List<String> fileLine = Files.readAllLines(file);
				
				// voltando a resposta ao cliente
				outputserver.write(String.valueOf(fileLine.size()));
				outputserver.newLine();
				outputserver.flush();
				
				for (String line : fileLine) {
					outputserver.write(line);
					outputserver.newLine();
					outputserver.flush();
				}


			}else if(operation == 2) {
				System.out.println("WRITE");
				// recebendo o numero a ser processado do cliente
				int num = Integer.parseInt(inputserver.readLine());
				// processando o numero
				String answer = isPrime(num);
				// voltando a resposta ao cliente
				outputserver.write(answer);
				outputserver.newLine();
				outputserver.flush();
				
				//Write to File
				//String fileName = pathFile + "/" + serverPort + ".txt";
				WriteFile(pathFile, serverPort, num, answer);

			}else if(operation == 3){
				System.out.println("FREE");
				// enviando mensagem do servidor para o load balancer ocupando o servidor 
		        outputserver.write("1");
		        outputserver.newLine();
		        outputserver.flush();
			}
			
			// Close socket
			clientSocket.close();
			
			
			System.out.println("[+] Client disconected!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String isPrime(int num) {
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
	
	public void WriteFile(String path, int serverPort, int num, String answer) throws IOException {
		
		
		String filePath = path + "/" + serverPort + ".txt";
		File file = new File(filePath);
		
		if(!file.exists()) {
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file, true);
		String s = "The number " + num + " " + answer + " prime!\n";
		fileWriter.write(s);
		fileWriter.close();
	}
	

}
