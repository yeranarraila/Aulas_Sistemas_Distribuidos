import LamportCorba.*;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class Client {

    public static void main(String[] args) {
		short cliente = (short)-1;
		int idLocal = -1;
		int logado = 0;

		try {
			Servidor serverCommunication;

			// Inicializa o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);
			// Obt�m a refer�ncia do servidor de nomes (NameService)
			// Essa tarefa � realizada pelo ORB (resolve_initial_references)
			NamingContextExt nc = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));
			// Obt�m o objeto CORBA (gen�rico) do servidor HelloWorld
			// � o servidor de nomes que fornece essa refer�ncia (resolve)
			org.omg.CORBA.Object o = nc.resolve(nc.to_name("Lider"));
			// Transforma o objeto CORBA gen�rico num objeto CORBA HellowWorld
			serverCommunication = ServidorHelper.narrow(o);
			try{

				/*Requisicoes:
				 * 1: Hello World
				 * 2: Bad World for YOU
				 **/

				System.out.println(serverCommunication.requisicao_cliente(1));//envia a requisicao pro servidor

			}catch(Exception e)
			{
				o = nc.resolve(nc.to_name("Lider")); //voce ta ai?
				// Transforma o objeto CORBA gen�rico num objeto CORBA HellowWorld
				serverCommunication = ServidorHelper.narrow(o);
				System.err.println("Erro no Lider... execute o comando novamente");
			}
		}//try
			catch (Exception e) {
				e.printStackTrace();
			}
    }
}
