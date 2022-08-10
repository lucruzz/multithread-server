package loadbalancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import servidor.ProcessaRequisicao;
import servidor.Servidor;

public class HandleBalancer extends Thread {
	
	ConcurrentLinkedQueue<Servidor> queue = new ConcurrentLinkedQueue<Servidor>(); // lista para verificar disponibilidade
	ArrayList<Servidor> list = new ArrayList<Servidor>(); // lista de servidores
	Map<Integer, Integer> verify = new HashMap<Integer, Integer>();
	RequestQueue queueRequests = new RequestQueue();
	
	public HandleBalancer(ArrayList<Servidor> list, ConcurrentLinkedQueue<Servidor> queue, RequestQueue queueRequests){
		this.queue = queue;
		this.list = list;
		this.queueRequests = queueRequests;
		
		for (int i = 0; i < list.size(); i++) {
			queue.add(list.get(i));
			verify.put(list.get(i).getPort(), 0);
		}
	}
	
	@Override
	public void run() {
		while(true) {
			if(queueRequests.getSize() != 0) {
				Servidor serverHost = queue.poll();
				int serverfree = 0;
				
				// verifica se o servidor está ocupado
				if(verify.get(serverHost.getPort()) == 1) {
					queue.add(serverHost);
					continue;
				}
				try ( Socket lbsocket = new Socket(serverHost.getIp(), serverHost.getPort()) ){
					
					InputStreamReader inputreader;
			    	OutputStreamWriter outputwriter;
			    	
			    	BufferedReader inputbalancer;
			    	BufferedWriter outputbalancer;

			    	inputreader = new InputStreamReader(lbsocket.getInputStream());
					outputwriter = new OutputStreamWriter(lbsocket.getOutputStream());
					inputbalancer = new BufferedReader(inputreader);
					outputbalancer = new BufferedWriter(outputwriter);
					
					// enviando mensagem do load balancer para o servidor
			        outputbalancer.write("3");
			        outputbalancer.newLine();
			        outputbalancer.flush();
					
			        // recebe mensagem vinda do servidor fechando o servidor
			        serverfree = Integer.parseInt(inputbalancer.readLine());
			        
			        // marca o servidor como ocupado
			        verify.replace(serverHost.getPort(), serverfree);
			        
			        Request req = queueRequests.delRequest();
			        int port;
			        
			        if(req.getServerPort() != serverHost.getPort()) {
			        	req.setServerPort(serverHost.getPort());
						port = serverHost.getPort();//req.getServerPort();
			        }else {
						port = req.getServerPort();
			        }

			        lbsocket.close();

			        
					Socket connectionSocket = req.getConsocket();

					
					inputreader = new InputStreamReader(connectionSocket.getInputStream());
					outputwriter = new OutputStreamWriter(connectionSocket.getOutputStream());
					inputbalancer = new BufferedReader(inputreader);
					outputbalancer = new BufferedWriter(outputwriter);
					
					// enviando a porta do servidor para o cliente
			        outputbalancer.write(String.valueOf(port));
			        outputbalancer.newLine();
			        outputbalancer.flush();
			        
			        String answer = inputbalancer.readLine();
			        

			        if(req.getOperation() == 2){
						for(int i = 0; i < list.size() - 1; i++) {
	
							Servidor aux = queue.poll();
							int anotherPort = aux.getPort();

							if(port != anotherPort) {
								ProcessaRequisicao t = new ProcessaRequisicao();
								t.WriteFile(aux.getPath(), anotherPort, req.getNum(), answer);
							}

							queue.add(aux);
						}
					}
			        
			        // marca o servidor como livre
			        serverfree = 0;
			        verify.replace(serverHost.getPort(), serverfree);
				}catch(IOException e) {
					
					
				}
				queue.add(serverHost);
			}
			

		}
		
		
	}

}
