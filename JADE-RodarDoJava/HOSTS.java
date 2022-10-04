/************** BIBLIOTECAS REQUERIDAS **************************/

//NOME DOS AGENTES FIXOS DEVEM SER = NOMES DOS HOSTS PARA DAR CERTO A COMUNICAÇÃO
//O PRIMEIRO HOST CHAMA-SE N0

import java.io.FileReader;
import java.io.IOException;

import jade.core.Agent;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.behaviours.*;

import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionOntology;
import jade.domain.introspection.MovedAgent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.lang.String;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/************** END BIBLIOTECAS REQUERIDAS **************************/

public class HOSTS extends Agent{
	private Controller me;
	private ArrayList<String> nodos     = new ArrayList<String>();
	private ArrayList<String> resources = new ArrayList<String>();
	/****************** SETUP DO AGENTE *****************************/
	protected void setup(){
		try{
			this.me = (Controller)(getArguments()[0]);
			System.out.println("HOST: meu nome é: "+me.getName());

			setConnection();//informa ao host os nodos aos quais o mesmo está conectado
			me.setNodos(nodos);

			if(me.getName().equals("N0")){
				// COMPORTAMENTO 01: CASO NODO SEJA HOME (primeiro host)
			//	Behaviour b1 = new CreateMobileAgents(this,me); //cria agentes móveis <-- startar no homebehaviours!
				Behaviour b2 = new HomeBehaviours(this,me);//trata requests
			//	addBehaviour(b1);
				addBehaviour(b2);
			}

			else{
				// COMPORTAMENTO 02: CASO NODO SERVIDOR DE RECURSOS

				setResources(); //informa ao host os recursos disponíveis
				me.setResources(resources);

  				Behaviour b3 = new ServerBehaviours(this,me);
   				addBehaviour(b3);
   				}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void setResources(){
		try{
			FileReader file = new FileReader( "RESOURCES.config" );

			if(file == null)
				System.err.println("ERROR> 'RESOURCES.config' NOT FOUND!");

         	Scanner in = new Scanner( file );
         	String  rs = in.next();

         	if(rs.isEmpty())
         		System.err.println("ERROR> 'RESOURCES.config' IS EMPTY!");

         	StringTokenizer st = new StringTokenizer(rs,":"); //no resources.config os recursos devem estar no formato Rx:Ry
         	while(st.hasMoreTokens()){
         		resources.add(st.nextToken());
         	}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void setConnection(){
		//System.out.println("Setting up connections...");
		try {
			String linha="";
	 	 	FileReader file = new FileReader( "HOSTS.config" );
	 	 	if(file == null){
				System.err.println("ERROR> 'HOSTS.config' NOT FOUND!");
			}
         	Scanner in = new Scanner( file );
         	while (in.hasNext()) {
         		linha = in.next();
         		if (linha.contains("host")) {
					StringTokenizer st = new StringTokenizer(linha,"=");
					while(st.hasMoreTokens()){
						linha = st.nextToken();
							if(linha.equals("host"))
            					nodos.add(st.nextToken());
					}
         		}
         	}
         	//System.out.println(nodos.size()+" are setup.");
           		in.close();
    		} catch (IOException e) {
    		}
		}
}