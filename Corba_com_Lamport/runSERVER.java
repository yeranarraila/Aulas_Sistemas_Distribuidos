import LamportCorba.*;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;


public class runSERVER{

	private int id=-1;

	private void run(String[] args){

			if(args[0]!=null) {
					id=Integer.parseInt(args[0]);
				}else{
					System.err.println("Execute da seguinte maneira: java runSERVER <id>, onde <id> deve ser um nro inteiro positivo");
					System.exit(0);
				}

				// Inicializa o ORB
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

                try {

                        // Ativa o POA

                        org.omg.PortableServer.POA poa = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

                        poa.the_POAManager().activate();

                        // Instancia um objeto da classe Server

                        ServidorImpl server = new ServidorImpl();

                        // Transforma o objeto java (server) num objeto CORBA genérico (objCORBA)

                        org.omg.CORBA.Object objCORBA = poa.servant_to_reference(server);

                        // Obtém a referência (endereço) do servidor de nomes (NameService)

                        // Essa tarefa é realizada pelo ORB (orb.resolve_initial_references)

                        NamingContextExt nc = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));

                        // Registra a referência objCORBA no servidor de nomes usando o bind (pode ser tb com rebind)
						if(id==0){ //o Lider por default eh o servidor com id 0 no sistema
							nc.rebind(nc.to_name("Lider"), objCORBA);
							System.out.println("\n\n\n\n\n\n\n\n ------- Sou o Lider! -------\n\n\n\n\n\n\n\n");
							server.setMyName("Lider");
							server.entrarNoSD(objCORBA,"Lider"); //passa o objeto corba

						}else{ //se nao tiver o id =0, nao sera o lider
							nc.rebind(nc.to_name("Server"+id), objCORBA);  //registra o servidor "tenente"
							System.out.println("Sou o tenente "+id + "\n");

							//setando maliciosidade
							java.util.Scanner sc = new java.util.Scanner(System.in);
							System.out.println("Digite 0 para normal ou 1 para malicioso: ");
							int malicioso = sc.nextInt();

							while( malicioso!=1 && malicioso!=0 ){
								System.out.println("Voce nao sabe ler??? Digite 0 para normal ou 1 para malicioso: ");
								malicioso = sc.nextInt();
							}

							//buscando o Lider
							Servidor lider;
							org.omg.CORBA.Object o = nc.resolve(nc.to_name("Lider"));
							lider = ServidorHelper.narrow(o);
							server.setMyName("Server"+id);//registrando nome do servidor
							server.setLeaderLocation(o);
							server.setMaliciosidade(malicioso);
							lider.entrarNoSD(objCORBA,"Server"+id); //passa o objeto corba e seu id correspondente para o lider do S.D.

						}
                        orb.run();

			}catch (Exception e){
				e.printStackTrace();
			}
	}//run

	public static void main (String[] args){
		new runSERVER().run(args);
	}
}