package loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import servidor.Servidor;

public class LoadBalancerMain {

	private static int base = 0; // this variable is going to be used as base for divide task that which server will receive
	private static ArrayList<Servidor> serversList = new ArrayList<Servidor>();
	private static ConcurrentLinkedQueue<Servidor> queue = new ConcurrentLinkedQueue<Servidor>();
	private static ConcurrentLinkedQueue<Servidor> auxQueue = new ConcurrentLinkedQueue<Servidor>();
	
	public static void main(String[] args) throws IOException {

		int N_SERVERS = 3;
		int lbport = 6000; // porta balanceador-cliente
		int clientID = 0;
		int serversPort = 5000;
		String pathFile = ".";

		// serversList.add(new Servidor(serversPort, "127.0.0.1", "localhost"));
		for (int i = 0; i < N_SERVERS; i++) {
			serversList.add(new Servidor(serversPort+i, pathFile, "127.0.0.1", "localhost"));
			auxQueue.add(serversList.get(i));
		}
		
		// Cria a fila de requisicoes
		RequestQueue queueRequests = new RequestQueue();
		
		HandleBalancer queueHandler = new HandleBalancer(serversList, queue, queueRequests);
		queueHandler.start();
		
		try (ServerSocket lbsocket = new ServerSocket(lbport)){
			
			System.out.println("[+] Load balancer is listening on port " + lbport);
			System.out.println("[+] Load balancer is waiting for requisitions...");
			
			
			
			while (true) {
				// Wait for clients connections
				Socket connectionSocket = lbsocket.accept();
				
				InputStreamReader inputreader;
		    	//OutputStreamWriter outputwriter;
		    	
		    	BufferedReader inputbalancer;
		    	//BufferedWriter outputbalancer;

		    	inputreader = new InputStreamReader(connectionSocket.getInputStream());
				//outputwriter = new OutputStreamWriter(connectionSocket.getOutputStream());
				inputbalancer = new BufferedReader(inputreader);
				//outputbalancer = new BufferedWriter(outputwriter);
				
				// recebe mensagem vinda do cliente estabelencendo conexao
				String messageFromClient = inputbalancer.readLine();
				System.out.println(messageFromClient);
				
				
				clientID += 1;
				System.out.println("[+] Client " + clientID + " connected to the Load Balancer!");
				
				// Recebe a operacao que vai ser realizada do cliente
				int dataFromClient = Integer.parseInt(inputbalancer.readLine());
				int operation = dataFromClient;
			
				base = ((base + 1) % serversList.size());
				int serverPort = serversList.get(base).getPort();
				int num = 0;
				// 1 == READ | 2 == WRITE
				if (operation == 1) {
					System.out.println("[+] Client " + clientID + " wants to process READ operation!");
					//Request request = new Request(operation, clientID, 0, );
				}else {
					System.out.println("[+] Client " + clientID + " wants to process WRITE operation!");
					
					// Recebe o numero que vai ser processado vindo do cliente
					num = Integer.parseInt(inputbalancer.readLine());
					System.out.println("[+] Number to process " + num);
				}
				
				// Marco a requisicao e adiciono na fila de requisicoes
				Request request = new Request(connectionSocket, operation, clientID, num, serverPort, lbport);
				queueRequests.addRequest(request);
				
				
			}
		}catch (IOException e) {
			e.printStackTrace();
		}


    }

}
