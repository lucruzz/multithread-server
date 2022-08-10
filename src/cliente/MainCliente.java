package cliente;

import java.io.IOException;
import java.net.UnknownHostException;

public class MainCliente {

        public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException  {

                //Cliente cliente1 = new Cliente("127.0.0.1", Integer.parseInt(args[0]));
                Cliente cliente1 = new Cliente("127.0.0.1", 6000);

                cliente1.connectBalancer();	


        }

}