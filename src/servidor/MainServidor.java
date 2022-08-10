package servidor;

import java.io.IOException;
import java.net.UnknownHostException;


public class MainServidor {
	

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// Segundo: iniciar o servidor
		Servidor server1 = new Servidor(Integer.parseInt(args[0]), ".", "127.0.0.1", "localhost");
		// Servidor server1 = new Servidor(5002, ".","127.0.0.1", "localhost");
		// Servidor server1 = new Servidor(Integer.parseInt(args[0]), args[1], "127.0.0.1", "localhost");
		
		server1.rcvConnections();
		
	}
	

}
