package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Servidor{
	
	private String ip;
	private String hostname;
	private int port;
	private String path;
	private ServerSocket serversocket;
	
	public Servidor(int port, String path, String ip, String hostname) {
		this.port = port;
		this.ip = ip;
		this.path = path;
		this.hostname = hostname;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public int getPort() {
		return port;
	}
	
	public ServerSocket getServersocket() {
		return serversocket;
	}
	
	public ServerSocket setServersocket(int port) throws UnknownHostException, IOException {
		return this.serversocket = new ServerSocket(port);
	}
	
	public void rcvConnections() throws UnknownHostException, IOException {
		setServersocket(getPort());
		try {
			while ( true ) {
				// Wait for client connection
				System.out.println("[+] Server is waiting for connection on port " + getPort());
				Socket connectionSocket = getServersocket().accept();
				// Show IP address from client
				// System.out.println("[+] Client " + getServersocket().getInetAddress().getHostAddress() + " conected!");
				System.out.println("[+] Load Balancer is connected to the server!");
				// Open a thread for connected client
				ProcessaRequisicao t = new ProcessaRequisicao(connectionSocket, serversocket, getPort(), getPath());
				t.start();
			}
			
		}finally {
			serversocket.close();
		}
	}
	
}
