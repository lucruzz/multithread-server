package cliente;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Cliente extends Thread  {

    private String ip;
    private int port;
    private Socket clientsocket;

    private static int min = 2;
    private static int max = 1000000;
    private static int timelapsed = 20000;

    public Cliente(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Socket getClientsocket() {
        return clientsocket;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
    	return max;
    }

    public Socket setClientsocket(String ip, int port) throws UnknownHostException, IOException {
        return this.clientsocket = new Socket(ip, port);
    }
    
    public int randomNumber(int minimum, int maximum) {
    	Random num = new Random();
    	return num.nextInt(maximum) + minimum;
    }

    public void connectBalancer() throws UnknownHostException, IOException, InterruptedException {

    	while(true) {
    		clientsocket = new Socket(ip, port);
    		
    		//InputStreamReader inputreader;
    		OutputStreamWriter outputwriter;
    		
    		//BufferedReader inputclient;
    		BufferedWriter outputclient;
    		
    		//inputreader = new InputStreamReader(clientsocket.getInputStream());
    		outputwriter = new OutputStreamWriter(clientsocket.getOutputStream());
    		//inputclient = new BufferedReader(inputreader);
    		outputclient = new BufferedWriter(outputwriter);
    		
    		// enviando mensagem do cliente para o load balancer
    		outputclient.write("[>] Client connected with success!");
    		outputclient.newLine();
    		outputclient.flush();
    		System.out.println("[+] Client connected to the Load Balancer with success!");
    		
    		// Decide a operacao que vai fazer
    		int operation = randomNumber(1,2);
    		int numberToProcess = 0;
    		
    		// manda a operacao para o Load Balancer armazenar
    		outputclient.write(String.valueOf(operation));
    		outputclient.newLine();
    		outputclient.flush();
    		
    		if(operation == 1) {
    			System.out.println("[+] READ operation!");
    		}else {
    			numberToProcess = randomNumber(min, max);
    			System.out.println("[+] WRITE operation with the number " + numberToProcess);
    			
    			// manda o numero para o Load Balancer armazenar
    			outputclient.write(String.valueOf(numberToProcess));
    			outputclient.newLine();
    			outputclient.flush();
    		}
    		
    		ClientWait t = new ClientWait(clientsocket, operation, numberToProcess);
    		t.start();
    		
    		Thread.sleep(timelapsed);

    		//clientsocket.close();
    		
    		System.out.println("[+] Client disconected from Load Balancer!");
    		
    	}
    	
        
    }
    
}