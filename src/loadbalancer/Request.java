package loadbalancer;

import java.net.Socket;

public class Request {
	
	private int operation; // operacao que esta sendo realizada
	private int id; // id do cliente que solicitou
	private int num; // numero aleatorio para processar a verificacao do primo
	private int serverPort; // numeor da porta do servidor
	private int balancerPort; // numero da porta do balanceador
	private Socket consocket;
	
	public Request(Socket consocket, int operation, int id, int num, int serverPort, int balancerPort) {
		this.consocket = consocket;
		this.operation = operation;
		this.id = id;
		this.num = num;
		this.serverPort = serverPort;
		this.balancerPort = balancerPort;
		
	}
	
	public Socket getConsocket() {
		return consocket;
	}

	public int getOperation() {
		return operation;
	}

	public int getId() {
		return id;
	}

	public int getNum() {
		return num;
	}
	
	public int setServerPort(int port) {
		return port;
	}

	public int getServerPort() {
		return serverPort;
	}
	
	public int getBalancerPort() {
		return balancerPort;
	}
	
	public void printRequest() {
		System.out.println("[REQUEST INFORMATIONS]");
		System.out.println("[+] SERVER PORT:" + getServerPort() );
		System.out.println("[+] NUMBER     :" + getNum() );
		System.out.println("[+] CLIENTE ID :" + getId() );
	}
	
	

}
