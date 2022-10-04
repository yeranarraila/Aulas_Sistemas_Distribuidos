import LamportCorba.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ServidorImpl extends ServidorPOA{

		private String myName="";
		//qtdMembros = membros do SD FORA o proprio servidor
		//malicioso: se =0 -> tenente nao malicioso, senao -> malicioso
		//decisaoTomada = decidira se sera hello ou badworld
		private int jaDecidi=0, malicioso=0,qtdMembros=0,decisaoTomada=0;

		private Map<String, org.omg.CORBA.Object> participantes = new HashMap<String, org.omg.CORBA.Object>();
		private ServerUtils.ServerDATA data = new ServerUtils.ServerDATA();
		private ServerUtils.SDMethods reply = new ServerUtils.SDMethods(data);

	/*---------------------------------- Atualiza o Sistema Distribuido ----------------------------------*/
		public void updateSD(org.omg.CORBA.Object[] participantes,String[] chaves){
			this.participantes.clear(); //limpa a lista atual de participantes
			try{
				for(int i = 0; i < participantes.length; i++){
					this.participantes.put(chaves[i].toString(),participantes[i]);//atualiza a lista de participantes
				}
				data.setParticipantes(this.participantes);//atualiza a lista de participantes
				System.out.println("S.D. atualizado! Participantes no S.D.: "+data.getParticipantes().size());
				this.qtdMembros = (data.getParticipantes().size())-1;
				}catch(Exception e){
					System.out.println("Erro ao receber nova lista de participantes. ");
				}
		}//updateSD

     /*---------------------------------- LIDER Insere uma nova Maquina ao Sistema Distribuido ----------------------------------*/
		public int entrarNoSD(org.omg.CORBA.Object servidor,String key){
				data.addParticipante(servidor,key); //adiciona um servidor ao S.D.
				this.qtdMembros = (data.getParticipantes().size())-1;

				//é necessário criar vetor, pois o RMI do CORBA nao aceita envio de HashMap, ArrayList etc

				org.omg.CORBA.Object[] servers = new org.omg.CORBA.Object[data.getParticipantes().size()]; //vetor de mesmo tipo e tamanho da lista
				String[] keys = new String[data.getParticipantes().size()];//vetor com chaves

				int j=0;
				for(String k : data.getParticipantes().keySet()){
					keys[j]=k; //adiciona na lista de chaves
					servers[j] =data.getParticipantes().get(k); //faz copia da lista de servidores no vetor
					j++;
				}
				//repassando a lista de participantes do SD atualizada para todos os servers

				for(int i = 0;i<data.getParticipantes().size();i++){
					Servidor o = ServidorHelper.narrow(data.getParticipantes().get(keys[i])); //pega endereço de todos os tenentes
					System.out.println("Tentando enviar lista de participantes para:" +keys[i]);
					o.updateSD(servers,keys); //envia a lista de servidores e chaves para todos os tenentes
					System.out.println("Enviado!");
				}

				return data.getParticipantes().size();
		}
/*---------------------------------- Troca de Mensagens Entre os Membros do S.D. ----------------------------------*/
		public void mensagemDoLider(int requisicao) //recebe a requisicao do Lider
		{
			//--------------------DEBUG
			System.out.println("\n\nDEBUG>Recebi do lider: "+requisicao+"\n\n");
			//--------------------
			this.jaDecidi = 0;
			if(this.malicioso == 0){
				data.addDecisao(requisicao);//adiciona a lista de decisoes
				System.out.println("Adicionei na lista de requisicoes recebidas\nAgora ela tem tamanho: "+data.getDecisoes().size());
			}else{
				if(requisicao == 1) data.addDecisao(2);//adiciona a lista de decisoes
				else data.addDecisao(1);//adiciona a lista de decisoes;

				System.out.println("Adicionei uma requisicao arbitraria na lista de requisicoes recebidas\nAgora ela tem tamanho: "+data.getDecisoes().size());
			}
			//--------------------DEBUG
			System.out.println("\n\nDEBUG>Enviando aos outros: "+requisicao+"\n\n");
			//------------------------
			reply.repassar(this.malicioso,this.jaDecidi,requisicao,this.myName,data.getParticipantes());//repassa para os outros membros

		}

		public void mensagemDosMembros(int requisicao){  //recebe dos outros servidores o que eles "dizem" ter recebido

			data.addDecisao(requisicao);//adiciona a lista de decisoes
			System.out.println("Adicionei na lista de requisicoes recebidas\nAgora ela tem tamanho: "+data.getDecisoes().size());
			if(!myName.equals("Lider")){
				if(data.getDecisoes().size() == this.qtdMembros){
					if(this.jaDecidi == 0){
						reply.decidir(); //decide usando lamport
						this.jaDecidi = 1;
					}
				}
			}
		}

/*---------------------------------- LIDER Recebe uma requisicao do Cliente e Repassa aos demais Servers ----------------------------------*/
		public String requisicao_cliente(int requisicao){ //requisicao recebida do cliente
			data.addDecisao(requisicao);//adiciona a lista de decisoes
			return reply.processarRequest(requisicao,data.getParticipantes(),this.myName); //repassa aos demais servers
		}
/*---------------------------------- Metodos para Controle Interno ----------------------------------*/
		public void setMyName(String myName){
			this.myName = myName;
		}
		public void setMaliciosidade(int malicioso){
			this.malicioso = malicioso;
		}
		public void setLeaderLocation(org.omg.CORBA.Object leaderLocation){
			data.setLeaderLocation(leaderLocation);
		}


}
